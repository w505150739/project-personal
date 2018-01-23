package com.personal.common.lock;

/**
 * 
 * 需考虑的问题：
 * 1、A线程获得锁，由于执行时间过长，已经超出有效时间，若此时B线程获得锁。当B在执行时，A执行完成并释放锁，这样会使B的锁也失效。
 * @author huangga
 *
 */
public interface QuartzLockManage {
	
	
	/**
	 * 获取锁
	 * @param lock 锁id
	 * @param jobId 任务id
	 * @param time 有效时间
	 * @return
	 */
	public boolean holdLock(String lock, String jobId, long time);
	

	/**
	 * 清除锁
	 * @param lock 锁名称
	 * @param jobId 任务id
	 * @return
	 */
	public boolean clearLock(String lock, String jobId);
	
}
