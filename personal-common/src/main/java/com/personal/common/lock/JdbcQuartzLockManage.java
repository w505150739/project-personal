package com.personal.common.lock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


/**
 * 基于数据库实现的分布锁
 * 
 * 可能场景：A线程获得锁，由于执行时间过长，已经超出有效时间，此时B线程获得锁。
 * 建议：可将有效时间设置长一些，超过任务执行时间，这样就算A线程死掉，B线程也可在一段时间后获得锁。
 * @author huangga
 *
 */
public class JdbcQuartzLockManage implements QuartzLockManage{
	
	private String UNLOCK = "unlock"; 
	private String LOCK = "lock";
	
	
	private static String SELECT_BY_LOCK = "select id,jobid,state,start_time,active_time,version from t_quartz_lock where id = ?";
	
	private static String INSERT = "insert into t_quartz_lock (id,jobid,state,start_time,active_time,version) values (?,?,?,?,?,?)";
	
	private static String UPDATE = "UPDATE t_quartz_lock SET jobid=?,state =?,start_time =?,active_time=?,version =? where id = ? and state = ? AND version = ?";
	
	//清除
	private static String CLEAR_LOCK = "UPDATE t_quartz_lock SET jobid=?,state =? ,start_time = ?,active_time=?  where id = ? and jobid=?";
	
	//重置锁
	private static String RESET_LOCK = "UPDATE t_quartz_lock SET jobid=?,state =? ,start_time = ?,active_time=?  where id = ?";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private PlatformTransactionManager transactionManager;



	/**
	 * 基于数据库实现乐观锁
	 */
	@Override
	public synchronized boolean holdLock(String lock,String jobId, long time) {
		TransactionDefinition def = new DefaultTransactionDefinition();
	    TransactionStatus status = transactionManager.getTransaction(def);
	    boolean result = false;
	    int line = 0;
	    try {
	    	Map<String, Object> map = getLock(lock,jobId);
	    	if(!isLock(map)){
	    		line = doLock(map,lock,jobId,time);
	    	}
	    	transactionManager.commit(status);
	    } catch (DataAccessException e) {
	         System.out.println("Error in creating record, rolling back");
	         transactionManager.rollback(status);
	         result = false;
	    }
	    
	    if(line==1){
	    	result = true;
	    }
		return result;
	}
	
	@Override
	public synchronized boolean clearLock(String lock,String jobId) {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		try {
			jdbcTemplate.update(CLEAR_LOCK, "NoneJobId",UNLOCK,date,0,lock,jobId);
			System.out.println("relese lock:"+lock +" succeed! ");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param map
	 * @param lock
	 * @param jobId 
	 * @param time
	 */
	private int doLock(Map<String, Object> map, String lock, String jobId, long time) {
		
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		
		int line = 0;
		Integer version = (Integer)map.get("version");
		try {
			line = jdbcTemplate.update(UPDATE,
					jobId,LOCK, date,time,version+1, //更新参数
					lock,UNLOCK,version); //查询条件：id,未锁,版本号
		} catch (Exception e) {
			line = 0;
		}
		
		return line;
	}



	/**
	 * 判断是否上锁
	 * @param map
	 * @return
	 */
	private boolean isLock(Map<String, Object> map) {
		boolean islock = true;
		if(null != map && null != map.get("state")){
			String state = (String) map.get("state");
			if(UNLOCK.equals(state)){
				islock = false;
			}
		}
		return islock;
	}



	/**
	 * 获取锁，若无则创建锁
	 * @param lock
	 * @param jobId 
	 * @return
	 */
	private Map<String, Object> getLock(String lock, String jobId) {
		
		Map<String, Object> map = queryLock(lock);
		if(null == map || StringUtils.isBlank((String) map.get("id"))){
			if(init(lock)){
				map = queryLock(lock);
			}
		}else if (null != map && (Integer)map.get("active_time")>0) {
			
			if(isLost(map)){
				//清除
				if(resetLock(lock,jobId)){
					//查询
					map = queryLock(lock);
				}
			}
			
		}
		
		return map;
	}
	
	/**
	 * 重置锁
	 * @param lock
	 * @param jobId
	 * @return
	 */
	private boolean resetLock(String lock,String jobId) {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		try {
			jdbcTemplate.update(RESET_LOCK, "NoneJobId",UNLOCK,date,0,lock);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//是否过期
	private boolean isLost(Map<String, Object> map){
		
		boolean islost = false;
		
		java.sql.Timestamp starttime = (java.sql.Timestamp)map.get("start_time");
		//开始时间
		Date startTime = new Date(starttime.getTime());
		//有效时间
		Integer time = (Integer)map.get("active_time");
		
		long now = System.currentTimeMillis();
		if(null != startTime){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startTime);
			long start = calendar.getTimeInMillis();
			
			if((now - start) > time*1000){
				islost = true;
			}
			
		}
		return islost;
	}
	
	
	/**
	 * 查询锁信息
	 * @param lock
	 * @return
	 */
	private Map<String, Object> queryLock(String lock) {
		
		Map<String, Object> map = null;
		try {
			map = jdbcTemplate.queryForMap(SELECT_BY_LOCK, lock);
		} catch (EmptyResultDataAccessException e) {
			map = null;
		}
		return map;
	}
	
	
	/**
	 * 创建无状态锁
	 * @param lock
	 * @return
	 */
	private boolean init(String lock){
		
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		
		boolean result = false;
		int line = 0;
		try {
			Integer version = 1;
			line = jdbcTemplate.update(INSERT, lock,"NoneJobId", UNLOCK, date,0, version);
		} catch (Exception e) {
			line = 0;
		}
		if(line == 1){
			result = true;
		}
		return result;
	}
	

	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}


	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	

	
	
}
