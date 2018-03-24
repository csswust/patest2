package com.csswust.patest2.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在线用户监听器
 *
 * @author 杨顺丰
 */
@WebListener
public class OnlineListener implements HttpSessionListener {
    public final static Map<String, HttpSession> onlineMap = new ConcurrentHashMap<>();

    // 新建一个session时触发此操作
    public void sessionCreated(HttpSessionEvent httpEvent) {
        HttpSession httpSession = httpEvent.getSession();
        onlineMap.put(httpSession.getId(), httpSession);
    }

    // 销毁一个session时触发此操作
    public void sessionDestroyed(HttpSessionEvent httpEvent) {
        HttpSession httpSession = httpEvent.getSession();
        onlineMap.remove(httpSession.getId());
    }
}