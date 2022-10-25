package com.zsl0.util.excel;

import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author zsl0
 * create on 2022/10/17 9:05
 */
public class ExcelUtil {

    static Logger log = LoggerFactory.getLogger(Exception.class);

    /**
     * 设置excel 标题
     */
    public static void setTitle(XSSFRow head, String... args) {
        for (int i = 0; i < args.length; i++) {
            head.createCell(i).setCellValue(args[i]);
        }
    }



    /**
     * excel 文件下载
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @param filename 文件名
     * @param out excel对象
     */
    public static void sendFile(HttpServletRequest req, HttpServletResponse resp, String filename, POIXMLDocument out) throws IOException {
        //在回传前，通过响应头告诉客户端返回的数据类型
        resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        //告诉客户端收到的数据是用于下载使用（通过响应头）
        if (req.getHeader("User-Agent").contains("Firefox")) {
            //火狐浏览器 Base64
            resp.setHeader("Content-Disposition", "attachment; filename==?UTF-8?B?" + new BASE64Encoder().encode(filename.getBytes(StandardCharsets.UTF_8)) + "?=");
        } else {
            //谷歌 IE URLEncoder
            resp.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        }
        //获取回传的输出流 并输出文件
        ServletOutputStream outputStream = resp.getOutputStream();
        out.write(outputStream);
        log.debug("send '{}' completion", filename);
    }

}
