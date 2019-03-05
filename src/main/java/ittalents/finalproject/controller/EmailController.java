package ittalents.finalproject.controller;

import ittalents.finalproject.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
public class EmailController {
    @Autowired
    private EmailService emailService;
    @RequestMapping(value = "/sendemail")
    public String sendEmail() {
        try {
            emailService.sendmail();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("loshu -----------");
            e.printStackTrace();
        }
        return "Email sent successfully.";
    }
}
