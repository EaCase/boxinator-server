package com.example.boxinator.services.email;

import com.example.boxinator.errors.exceptions.ApplicationException;
import com.example.boxinator.repositories.shipment.ShipmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

    private final ShipmentRepository shipmentRepository;

    public Smtp2goService(
            ShipmentRepository shipmentRepository
    ) {
        this.shipmentRepository = shipmentRepository;
    }

    @Override
    public void sendAccountRegistration(String email, String temporaryUserToken) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(MAIL_SENDER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Register your account.");

            Multipart mp = new MimeMultipart("alternative");
            BodyPart text = new MimeBodyPart();
            text.setText("Click the following link to finish registering your account: " + URL_FINISH_REG_BASE + "?token=" + temporaryUserToken);
            mp.addBodyPart(text);

            message.setContent(mp);

            Transport.send(message);
        } catch (javax.mail.MessagingException e) {
            System.err.println("Failed to send the registration email: " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void sendOrderConfirmation(String email, Long shipmentId) {

        var shipment = shipmentRepository.findById(shipmentId).orElseThrow(() -> new ApplicationException("No shipment with that id", HttpStatus.BAD_REQUEST));

        try {
            Message message = new MimeMessage(getSession());
            Multipart mp = new MimeMultipart("alternative");
            BodyPart htmlmessage = new MimeBodyPart();
            htmlmessage.setContent(
                    "<h1>Hello! Thank you for placing your shipment</h1>" + "<br>" +
                            "<p><b>Order successfully placed! with order id: </b></p>" + shipmentId + "<br>" +
                            "<p><b>Shipment placed for item: </b></p>" + shipment.getBoxTier().getName() + " mystery box" + "<br>" +
                            "<p><b>Box colour: </b></p>" + shipment.getBoxColor() + "<br>" +
                            "<p><b>Box weight: <b></p>" + shipment.getBoxTier().getWeight() + "kg" + "<br>" +
                            "<p><b>Box being shipped to: </b><p>" + shipment.getCountry().getName() + "<br>" +
                            "<p><b>Total cost of order: </b></p>" + shipment.getCost() + "&#8364;"
                    , "text/html");
            mp.addBodyPart(htmlmessage);
            message.setFrom(new InternetAddress(MAIL_SENDER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Order confirmation");
            message.setContent(mp);
            Transport.send(message);

        } catch (javax.mail.MessagingException e) {
            System.err.println("Failed to send confirmation email: " + e);
            e.printStackTrace();
        }
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
