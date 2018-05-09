package com.csswust.patest2.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在线用户监听器
 *
 * @author 杨顺丰
 */
@WebListener
public class OnlineListener implements HttpSessionListener,HttpSessionAttributeListener {
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

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        HttpSession httpSession = event.getSession();
        onlineMap.put(httpSession.getId(), httpSession);
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        HttpSession httpSession = event.getSession();
        onlineMap.put(httpSession.getId(), httpSession);
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        HttpSession httpSession = event.getSession();
        onlineMap.put(httpSession.getId(), httpSession);
    }
}