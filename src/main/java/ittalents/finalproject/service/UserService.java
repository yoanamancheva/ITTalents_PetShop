package ittalents.finalproject.service;

import ittalents.finalproject.controller.UserController;
import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.User;
import ittalents.finalproject.model.pojos.dto.ChangePasswordUserDTO;
import ittalents.finalproject.model.pojos.dto.UserDTO;
import ittalents.finalproject.model.repos.UserRepository;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.InvalidInputException;
import ittalents.finalproject.util.exceptions.UserNotFoundException;
import ittalents.finalproject.util.mail.MailUtil;
import ittalents.finalproject.util.mail.Notificator;
import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static ittalents.finalproject.controller.BaseController.LOGGED_USER;

@Service
public class UserService {

    public static final int MIN_SYMBOLS = 7;
    @Autowired
    private Notificator notificator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailUtil mailUtil;

    static Logger log = Logger.getLogger(UserController.class.getName());


//    register user
    public UserDTO registerUser(User user, HttpSession session) throws BaseException {

        validateUserInput(user);
        if (user.isNotifications()) {
            notificator.addObserver(user);
        }
        String cryptedPass = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(cryptedPass);
        user.setPassword2(cryptedPass);

        userRepository.save(user);
        new Thread(() -> {
            try {
                mailUtil.sendmail(user.getEmail(), MailUtil.VERIFY_EMAIL_SUBJECT, MailUtil.VERIFY_EMAIL_CONTENT);
            } catch (MessagingException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }).start();
        session.setAttribute(LOGGED_USER, user);
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
                                        user.getEmail(), user.isAdministrator(), user.isNotifications(), user.isVerified());
        return userDTO;
    }

//    register admin
    public UserDTO registerAdmin(User user, HttpSession session) throws BaseException{
        validateUserInput(user);
        user.setAdministrator(true);
        session.setAttribute(LOGGED_USER, user);
        String cryptedPass = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(cryptedPass);
        user.setPassword2(cryptedPass);
        userRepository.save(user);
        new Thread(() -> {
            try {
                mailUtil.sendmail(user.getEmail(), MailUtil.VERIFY_EMAIL_SUBJECT_ADMIN,
                        MailUtil.VERIFY_EMAIL_CONTENT_ADMIN);
            } catch (MessagingException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }).start();
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.isAdministrator(), user.isNotifications(), user.isVerified());
        return userDTO;
    }

//    confirm email
    public Message confirmEmail(User user) {
        user.setVerified(true);
        userRepository.save(user);
        new Thread(() -> {
            try {
                mailUtil.sendmail(user.getEmail(), MailUtil.SUCCESSFUL_REGISTRATION_SUBJECT,
                        MailUtil.SUCCESSFUL_REGISTRATION_CONTENT);
            } catch (MessagingException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }).start();

        return new Message("You successfully confirmed your email address. Enjoy shopping!",
                LocalDateTime.now(), HttpStatus.OK.value());
    }

//    logout user
    public Message logoutUser(HttpSession session) {
        if(session.getAttribute(LOGGED_USER) != null) {
            session.invalidate();
            return new Message("You logged out successfully", LocalDateTime.now(), HttpStatus.OK.value());
        }
        return new Message("You are not logged in", LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
    }

//    login user
    public Object loginUser(User user, HttpSession session) throws BaseException {
        String pendingUsername = user.getUsername();
        String pendingPassword = user.getPassword();
        if(pendingPassword == null || pendingUsername == null) {
            throw new InvalidInputException("Username/password should not be empty.");
        }
//        if(session.getAttribute(LOGGED_USER) != null) {
//            throw new InvalidInputException("You are already logged in.");
//        }
        pendingUsername = pendingUsername.trim();
        pendingPassword = pendingPassword.trim();

        User dbUser = getUserByName(pendingUsername);
        String dbPass = dbUser.getPassword();

        if(getUserByName(pendingUsername) != null ) {
            if (dbUser.getUsername().equals(pendingUsername) &&
                    BCrypt.checkpw(pendingPassword, dbPass)) {
                session.setAttribute(LOGGED_USER, dbUser);
                return new Message("You logged successfully.", LocalDateTime.now(), 200);
            }
        }
        throw new InvalidInputException("Wrong username/password");
    }


    public Message forgotPassword(long id, HttpSession session) throws BaseException {
        if(session.getAttribute(LOGGED_USER) == null) {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                String newPassword = new Random().ints(8, 33, 122)
                        .collect(StringBuilder::new,
                                StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
                new Thread(() -> {
                    try {
                        mailUtil.sendmail(user.get().getEmail(), "New password",
                                "Hello, this is your new password. \""
                                        + newPassword + "\"  You can change it anytime after you log in.");
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        log.error(e.getMessage());
                    }
                }).start();

                String cryptedPass = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                user.get().setPassword(cryptedPass);
                userRepository.save(user.get());
                return new Message("Your new password is " + newPassword, LocalDateTime.now(), HttpStatus.OK.value());
            } else {
                throw new InvalidInputException("No user found with that id.");
            }
        }
        else {
            throw new InvalidInputException("You can not reset password while you are logged in.");
        }
    }


//    sets all user's data to 'deleted'
    public Message userDeleteProfile(User userSession, User user) throws BaseException{

        if(userRepository.findById(userSession.getId()).isPresent()) {
            if(BCrypt.checkpw(user.getPassword(), userSession.getPassword()) && userSession.getEmail().equals(user.getEmail())) {
                userSession.setUsername("deleted");
                userSession.setFirstName("deleted");
                userSession.setLastName("deleted");
                userSession.setPassword("deleted");
                userSession.setPassword2("deleted");
                userSession.setEmail("deleted");
                userSession.setNotifications(false);
                userRepository.save(userSession);
                return new Message("Profile deleted successfully.", LocalDateTime.now(), HttpStatus.OK.value());
            }
        }
        throw new InvalidInputException("Wrong email/password. Can not delete profile.");
    }


//    change user's password, only if it is logged in
    public Message updatePassword(ChangePasswordUserDTO pendingUser, User user) throws BaseException{
        if(userRepository.findById(user.getId()).isPresent()) {

            if(BCrypt.checkpw(pendingUser.getPassword(), user.getPassword())
                    && user.getUsername().equals(pendingUser.getUsername())) {
                if(!BCrypt.checkpw(pendingUser.getNewPassword(), user.getPassword())) {
                    if(pendingUser.getNewPassword() != null) {
                        if (pendingUser.getNewPassword().length() >= MIN_SYMBOLS) {
                            String cryptedPass = BCrypt.hashpw(pendingUser.getNewPassword(), BCrypt.gensalt());
                            user.setPassword(cryptedPass);
                            userRepository.save(user);
                            new Thread(() -> {
                                try {
                                    mailUtil.sendmail(user.getEmail(), MailUtil.SUCCESSFUL_NEW_PASSWORD_SUBJECT,
                                            MailUtil.SUCCESSFUL_NEW_PASSWORD_CONTENT +
                                                    " Your new password is \"" + pendingUser.getNewPassword());
                                } catch (MessagingException e) {
                                    e.printStackTrace();
                                    log.error(e.getMessage());
                                }
                            }).start();
                            return new Message("You successfully changed your password.", LocalDateTime.now(),
                                    HttpStatus.OK.value());

                        }
                        throw new InvalidInputException("New password should be more than " + MIN_SYMBOLS + " symbols.");
                    }
                    throw new InvalidInputException("New password can not be empty.");
                }
                throw new InvalidInputException("New password can not be the same as old password.");
            }
            throw new InvalidInputException("Wrong username/password. Can not change password.");
        }
        throw new InvalidInputException("No user with that username/password.");
    }


    public Message unsubscribeFromNotifications(User user) throws BaseException{
        if(user.isNotifications()) {
            notificator.removeObserver(user);
            user.setNotifications(false);
            userRepository.save(user);
            return new Message("You successfully unsubscribed.", LocalDateTime.now(), HttpStatus.OK.value());
        }
        else {
            throw new UserNotFoundException("You are not subscribed.");
        }
    }
//    validations ------------------------------------------------------------------------------------------------------
    private void validateUserInput( User user) throws BaseException {
        validateNullInput(user);
        String newUsername = user.getUsername();
        String newEmail = user.getEmail();
        newEmail = newEmail.trim();
        user.setPassword(user.getPassword().trim());
        user.setPassword2(user.getPassword2().trim());
        if (getUserByName(newUsername) != null) {
            throw new InvalidInputException("This username is already used by another user.");
        }
        else if( getUserByEmail(newEmail) != null) {
            throw new InvalidInputException("This email is already user by another user.");
        }
        else if(user.getPassword().length() < MIN_SYMBOLS) {
            throw new InvalidInputException("The password should be more than " + MIN_SYMBOLS + " symbols");
        }
        else if (!user.getPassword().equals(user.getPassword2()) ) {
            throw new InvalidInputException("The passwords don't match.");
        }
        else if (!user.getEmail().matches("^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})$")) {
            throw new InvalidInputException("The email is not valid");
        }
    }

    private void validateNullInput(User user) throws BaseException {
        if(user.getUsername() == null) {
            throw new InvalidInputException("The username should not be empty");
        }
        if(user.getFirstName() == null) {
            throw new InvalidInputException("The first name should not be empty");
        }
        if(user.getLastName() == null) {
            throw new InvalidInputException("The last name should not be empty");
        }
        if(user.getPassword() == null || user.getPassword2() == null) {
            throw new InvalidInputException("Password can not be empty.");
        }
        if(user.getEmail() == null) {
            throw new InvalidInputException("Email can not be empty.");
        }
    }

    private User getUserByName(String username) throws BaseException{
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()) {
            return user.get();
        }
        return null;
    }

    private User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            return user.get();
        }
        return null;
    }
}
