package com.zsl0.util.office;

import cn.hutool.core.io.IoUtil;
import com.zsl0.util.spring.WebUtil;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zsl0
 * create on 2022/10/17 9:05
 */
public class ExcelUtil {

    static Logger log = LoggerFactory.getLogger(ExcelUtil.class);

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
    public static void webDownload(HttpServletRequest req, HttpServletResponse resp, String filename, POIXMLDocument out) throws IOException {
        //在回传前，通过响应头告诉客户端返回的数据类型
        WebUtil.setResponseByDownload(req, resp, filename);
        //获取回传的输出流 并输出文件
        ServletOutputStream outputStream = resp.getOutputStream();
        out.write(outputStream);
        outputStream.flush();
        IoUtil.close(outputStream);
        log.debug("send '{}' completion", filename);
    }

}
