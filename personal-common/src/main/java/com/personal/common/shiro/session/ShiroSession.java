package com.personal.common.shiro.session;

import com.personal.common.shiro.ShiroRedisManager;
import com.personal.common.util.LogUtil;
import com.personal.common.util.SerializeUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author liuyuzhu
 * @description: 继承Shiro的AbstractSessionDAO，实现对session操作的方法，将Shiro会话保存到redis中，实现session共享
 * @date 2018/1/20 1:43
 */
@Setter
@Getter
public class ShiroSession extends AbstractSessionDAO{

    private static LogUtil logger = LogUtil.getLogger(ShiroSession.class);

    /**
     * session前缀，用于识别缓存中的session
     */
    private String keyPrefix = "sessionId-";

    @Autowired
    private ShiroRedisManager redisManager;

    public ShiroRedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(ShiroRedisManager redisManager) {
        this.redisManager = redisManager;
        //初始化redisManager
        this.redisManager.init();
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if(logger.isDebugEnabled()){
            logger.info("update session: "+session.getId());
        }
        this.saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if(session == null || session.getId() == null){
            logger.error("session or session id is null");
            return;
        }
        redisManager.del(this.getByteKey(session.getId()));
    }
    /**
     * 获取当前活动的session
     * 系统启动时，获取所有缓存中的会话，用于判断会话是否过期。
     */
    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<Session>();

        Set<byte[]> keys = redisManager.keys(getKeyPrefix() + "*");
        if(keys != null && keys.size()>0){
            for(byte[] key:keys){
                Session s = (Session) SerializeUtil.deserialize(redisManager.get(key));
                sessions.add(s);
            }
        }
        return sessions;
    }
    /**
     * 生成sessionID,保存到redis
     */
    @Override
    public Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        if(logger.isDebugEnabled()){
            logger.info("create and save session:"+sessionId);
        }
        return sessionId;
    }
    /**
     * 获取session
     */
    @Override
    public Session doReadSession(Serializable sessionId) {
        if(sessionId == null){
            logger.error("session id is null");
            return null;
        }
        if(logger.isDebugEnabled()){
            logger.debug("read sessionid:"+this.getByteKey(sessionId));
        }
        Session s = (Session) SerializeUtil.deserialize(redisManager.get(this.getByteKey(sessionId)));
        return s;
    }

    /**
     * 保存session
     * @param session
     * @throws UnknownSessionException
     */
    public void saveSession(Session session) throws UnknownSessionException {
        if(session == null || session.getId() == null){
            logger.error("session or session id is null");
            return;
        }
        if(logger.isDebugEnabled()){
            logger.debug("save sessionid:"+this.getByteKey(session.getId()));
        }
        byte[] key = getByteKey(session.getId());
        byte[] value = SerializeUtil.serialize(session);
        session.setTimeout(redisManager.getExpire()*1000);
        this.redisManager.set(key, value, redisManager.getExpire());
    }

    /**
     * 获得byte[]型的key
     * @param sessionId
     * @return
     */
    private byte[] getByteKey(Serializable sessionId){
        String preKey = getKeyPrefix() + sessionId;
        return preKey.getBytes();
    }
}
