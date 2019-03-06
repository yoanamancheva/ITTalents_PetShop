package ittalents.finalproject.utils.email;

import ittalents.finalproject.controller.BaseController;
import ittalents.finalproject.model.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;


@Component
public class Notificator extends BaseController {
    @Autowired
    private MailUtil mailUtil;

    private List<User> users = new ArrayList<>();


    public void addObserver(User user) {
        this.users.add(user);
    }

    public void removeObserver(User user) {
        this.users.remove(user);
    }

    //todo - catch exception
    public void sendNews(String subject, String content) {
        new Thread(() -> {
        for (User user : this.users) {
            new Thread(() -> {
                try {
                    mailUtil.sendmail(user.getEmail(), subject, content);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }).start();

        }}).start();
    }
}
