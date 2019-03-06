package ittalents.finalproject.controller;

import ittalents.finalproject.exceptions.*;
import ittalents.finalproject.model.pojos.Message;

import ittalents.finalproject.model.pojos.products.Product;
import org.springframework.http.HttpRequest;
import ittalents.finalproject.model.pojos.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.sql.SQLException;
import java.time.LocalDateTime;

//@Transactional(rollbackfor = //nqkakuv exception)
@RestController
public abstract class BaseController {

    public static final String LOGGED_USER = "loggedUser";

    @ExceptionHandler({InvalidInputException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Message invalidInput(Exception e) {
        return new Message(e.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
    }


    @ExceptionHandler({ProductNotFoundException.class, PetNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Message productNotFound(Exception e) {
        return new Message(e.getMessage(), LocalDateTime.now(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({NotLoggedInException.class, NotLoggedAdminException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public Message notLoggedHandler(Exception e){
        return new Message(e.getMessage(), LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler({ProductOutOfStockException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Message productOutOfStockHandler(Exception e) {
        return new Message(e.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({BaseException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Message basedHandler(){
        return new Message(new BaseException("You are trying to input invalid properties. Try again!").getMessage(),
                LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({SQLException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Message sqlHandler(Exception e) {
        return new Message(e.getMessage(), LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

//    @ExceptionHandler({Exception.class})
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public Message allExceptionHandler(){
//        return new Message(new Exception("Sorry, the server is temporary down. ").getMessage(), LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value());
//    }



    protected void validateLogin(HttpSession session) throws NotLoggedInException{
        if(session.getAttribute(LOGGED_USER) == null) {
            throw new NotLoggedInException();
        }
    }

    protected void validateLoginAdmin(HttpSession session)throws BaseException {
        if(session.getAttribute(LOGGED_USER) == null){
            throw new NotLoggedInException();
        }
        else {
            User user = (User)(session.getAttribute(LOGGED_USER));
            if(!user.isAdministrator()) {
                throw new NotLoggedAdminException();
            }
        }
    }

}
