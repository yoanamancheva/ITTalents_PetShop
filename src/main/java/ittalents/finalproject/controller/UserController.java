package ittalents.finalproject.controller;

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

    static Logger log = Logger.getLogger(UserController.class.getName());


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Notificator notificator;

    @Autowired
    private MailUtil mailUtil;

    @PostMapping(value = "register")
    public User addUser(@RequestBody User user, HttpSession session) throws BaseException {

        validateUserInput(user);
        if (user.isNotifications()) {
            notificator.addObserver(user);
        }
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
        return user;

    }

    //todo fix better exception
    @GetMapping(value = "register/confirmed")
    public Message confirmedEmail(HttpSession session){
            User user = (User) session.getAttribute(LOGGED_USER);
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

    @PostMapping(value = "register/admin")
    public User addAdmin(@RequestBody User user , HttpSession session) throws BaseException{

        validateUserInput(user);
        user.setAdministrator(true);
        session.setAttribute(LOGGED_USER, user);
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
        return user;

    }

    @GetMapping(value = "logout")
    public Object logout(HttpSession session) {
        if(session.getAttribute(LOGGED_USER) != null) {
            session.invalidate();
            return new Message("You logged out successfully", LocalDateTime.now(), HttpStatus.OK.value());
        }
        return new Message("You are not logged in", LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
    }

    private void validateUserInput( User user) throws BaseException{
        String newUsername = user.getUsername();
        String newEmail = user.getEmail();
        newEmail = newEmail.trim();
        validateNullInput(user);
        user.setPassword(user.getPassword().trim());
        user.setPassword2(user.getPassword2().trim());
        if (getUserByName(newUsername) != null) {
            throw new InvalidInputException("This username is already used by another user.");
        }
        else if( getUserByEmail(newEmail) != null) {
            throw new InvalidInputException("This email is already user by another user.");
        }
        else if(user.getPassword().length() < 3 ) {
            throw new InvalidInputException("The password should be more than 3 symbols");
        }
        else if (!user.getPassword().equals(user.getPassword2()) ) {
            throw new InvalidInputException("The passwords don't match.");
        }
        else if (!user.getEmail().matches("^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})$")) {
            throw new InvalidInputException("The email is not valid");
        }
    }


    @PostMapping(value = "login")
    public Object login(@RequestBody User user, HttpSession session) throws BaseException{
        String pendingUsername = user.getUsername();
        String pendingPassword = user.getPassword();
        if(pendingPassword == null || pendingUsername == null) {
            throw new InvalidInputException("Username/password should not be empty.");
        }
//        if(session.getAttribute(LOGGED_USER) != null) {
//            throw new InvalidInputException("You are already logged in.");
//        }
        if(getUserByName(pendingUsername) != null ) {
            if (getUserByName(pendingUsername).getUsername().equals(pendingUsername) &&
                    getUserByName(pendingUsername).getPassword().equals(pendingPassword)) {
                session.setAttribute(LOGGED_USER, getUserByName(pendingUsername));
                return new Message("You logged successfully.", LocalDateTime.now(), 200);
            }
        }
        throw new InvalidInputException("Wrong username/password");
    }

    //todo deal with exception better
    @GetMapping(value = "/reset/password")
    public Message forgottenPassword(@RequestParam("id") long id, HttpSession session) throws BaseException {
        if(session.getAttribute(LOGGED_USER) == null) {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                String newPassword = new Random().ints(5, 33, 122)
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

                user.get().setPassword(newPassword);
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

    @PutMapping(value = "profile/delete")
    public Object deleteProfile(@RequestBody User user, HttpSession session)
                                throws BaseException {
        validateLogin(session);
        User userSession = (User)session.getAttribute(LOGGED_USER);
        if(userRepository.findById(userSession.getId()).isPresent()) {
            if(userSession.getPassword().equals(user.getPassword()) && userSession.getEmail().equals(user.getEmail())) {
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

    @PutMapping(value = "profile/update/password")
    public Object updatePassword(@RequestBody ChangePasswordUserDTO pendingUser, HttpSession session) throws BaseException {
        validateLogin(session);
        User user = (User)session.getAttribute(LOGGED_USER);
        if(userRepository.findById(user.getId()).isPresent()) {
            if(user.getPassword().equals(pendingUser.getPassword()) && user.getUsername().equals(pendingUser.getUsername())) {
                if(!user.getPassword().equals(pendingUser.getNewPassword())) {
                    if (pendingUser.getNewPassword().length() >= 3) {
                        user.setPassword(pendingUser.getNewPassword());
                        userRepository.save(user);
                        new Thread(()-> {
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
                    throw new InvalidInputException("New password should be more than 3 symbols.");
                }
                throw new InvalidInputException("New password can not be the same as old password.");
            }
            throw new InvalidInputException("Wrong username/password. Can not change password.");
        }
        throw new InvalidInputException("No user with that username/password.");
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



    //working
//    @PostMapping(value = "/users/register")
//    public void addUser(HttpServletRequest req, HttpServletResponse resp) throws Exception{
//        String username = req.getParameter("username");
//        String password = req.getParameter("password");
//        String password2 = req.getParameter("password2");
//        String first_name = req.getParameter("first_name");
//        String last_name = req.getParameter("last_name");
//        String email = req.getParameter("email");
//
//
//        //TODO validation
//        User user = new User(username, password, first_name, last_name, email, false);
//        UserDAO.getInstance().addUser(user);
//        resp.getWriter().append("Successfully registered.");
//    }


//    todo deleting user, cannot due to being fk i other tables
//    @GetMapping(value = "delete/username/{username}")
//    public Object deleteUser (@PathVariable("username") String username, HttpSession session) throws BaseException, SQLException {
//        super.validateLoginAdmin(session); //ili go nqma v bazata
//        UserDAO.getInstance().deleteUser(username);
//        return new Message("User deleted successfully", LocalDateTime.now(), 200);
//
//    }

    //to fix
//    @PostMapping(value = "/users/login")
//    public Object login(HttpServletRequest req, HttpServletResponse res, HttpSession session) throws SQLException{
//        String username = req.getParameter("username");
//        String password = req.getParameter("password");
//
//        if(!isLogged(session)) {
//            if(UserDAO.getInstance().getUserByUsername(username) != null) {
//                System.out.println(UserDAO.getInstance().getUserByUsername(username));
//
//            }
//            session.setAttribute("username", username);
//            return session.getAttribute("Congrats! You logged in successfully.");
//        }
//        return "opa";
//    }


    @Autowired
    private JdbcTemplate jdbcTemplate;

    //working, not used
//    @GetMapping(value = "/users")
//    public List<User> getAll() {
//        List<User> users = jdbcTemplate.query("SELECT id, username, password, first_name, last_name, email, administrator FROM users", (resultSet, i) -> toUser(resultSet));
//        return users;
//    }


    //working not
//    private User toUser(ResultSet resultSet) throws SQLException{
//        User u = new User(resultSet.getString("username"),
//                        resultSet.getString("password"),
//                        resultSet.getString("first_name"),
//                        resultSet.getString("last_name"),
//                        resultSet.getString("email"),
//                        resultSet.getBoolean("administrator"));
//        u.setId(resultSet.getLong("id"));
//          return u;
//    }


    //working, not used
//    @GetMapping(value = "/userById/{id}")
//    public Object getUserById(@PathVariable("id") long id){
//        String sql = "SELECT * FROM users WHERE id = ?";
//        User user = (User) jdbcTemplate.queryForObject(
//                sql, new Object[] { id },
//                new BeanPropertyRowMapper(User.class));
//
//        return user;
//    }

    //working
//    @GetMapping(value = "/userByUsername/{username}")
//    public User getByUsername(@PathVariable("username") String username){
//        String sql = "SELECT * FROM users WHERE username = ?";
//        User user = (User) jdbcTemplate.queryForObject(
//                sql, new Object[] { username },
//                new BeanPropertyRowMapper(User.class));
//        return user;
//    }
}
