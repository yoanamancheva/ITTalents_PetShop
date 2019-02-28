package ittalents.finalproject.controller;

import ittalents.finalproject.exceptions.BaseException;
import ittalents.finalproject.exceptions.InvalidInputException;
import ittalents.finalproject.exceptions.NotLoggedInException;
import ittalents.finalproject.exceptions.ProductOutOfStockException;
import ittalents.finalproject.model.pojos.User;
import ittalents.finalproject.model.daos.UserDAO;
import ittalents.finalproject.model.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "users")
public class UserController extends BaseController{

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "register")
    public User addUser(@RequestBody User user, HttpSession session) throws BaseException{
        validateUserInput(user);
        userRepository.save(user);
        session.setAttribute(LOGGED_USER, user);
        return user;
    }

    @PostMapping(value = "register/admin")
    public User addAdmin(@RequestBody User user , HttpSession session) throws BaseException{
        validateUserInput(user);
        user.setAdministrator(true);
        session.setAttribute(LOGGED_USER, user);
        userRepository.save(user);
        return user;
    }

    @GetMapping(value = "logout")
    public Object logout(HttpSession session) {
        if(session.getAttribute(LOGGED_USER) != null) {
            session.removeAttribute(LOGGED_USER);
            return "You logged out successfully";
        }
        return "You are not logged in";
    }

    private void validateUserInput( User user) throws BaseException{
        String newUsername = user.getUsername();
        String newEmail = user.getEmail();
        validateNullInput(user);
        if ( getUserByName(newUsername) != null ) {
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


    //to fix null pointer
    @PostMapping(value = "login")
    public Object login(@RequestBody User user, HttpSession session) throws BaseException{
        String pendingUsername = user.getUsername();
        String pendingPassword = user.getPassword();
        if(getUserByName(pendingUsername).getUsername().equals(pendingUsername) &&
            getUserByName(pendingUsername).getPassword().equals(pendingPassword)) {
            session.setAttribute(LOGGED_USER, getUserByName(pendingUsername));
            System.out.println(session.getAttribute(LOGGED_USER));
            return "You logged successfully.";
        }
        throw new InvalidInputException("Wrong username/password");
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

    private User getUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
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


        //TODO validation
    @GetMapping(value = "delete/{username}")
    public void deleteUser (@PathVariable("username") String username, HttpSession session) throws NotLoggedInException {
        try {
            super.validateLogin(session); //ili go nqma v bazata
            UserDAO.getInstance().deleteUser(username);
        } catch (SQLException e) {
            System.out.println("Problem with deleting the user - " + e.getMessage());
        }
    }

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
