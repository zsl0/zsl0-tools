package com.zsl0.util.file;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author zsl0
 * create on 2022/10/19 15:32
 */
public class FileUtil {

    /**
     * 压缩多个文件为压缩包
     * @param fileIS Key,Value 文件名-流
     * @param os 输出流
     */
    public static void fileZip(Map<String, InputStream> fileIS, OutputStream os) {
        ZipOutputStream zos = new ZipOutputStream(os);

        int i = 0;
        for (Map.Entry<String, InputStream> entry : fileIS.entrySet()) {
            String filename = entry.getKey();
            InputStream is = entry.getValue();
            try {
                zos.putNextEntry(new ZipEntry(filename));

                int len;
                byte[] buffer = new byte[1024 * 8];
                while ((len = is.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }

                zos.closeEntry();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setResponseByDownload(HttpServletRequest req, HttpServletResponse resp, String filename) throws UnsupportedEncodingException {
        //在回传前，通过响应头告诉客户端返回的数据类型
        resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        //告诉客户端收到的数据是用于下载使用（通过响应头）
        if (req.getHeader("User-Agent").contains("Firefox")) {
            //火狐浏览器 Base64
            resp.setHeader("Content-Disposition", "attachment; filename==?UTF-8?B?" + Arrays.toString(Base64.getEncoder().encode(filename.getBytes(StandardCharsets.UTF_8))) + "?=");
        } else {
            //谷歌 IE URLEncoder
            resp.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        }
    }
}
