package com.zsl0.util.freemarker;

import cn.hutool.core.io.IoUtil;
import freemarker.template.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * todo：
 *  1.处理异常
 *  2.编写样例说明
 *  3.优化process()方法
 *
 *  *         案例：
 *  *          test.ftl 文件内容：
 *  *              username: ${user.name}
 *  *              password: ${user.password}
 * <div>
 *
 *
 *         // 创建Configuration，建议使用单例模式
 *         Configuration configuration = getConfiguration("src/main/resources/templates");
 *
 *         // 获取模版
 *         Template template = getTemplate("test.ftl", configuration);
 *
 *         // 构建数据
 *         Map<String, Object> root = new HashMap<>();
 *         Map<String, Object> user = new HashMap<>();
 *         root.put("user", user);
 *         user.put("name", "zsl0");
 *         user.put("password", "zsl");
 *
 *         // 生成执行后的数据数组
 *         byte[] process = process(root, template);
 *         String s = new String(process, StandardCharsets.UTF_8);
 *         System.out.println(s);
 * </div>
 *
 * @author zsl0
 * created on 2022/11/28 21:17
 */
public class FreemarkerUtil {

    /**
     * 创建Configuration,处理创建和 缓存 预解析模板(比如 Template 对象)的工作
     *  注意：不需要重复创建 Configuration 实例； 它的代价很高，尤其是会丢失缓存。Configuration 实例就是应用级别的单例。
     * @param directory ftl模版路径
     */
    public static Configuration getConfiguration(String directory) {
        return getConfiguration(directory, Configuration.VERSION_2_3_22, StandardCharsets.UTF_8, TemplateExceptionHandler.RETHROW_HANDLER);
    }

    public static Configuration getConfiguration(String directory, Version version, Charset charset, TemplateExceptionHandler templateExceptionHandler) {
        Configuration configuration = null;
        try {
            configuration = new Configuration(version);

            File dirFile = Paths.get(directory).toFile();

            // 判断参数路径是否存在
            if (!dirFile.exists()) {
                throw new RuntimeException(String.format("Directory does not exist！ dir=%s", directory));
            }

            // 设置加载模版路径
            configuration.setDirectoryForTemplateLoading(dirFile);

            // 设置默认编码格式
            configuration.setDefaultEncoding(charset.name());

            configuration.setTemplateExceptionHandler(templateExceptionHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return configuration;
    }

    /**
     * 获取模版
     * @param templateName 模版名称 (如 templates/html/index.ftl -> html/index.ftl)
     * @param configuration Configuration实例
     */
    public static Template getTemplate(String templateName, Configuration configuration) {
        Template template = null;
        try {
            template = configuration.getTemplate(templateName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return template;
    }

    /**
     * 执行处理template模版
     * @param objectModel 数据容器
     * @param template 模版对象
     * @return 执行后的byte数组，默认UTF-8字符集
     */
    public static byte[] process(Object objectModel, Template template) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(baos);
        try {
            template.process(objectModel, outputStreamWriter);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(outputStreamWriter);
        }
        return baos.toByteArray();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Map<String, Object> objectModel = new HashMap<>();
        Map<String, Object> forecast = new HashMap<>();
        Map<String, Object> create = new HashMap<>();

        List<Map<String, Object>> weathers = new ArrayList<>();

        objectModel.put("forecast", forecast);
        objectModel.put("weathers", weathers);
        objectModel.put("company_name", "杭州辰青和业科技有限公司");
        objectModel.put("create", create);

        forecast.put("year", 2022);
        forecast.put("month", 12);
        forecast.put("day", 11);

        for (int i = 0; i < 2; i++) {
            Map<String, Object> weather = new HashMap<>();

            weather.put("day", 21 + i);
            weather.put("phenomena", "多云");
            weather.put("maxTp", 11 - i);
            weather.put("minTp", -1 + i);
            weather.put("level", "中雨");

            weathers.add(weather);
        }


        create.put("year", "2022");
        create.put("month", 12);
        create.put("day", 11);


        Configuration configuration = getConfiguration("src/main/resources/templates");
        Template template = getTemplate("weather_forecast_48_hour.ftl", configuration);
        byte[] content = process(objectModel, template);
//        System.out.println(new String(content, StandardCharsets.UTF_8));
        FileOutputStream fos = new FileOutputStream("/Users/zsl0/Desktop/abc.doc");
        IoUtil.copy(new ByteArrayInputStream(content), fos);


    }

}
