package com.zsl0.util.spring;

import cn.hutool.core.io.IoUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

/**
 * @author zsl0
 * created on 2022/12/12 20:29
 */
public class WebUtil {

    /**
     * 文件下载 设置相应头信息
     * @param req ServletRequest对象
     * @param resp ServletResponse对象
     * @param filename 文件名
     * @throws UnsupportedEncodingException 不支持的编码异常
     */
    public static void setResponseByDownload(HttpServletRequest req, HttpServletResponse resp, String filename) throws UnsupportedEncodingException {
        //在回传前，通过响应头告诉客户端返回的数据类型
        resp.setContentType("application/octet-stream");
        //告诉客户端收到的数据是用于下载使用（通过响应头）
        if (req.getHeader("User-Agent").contains("Firefox")) {
            //火狐浏览器 Base64
            resp.setHeader("Content-Disposition", "attachment; filename==?UTF-8?B?" + Arrays.toString(Base64.getEncoder().encode(filename.getBytes(StandardCharsets.UTF_8))) + "?=");
        } else {
            //谷歌 IE URLEncoder
            resp.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        }
    }


    /**
     * excel 文件下载
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @param filename 文件名
     * @param is excel对象
     */
    public static void sendFile(HttpServletRequest req, HttpServletResponse resp, String filename, InputStream is) throws IOException {
        //在回传前，通过响应头告诉客户端返回的数据类型
        WebUtil.setResponseByDownload(req, resp, filename);
        //获取回传的输出流 并输出文件
        ServletOutputStream outputStream = resp.getOutputStream();
        IoUtil.copy(is, outputStream);
        outputStream.flush();
        IoUtil.close(outputStream);
        IoUtil.close(is);
    }


    /**
     * 获取ServletRequest(根据springboot获取)
     * @return ServletResponse对象
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }


    /**
     * 获取ServletResponse(根据springboot获取)
     * @return ServletResponse对象
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
    }

    /**
     * 获取ip地址
     * @return ip字符串
     */
    public static String getIp() {
        HttpServletRequest request = getRequest();
        return request.getHeader("host");
    }
}
