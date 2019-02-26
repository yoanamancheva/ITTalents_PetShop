package ittalents.finalproject.controller;

import ittalents.finalproject.model.User;
import ittalents.finalproject.model.daos.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController extends BaseController{

    @PostMapping(value = "/users/register")
    public void addUser(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String password2 = req.getParameter("password2");
        String first_name = req.getParameter("first_name");
        String last_name = req.getParameter("last_name");
        String email = req.getParameter("email");





        //TODO validation
        User user = new User(username, password, first_name, last_name, email, false);
        UserDAO.getInstance().addUser(user);
        resp.getWriter().append("Successfully registered.");

        }


        //TODO validation
    @GetMapping(value = "/users/delete/{username}")
    public void deleteUser (@PathVariable("username") String username) {

        try {
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

    public static boolean isLogged(HttpSession session) {
        if(!session.isNew() && session.getAttribute("username") != null){
            return true;
        }
        return false;
    }


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping(value = "/users")
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT id, username, password, first_name, last_name, email, administrator FROM users", (resultSet, i) -> toUser(resultSet));
        return users;
    }


    private User toUser(ResultSet resultSet) throws SQLException{
        User u = new User(resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getBoolean("administrator"));
        u.setId(resultSet.getLong("id"));
          return u;
    }


    @GetMapping(value = "/userById/{id}")
    public Object findByCustomerId2(@PathVariable("id") long id){
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = (User) jdbcTemplate.queryForObject(
                sql, new Object[] { id },
                new BeanPropertyRowMapper(User.class));

        return user;
    }

    @GetMapping(value = "/userByUsername/{username}")
    public User getByUsername(@PathVariable("username") String username){
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = (User) jdbcTemplate.queryForObject(
                sql, new Object[] { username },
                new BeanPropertyRowMapper(User.class));
        return user;
    }
}
