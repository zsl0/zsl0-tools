package com.zsl0.util.log;

/**
 * @author zsl0
 * created on 2023/4/17 10:11
 */
public class LogUtil {

    /**
     * 获取异常栈跟踪信息
     * @param t 异常
     * @return 字符串信息
     */
    public static String getLogStackTrace(Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append(t.toString());
        for (StackTraceElement element : t.getStackTrace()) {
            sb.append("\tat ").append(element.toString());
        }
        return sb.toString();
    }

}
