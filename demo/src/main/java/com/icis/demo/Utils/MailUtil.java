package com.icis.demo.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {
    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendInternshipApplicationMail(String to, String studentName, String companyName, String offerName){
        String subject = "Internship Application";
        String body = "Dear " + companyName + ",\n\n" +
                "A new application has been submitted by " + studentName + " for the " + offerName + " internship.\n\n" +
                "Please log in to the portal to view the application.\n\n" +
                "Best regards,\n" +
                "Internship Portal";
        sendMail(to, subject, body);
    }
}
