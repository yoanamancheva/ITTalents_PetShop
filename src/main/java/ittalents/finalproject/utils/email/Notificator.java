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
        System.out.println(users);
    }

    public void removeObserver(User user) {
        this.users.remove(user);
    }

    public void sendNews() throws MessagingException {
        for (User user : this.users) {
            mailUtil.sendmail(user.getEmail());
        }
    }
}
