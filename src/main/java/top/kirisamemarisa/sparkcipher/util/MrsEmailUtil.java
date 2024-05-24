package top.kirisamemarisa.sparkcipher.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Marisa
 * @Description 邮件工具类
 * @Date 2024/5/23
 */
@Service
public class MrsEmailUtil {
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private TemplateEngine templateEngine;
    @Value("${spring.mail.sender}")
    private String FROM_MAIL;

    /**
     * 简单文本邮件
     *
     * @param target  收件人
     * @param subject 主题
     * @param content 内容
     */
    public void sendSimpleMail(String target, String subject, String content) {
        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送人
        message.setFrom(FROM_MAIL);
        //邮件接收人
        message.setTo(target);
        //邮件主题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);
        //发送邮件
        mailSender.send(message);
    }


    public void sendCodeEmail(String target, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        sendHTMLEmail(map, target, "登录验证码", "login-template");
    }

    /**
     * 根据模板发送邮件
     *
     * @param ctx      邮件内容
     * @param target   收件人邮箱
     * @param subject  邮件主题（标题）
     * @param template 模板路径（不带后缀名）
     */
    public void sendHTMLEmail(Map<String, ?> ctx, String target, String subject, String template) {
        Context context = new Context();
        for (String key : ctx.keySet()) {
            context.setVariable(key, ctx.get(key));
        }
        // 使用Thymeleaf模板引擎处理模板
        String htmlContent = templateEngine.process(template, context);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(FROM_MAIL);
            helper.setTo(target);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // 第二个参数为true表示启用HTML格式
            javaMailSender.send(message);
        } catch (MessagingException e) {
            System.out.println("出错啦: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 带附件的邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件
     */
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(FROM_MAIL);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
            mailSender.send(message);
            System.out.println("发送成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}