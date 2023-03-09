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
 * mail:
 * host: smtp.163.com
 * password: xxxx
 * username: xxxxx
 *
 * @author zsl
 * created on 2021/12/29 16:32
 */
public class MailUtil {

    static Logger log = LoggerFactory.getLogger(MailUtil.class);

    /**
     * 发送邮件(使用Spring封装JavaMail)
     *
     * @param from           发送邮箱("zsl0<xxx@xx.com>")
     * @param to             目标邮箱
     * @param subject        标题
     * @param text           邮件内容
     * @param html           内容是否html
     * @param javaMailSender 发送对象
     * @return 成功返回true，反之false
     */
    private boolean sendEmail(String from, String to, String subject, String text, boolean html, JavaMailSender javaMailSender) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, html);
            javaMailSender.send(mimeMessage);
            log.debug("发送邮件成功 to={}", to);
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error("发送邮件失败 to={}", to);
            return false;
        }
        return true;
    }

    /**
     * 发送邮件(使用Spring封装JavaMail)
     *
     * @param from           发送邮箱("zsl0<xxx@xx.com>")
     * @param to             目标邮箱
     * @param subject        标题
     * @param text           邮件内容
     * @param html           内容是否html
     * @param attachments    附件列表
     * @param javaMailSender 发送对象
     * @return 成功返回true，反之false
     */
    private boolean sendEmail(String from, String to, String subject, String text, boolean html, Map<String, FileSystemResource> attachments, JavaMailSender javaMailSender) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
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
            log.debug("发送邮件成功 to={}", to);
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error("发送邮件失败 to={}", to);
            return false;
        }
        return true;
    }

}
