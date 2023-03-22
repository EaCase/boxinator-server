package com.example.boxinator.services.email;

import com.example.boxinator.errors.exceptions.ApplicationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Service
public class Smtp2goService implements EmailService {
    @Value("${mail.auth.username}")
    private String USERNAME;

    @Value("${mail.auth.password}")
    private String PASSWORD;

    @Value("${mail.smtp.auth}")
    private String MAIL_AUTH;

    @Value("${mail.smtp.starttls.enable}")
    private String MAIL_TLS;

    @Value("${mail.smtp.host}")
    private String MAIL_HOST;

    @Value("${mail.smtp.port}")
    private String MAIL_PORT;

    @Value("${mail.smtp.sender}")
    private String MAIL_SENDER;

    @Value("${client.url.registration}")
    private String URL_FINISH_REG_BASE;

    @Override
    public void sendRegisterAccount(String email, String temporaryUserToken) {
        try {
            Message message = new MimeMessage(getSession());
            Multipart mp = new MimeMultipart("alternative");
            BodyPart text = new MimeBodyPart();
            text.setText("Click the following link to finish registering your account: " + URL_FINISH_REG_BASE + "?token=" + temporaryUserToken);
            mp.addBodyPart(text);
            message.setFrom(new InternetAddress(MAIL_SENDER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Register your account.");
            message.setContent(mp);
            Transport.send(message);
        } catch (javax.mail.MessagingException e) {
            throw new RuntimeException("Failed to send mail.");
        }
    }

    @Override
    public void sendOrderConfirmation(String email, Long orderId) {

    }

    private Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", MAIL_AUTH);
        props.put("mail.smtp.starttls.enable", MAIL_TLS);
        props.put("mail.smtp.host", MAIL_HOST);
        props.put("mail.smtp.port", MAIL_PORT);

        return Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });
    }
}
