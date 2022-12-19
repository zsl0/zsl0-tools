package com.zsl0.util.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * #    邮箱配置
 * spring:
 *   mail:
 *     host: smtp.163.com
 *     password: xxxx
 *     username: xxxxx
 *
 * @author zsl
 * created on 2021/12/29 16:32
 */
public class MailUtil {

    static Logger log = LoggerFactory.getLogger(MailUtil.class);

//    @Autowired
//    JavaMailSender javaMailSender;
//
//    @Value("${spring.mail.username}")
//    String from;
//
//    @Value("${spring.mail.codeExpire:3}")
//    String codeExpire;
//
//    @Autowired
//    CodeServer codeServer;
//
//    @Value("${admin.mail}")
//    String adminMail;


    /**
     * 发送邮件(使用Spring封装JavaMail)
     * @param from 发送邮箱("zsl0<xxx@xx.com>")
     * @param to 目标邮箱
     * @param subject 标题
     * @param text 邮件内容
     * @param html 内容是否html
     * @param javaMailSender 发送对象
     */
    private void sendEmail(String from, String to, String subject, String text, boolean html, JavaMailSender javaMailSender) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, html);
            javaMailSender.send(mimeMessage);
            log.info("发送邮件成功 to={}", to);
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error("发送邮件失败 to={}", to);
        }
    }

    /**
     * 发送邮件(使用Spring封装JavaMail)
     * @param from 发送邮箱("zsl0<xxx@xx.com>")
     * @param to 目标邮箱
     * @param subject 标题
     * @param text 邮件内容
     * @param html 内容是否html
     * @param attachments 附件列表
     * @param javaMailSender 发送对象
     */
    private void sendEmail(String from, String to, String subject, String text, boolean html, Map<String, FileSystemResource> attachments, JavaMailSender javaMailSender) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, html);

            // 填入附件
            for (Map.Entry<String, FileSystemResource> entry : attachments.entrySet()) {
                String key = entry.getKey();
                FileSystemResource value = entry.getValue();
                mimeMessageHelper.addAttachment(key, value);
            }

            javaMailSender.send(mimeMessage);
            log.info("发送邮件成功 to={}", to);
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error("发送邮件失败 to={}", to);
        }
    }

//    /**
//     * 发送邮箱验证码
//     */
//    public void sendEmailCode(User user, int codeLength) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // 获取 freemarker 模板
//                String text = null;
//                try {
//                    // 获取模板
//                    Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
//                    configuration.setDirectoryForTemplateLoading(new File("E:\\JavaCode\\login\\src\\main\\resources\\ftl"));
//                    configuration.setDefaultEncoding("UTF-8");
//                    Template template = configuration.getTemplate("emailCode.ftl");
//                    // 设置模板变量
//                    String code = CodeUtils.getCode(codeLength);
//                    Map<String, String> data = new HashMap<>();
//                    data.put("name", user.getUserAccount());
//                    data.put("code", code);
//                    data.put("codeExpire", codeExpire);
//                    // 保存验证码
//                    codeServer.set(user.getUserEmail(), code);
//                    // 获取模板字符串
//                    StringWriter out = new StringWriter();
//                    template.process(data, out);
//                    text = out.toString();
//                    out.close();
//                    // 发送邮件
//                    String subject = "请注意查收验证码";
//                    sendEmail(user.getUserEmail(), subject, text);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    log.error("获取邮箱验证码 freemarker 失败");
//                }
//            }
//        }).start();
//    }
//
//    /**
//     * 预警邮件
//     */
//    public void sendWarnMail(String msg) {
//        log.info("发送预警邮件 date = {}", LocalDateTime.now().toString());
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                sendEmail(adminMail, "Warning 预警", msg);
//            }
//        }).start();
//    }
}
