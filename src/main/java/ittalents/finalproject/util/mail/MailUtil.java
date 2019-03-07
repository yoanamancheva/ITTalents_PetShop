package ittalents.finalproject.util.mail;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

@Component
public class MailUtil {

    private static final String EMAIL_SERVER = "petshop.noreply@gmail.com";
    private static final String PASSWORD = "petshop12345";

    public static final String VERIFY_EMAIL_CONTENT = "Please, follow the link and verify your email address." +
                                                        "\n localhost:8080/users/register/confirmed";
    public static final String VERIFY_EMAIL_SUBJECT = "Verifying email.";

    public static final String VERIFY_EMAIL_CONTENT_ADMIN = "You registered as admin. " +
                                                            " Please, follow the link and verify your email address." +
                                                             "\n localhost:8080/users/register/confirmed";
    public static final String VERIFY_EMAIL_SUBJECT_ADMIN = "Verifying email - admin.";


    //todo add link to promotions dto
    public static final String NEW_PROMOTIONS_PRODUCTS_CONTENT = "Hey, we have great new product promotions. Check them here.";
    public static final String NEW_PROMOTIONS_SUBJECT = "New product promotions.";

    //todo add link to pets promotions dto
    public static final String NEW_PROMOTIONS_PETS_CONTENT = "Hey, we have great new pet promotions. Check them here.";
    public static final String NEW_PROMOTIONS_PETS_SUBJECT = "New pet promotions.";

    public static final String SUCCESSFUL_REGISTRATION_CONTENT = "You successfully confirm your email address." +
                                                                 " Enjoy shopping.";
    public static final String SUCCESSFUL_REGISTRATION_SUBJECT = "Confirmed email address.";

    public static final String SUCCESSFUL_NEW_PASSWORD_CONTENT = "You successfully changed your password." +
            " Enjoy shopping.";
    public static final String SUCCESSFUL_NEW_PASSWORD_SUBJECT = "New password.";



    public void sendmail(String to, String subject, String content) throws MessagingException {
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
        msg.setSubject(subject);
        msg.setContent(content, "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(content, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }
}


