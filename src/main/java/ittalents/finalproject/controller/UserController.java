package ittalents.finalproject.controller;

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
    public User addUser(@RequestBody User user, HttpSession session) throws BaseException {
        return userService.registerUser(user, session);
    }

    @GetMapping(value = "register/confirmed")
    public Message confirmedEmail(HttpSession session){
        User user = (User) session.getAttribute(LOGGED_USER);
        return userService.confirmEmail(user);
    }

    @PostMapping(value = "register/admin")
    public User addAdmin(@RequestBody User user , HttpSession session) throws BaseException{
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





    @GetMapping(value = "/crypt")
    public String getCrypt() {
        String password = "1234";
        String cryptedPass = BCrypt.hashpw(password, BCrypt.gensalt());
        return cryptedPass;
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


//    @Autowired
//    private JdbcTemplate jdbcTemplate;

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
