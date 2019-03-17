package ittalents.finalproject.util.mail;

import ittalents.finalproject.controller.BaseController;
import ittalents.finalproject.model.pojos.User;
import ittalents.finalproject.model.repos.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;


@Component
public class Notificator extends BaseController {

    static Logger log = Logger.getLogger(Notificator.class.getName());

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private UserRepository userRepository;

    public void sendNews(String subject, String content) {
        List<User> users = userRepository.findAllByNotifications(true);
        if(!users.isEmpty()) {
            new Thread(() -> {
                for (User user : users) {
                    try {
                        mailUtil.sendmail(user.getEmail(), subject, content);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        log.error(e.getMessage());
                    }
                }
            }).start();
        }
    }

}
