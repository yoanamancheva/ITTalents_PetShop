package ittalents.finalproject.controller;

import ittalents.finalproject.model.pojos.dto.UserDTO;
import ittalents.finalproject.service.UserService;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.InvalidInputException;
import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.User;
import ittalents.finalproject.model.pojos.dto.ChangePasswordUserDTO;
import ittalents.finalproject.model.repos.UserRepository;
import ittalents.finalproject.util.exceptions.UserNotFoundException;
import ittalents.finalproject.util.mail.MailUtil;
import ittalents.finalproject.util.mail.Notificator;
import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;


@RestController
@RequestMapping(value = "users", produces = "application/json")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @PostMapping(value = "register")
    public UserDTO addUser(@RequestBody User user, HttpSession session) throws BaseException {
        return userService.registerUser(user, session);
    }

    @GetMapping(value = "register/confirmed")
    public Message confirmedEmail(HttpSession session) throws BaseException{
        validateLogin(session);
        User user = (User) session.getAttribute(LOGGED_USER);
        return userService.confirmEmail(user);
    }

    @PostMapping(value = "register/admin")
    public UserDTO addAdmin(@RequestBody User user , HttpSession session) throws BaseException{
        return userService.registerAdmin(user, session);
    }

    @GetMapping(value = "logout")
    public Object logout(HttpSession session) {
        return userService.logoutUser(session);
    }

    @PostMapping(value = "login")
    public Object login(@RequestBody User user, HttpSession session) throws BaseException{
        return userService.loginUser(user, session);
    }

    @GetMapping(value = "/reset/password")
    public Message forgottenPassword(@RequestParam("id") long id, HttpSession session) throws BaseException {
        return userService.forgotPassword(id, session);
    }

    @PutMapping(value = "profile/delete")
    public Object deleteProfile(@RequestBody User user, HttpSession session) throws BaseException {
        validateLogin(session);
        User userSession = (User)session.getAttribute(LOGGED_USER);
        return userService.userDeleteProfile(userSession, user);
    }

    @PutMapping(value = "profile/update/password")
    public Object updatePassword(@RequestBody ChangePasswordUserDTO pendingUser, HttpSession session) throws BaseException {
        validateLogin(session);
        User user = (User)session.getAttribute(LOGGED_USER);
        return userService.updatePassword(pendingUser,user);
    }


    @GetMapping(value = "profile/unsubscribe")
    public Message unsubscribe(HttpSession session) throws BaseException{
        validateLogin(session);
        User user = (User)session.getAttribute(LOGGED_USER);
        return userService.unsubscribeFromNotifications(user);
    }



    @GetMapping(value = "/crypt")
    public String getCrypt() {
        String password = "1234";
        String cryptedPass = BCrypt.hashpw(password, BCrypt.gensalt());
        return cryptedPass;
    }

}
