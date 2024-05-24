package com.icis.demo.Utils;

import com.icis.demo.Entity.Company;
import com.icis.demo.Entity.Student;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;


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

    public void sendOfferAcceptedMailToTheCompany(String to, String companyName, String offerName){
        String subject = "Internship Offer Accepted";
        String body = "Dear " + companyName + ",\n\n" +
                "Your offer " + offerName + " has been accepted.\n\n" +
                "Please log in to the website to view the application.\n\n" +
                "Best regards,\n" +
                "IZTECH Ceng Internship System";
        sendMail(to, subject, body);
    }

    public void sendOfferRejectedMailToTheCompany(String to, String companyName, String offerName){
        String subject = "Internship Offer Rejected";
        String body = "Dear " + companyName + ",\n\n" +
                "Your offer " + offerName + " has been rejected.\n\n" +
                "Please log in to the website to view the application.\n\n" +
                "Best regards,\n" +
                "IZTECH Ceng Internship System";
        sendMail(to, subject, body);
    }

    public void sendMessageWithAttachment(String to, Student student, Company company, String pathToAttachment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Internship Application:" + student.getName() + " " + student.getSurname());  // Replace [Student Full Name] with the actual name
            helper.setText( "Dear " + company.getCompanyName() + ",\n\n" +
                    "A new application has been submitted by " + student.getName() + " " + student.getSurname() + " internship.\n\n" +
                    "Please log in to the our website to view the application.\n\n" +
                    "Best regards,\n" +
                    "IZTECH Ceng Internship System");

            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);

            mailSender.send(message);
        } catch (Exception e) {
            //:)
        }
    }
}
