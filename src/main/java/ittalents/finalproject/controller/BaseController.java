package ittalents.finalproject.controller;

import ittalents.finalproject.exceptions.NotLoggedInException;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public abstract class BaseController {





    protected void validateLogin(HttpSession session) throws NotLoggedInException{
        if(session.getAttribute("username") == null) {
            throw new NotLoggedInException();
        }
    }

    protected void validateAdmin(HttpSession session) {

    }
    //TODO validates for product and pet
}
