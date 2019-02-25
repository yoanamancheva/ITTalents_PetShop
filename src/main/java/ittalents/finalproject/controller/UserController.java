package ittalents.finalproject.controller;

import ittalents.finalproject.model.User;
import ittalents.finalproject.model.daos.UserDAO;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@RestController
public class UserController {

    @PostMapping(value = "/users/register")
    public void addUser(HttpServletRequest req, HttpServletResponse resp) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String password2 = req.getParameter("password2");
        String first_name = req.getParameter("first_name");
        String last_name = req.getParameter("last_name");
        String email = req.getParameter("email");


        //TODO validation
        User user = new User(username, password, first_name, last_name, email, false);
        try {

            UserDAO.getInstance().addUser(user);

            resp.getWriter().append("Successfully registered.");
        } catch (Exception e) {
            try {
                resp.getWriter().append("Problem with registering the user.");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

//    @GetMapping(value = "/users/delete/{username}")
//    public void deleteUser (@PathVariable("username") String username) {
//
//        try {
//            UserDAO.getInstance().deleteUser(username);
//        } catch (SQLException e) {
//            System.out.println("Problem with deleting the user - " + e.getMessage());
//        }
//    }
}
