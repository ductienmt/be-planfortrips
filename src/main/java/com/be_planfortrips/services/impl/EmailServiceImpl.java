package com.be_planfortrips.services.impl;

import com.be_planfortrips.repositories.UserRepository;
import com.be_planfortrips.services.interfaces.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendOTPEmail(String toEmail, String otp, String content) {
        System.out.println("otp: " + otp);
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("Plan for Trips <planfortrips.296@gmail.com>");
            helper.setTo(toEmail);
            helper.setSubject("Mã OTP của bạn để thực hiện " + content);

            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<div style='max-width: 400px; margin: 0 auto; font-family: Arial, sans-serif; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9; text-align: center;'>")
                    .append("<h2 style='color: #333;'>Mã OTP của bạn</h2>")
                    .append("<p style='color: #555; font-size: 14px;'>Để tiếp tục, vui lòng sử dụng mã OTP sau. Mã này sẽ hết hạn sau 5 phút.</p>")
                    .append("<div style='display: flex; justify-content: space-between; gap: 10px; margin-top: 20px;'>");

            for (char digit : otp.toCharArray()) {
                htmlContent.append("<div style='width: 40px; height: 40px; line-height: 40px; border: 1px solid #ddd; border-radius: 4px; font-size: 20px; font-weight: bold; background-color: #ffffff;'>")
                        .append(digit)
                        .append("</div>");
            }

            htmlContent.append("</div></div>");

            helper.setText(htmlContent.toString(), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email", e);
        }
    }

    public void sendEmail(String toEmail, String password, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("Plan for Trips <planfortrips.296@gmail.com>");
            helper.setTo(toEmail);
            helper.setSubject(content);

            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<div style='max-width: 400px; margin: 0 auto; font-family: Arial, sans-serif; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9; text-align: center;'>")
                    .append("<h2 style='color: #333;'>" +
                            "Mật khẩu mới của bạn</h2>")
                    .append("<p style='color: #555; font-size: 14px;'>Để tiếp tục sử dụng tài khoản, vui lòng sử dụng mật khẩu sau.</p>")
                    .append("<div style='display: flex; justify-content: space-between; gap: 10px; margin-top: 20px;'>");

            for (char digit : password.toCharArray()) {
                htmlContent.append("<div style='font-size: 20px; font-weight: bold;'>")
                        .append(digit)
                        .append("</div>");
            }

            htmlContent.append("</div></div>");

            helper.setText(htmlContent.toString(), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email", e);
        }
    }
}