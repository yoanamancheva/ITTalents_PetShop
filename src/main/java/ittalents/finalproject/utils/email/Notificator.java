package ittalents.finalproject.utils.email;

import ittalents.finalproject.controller.BaseController;
import ittalents.finalproject.model.pojos.ErrorMsg;
import ittalents.finalproject.model.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
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

//    public Object sendNews() {
//        new Thread(() -> {
//        for (User user : this.users) {
//            try {
//                mailUtil.sendmail(user.getEmail());
//            } catch (MessagingException e) {
//                return new ErrorMsg("Problem with sending mail." , LocalDateTime.now(),
//                                    HttpStatus.INTERNAL_SERVER_ERROR.value());
//            }
//        }}).start();
//    }
}
