package com.csswust.patest2.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * @author 杨顺丰
 */
public class SystemUtil {
    public static void main(String[] args) throws Exception {
        // System.out.println(property());
        System.out.println("----------------------------------");
    }

    public static List<SystemInfo> servlet(HttpServletRequest request) throws Exception {
        List<SystemInfo> list = new LinkedList<SystemInfo>();
        list.add(new SystemInfo("realPath", "项目所在服务器的全路径",
                request.getServletContext().getRealPath("/")));
        list.add(new SystemInfo("contextPath", "服务器地址前缀", request.getServerName() + ":"
                + request.getServerPort() + request.getContextPath()));
        // list.add(new SystemInfo("serverName", "服务器地址/域名",
        // request.getServerName()));
        // list.add(new SystemInfo("serverPort", "服务器端口",
        // request.getServerPort()));
        // list.add(new SystemInfo("contextPath", "项目名称",
        // request.getContextPath()));
        return list;
    }

    public static List<SystemInfo> property() throws Exception {
        List<SystemInfo> list = new LinkedList<SystemInfo>();
        InetAddress addr = InetAddress.getLocalHost();
        Runtime runtime = Runtime.getRuntime();
        Properties props = System.getProperties();

        list.add(new SystemInfo("ip", "服务器ip地址", addr.getHostAddress()));
        list.add(new SystemInfo("hostName", "服务器主机名", addr.getHostName()));
        // list.add(new SystemInfo("userName", "用户的账户名称",
        // props.getProperty("user.name")));
        // list.add(new SystemInfo("userHome", "用户的主目录",
        // props.getProperty("user.home")));
        // list.add(new SystemInfo("userDir", "用户的当前工作目录",
        // props.getProperty("user.dir")));

        list.add(new SystemInfo("totalMemory", "JVM可以使用的总内存",
                runtime.totalMemory() / 1024 / 1024 + "M"));
        list.add(new SystemInfo("freeMemory", "JVM可以使用的剩余内存",
                runtime.freeMemory() / 1024 / 1024 + "M"));
        list.add(new SystemInfo("availableProcessors", "JVM可以使用的处理器个数",
                runtime.availableProcessors()));
        list.add(new SystemInfo("javaVersion", "Java的运行环境版本", props.getProperty("java.version")));
        list.add(new SystemInfo("javaHome", "Java的安装路径", props.getProperty("java.home")));
        list.add(new SystemInfo("vmVersion", "Java的虚拟机规范版本",
                props.getProperty("java.vm.specification.version")));

        list.add(new SystemInfo("osName", "操作系统的名称", props.getProperty("os.name")));
        list.add(new SystemInfo("osArch", "操作系统的构架", props.getProperty("os.arch")));
        list.add(new SystemInfo("osVersion", "操作系统的版本", props.getProperty("os.version")));
        return list;
    }
}
