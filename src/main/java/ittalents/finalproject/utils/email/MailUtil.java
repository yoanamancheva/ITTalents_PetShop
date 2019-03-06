package ittalents.finalproject.utils.email;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

@RestController
public class MailUtil {

    private static final String EMAIL_SERVER = "petshop.noreply@gmail.com";
    private static final String PASSWORD = "petshop12345";
    public static final String SUBJECT = "New promotions";
    public static final String CONTENT = "New promotions.";


    public void sendmail(String to) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_SERVER, PASSWORD);
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(EMAIL_SERVER, false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        msg.setSubject(SUBJECT);
        msg.setContent(CONTENT, "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(CONTENT, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }
}
