package com.zsl0.util.office;

import cn.hutool.core.io.IoUtil;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import com.aspose.words.Document;
import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author zsl0
 * created on 2022/12/12 16:33
 */
public class WordPdfUtil {
    /**
     * 获取破解码文件内容
     * @return
     */
    public static boolean getLicense() {
        boolean result = false;
        InputStream is = null;
        try {
            //  license.xml应放在..\WebRoot\WEB-INF\classes路径下
            is = WordPdfUtil.class.getClassLoader().getResourceAsStream("static/license.xml");
            License license = new License();
            license.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(is);
        }
        return result;
    }

    /**
     * word转pdf文件
     *
     * @param word 原文件地址
     * @param pdf  保存的pdf文件地址
     * @return
     */
    public static void wordConvertPdf(String word, String pdf) {
        // 验证License 若不验证则转化出的pdf文档会有水印产生
        if (!getLicense()) {
            return;
        }
        FileOutputStream os = null;
        //判断是否windows系统，Linux要读取字体，否则pdf字体为方格
        OsInfo osInfo = SystemUtil.getOsInfo();
        if (osInfo.isLinux()) {
            String path = WordPdfUtil.class.getClassLoader().getResource("static/simsun.ttc").getPath();
            FontSettings.getDefaultInstance().setFontsFolder(path, true);
        }
        try {
            // 新建一个空白pdf文档
            File file = new File(pdf);
            os = new FileOutputStream(file);
            // Address是将要被转化的word文档
            Document doc = new Document(word);
            // 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            doc.save(os, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(os);
        }
    }

    /**
     * word转pdf文件
     *
     * @param wordis 原文件地址
     * @param pdfos  保存的pdf文件地址
     * @return
     */
    public static void wordConvertPdf(InputStream wordis, OutputStream pdfos) {
        // 验证License 若不验证则转化出的pdf文档会有水印产生
        if (!getLicense()) {
            return;
        }
        //判断是否windows系统，Linux要读取字体，否则pdf字体为方格
        OsInfo osInfo = SystemUtil.getOsInfo();
        if (osInfo.isLinux()) {
//            String path = WordPdfUtil.class.getClassLoader().getResource("static/simsun.ttc").getPath();
//            FontSettings.getDefaultInstance().setFontsFolder(path, true);
            FontSettings.getDefaultInstance().setFontsFolder("/usr/share/fonts", true);
        }
        try {
            // Address是将要被转化的word文档
            Document doc = new Document(wordis);
            // 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            doc.save(pdfos, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        InputStream resourceAsStream = WordPdfUtil.class.getClassLoader().getResourceAsStream("static/abc.doc");
        long start = System.currentTimeMillis();
//            wordConvertPdf("/Users/zsl0/IdeaProjects/gitProjects/zsl0-tools/src/main/resources/static/abc.doc", "/Users/zsl0/Desktop/abc.pdf");
        ByteArrayOutputStream pdfos = new ByteArrayOutputStream();
        wordConvertPdf(resourceAsStream, pdfos);
        System.out.println(System.currentTimeMillis() - start + " ms, bytesize=" + pdfos.toByteArray().length);
        IoUtil.copy(new ByteArrayInputStream(pdfos.toByteArray()), Files.newOutputStream(Paths.get("/Users/zsl0/Desktop/abc.pdf")));
    }
}
