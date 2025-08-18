package com.tt._2025.b077.huellaspormexico.modules.users.tasks;

import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class UserEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${front.admin.url}")
    private String adminFrontUrl;

    /**
     * Send verification account email
     *
     * @param user      User Model
     * @param token     Valid token
     */

    public void send_verification_email(User user, String token) {
        try {
            Context context = new Context();
            context.setVariable("username", user.getUsername());
            context.setVariable("token", token);
            context.setVariable("host", adminFrontUrl);
            String html = templateEngine.process("users/send_email_verification.html", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Verifica tu cuenta");
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo electrónico", e);
        }
    }

    /**
     * Send forgot password email
     *
     * @param user  User Model
     * @param token Valid reset token
     */
    public void send_forgot_password_email(User user, String token) {
        try {
            Context context = new Context();
            context.setVariable("username", user.getUsername());
            context.setVariable("token", token);
            context.setVariable("host", adminFrontUrl);
            String html = templateEngine.process("users/send_forgot_password.html", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Recuperar contraseña - Huellas por México");
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo de recuperación", e);
        }
    }

}
