package ittalents.finalproject.controller;

import ittalents.finalproject.model.pojos.Message;

import ittalents.finalproject.util.exceptions.*;
import org.apache.log4j.Logger;
import ittalents.finalproject.model.pojos.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@RestController
public abstract class BaseController {

    static Logger log = Logger.getLogger(ProductController.class.getName());

    public static final String LOGGED_USER = "loggedUser";

    @ExceptionHandler({InvalidInputException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Message invalidInput(Exception e) {
        log.error(e.getMessage());
        return new Message(e.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
    }


    @ExceptionHandler({ProductNotFoundException.class, PetNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Message productNotFoundHandler(Exception e) {
        log.error(e.getMessage());
        return new Message(e.getMessage(), LocalDateTime.now(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Message userNotFoundHandler(Exception e) {
        log.error(e.getMessage());
        return new Message(e.getMessage(), LocalDateTime.now(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({NotLoggedInException.class, NotLoggedAdminException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public Message notLoggedHandler(Exception e){
        log.error(e.getMessage());
        return new Message(e.getMessage(), LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler({ProductOutOfStockException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Message productOutOfStockHandler(Exception e) {
        log.error(e.getMessage());
        return new Message(e.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({MessagingException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Message emailHandler(Exception e) {
        log.error(e.getMessage());
        return new Message("Problem with sending the email. Please try again later.", LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler({BaseException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Message basedHandler(Exception e){
        log.error(e.getMessage());
        return new Message(new BaseException("You are trying to input invalid properties. Try again!").getMessage(),
                LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({SQLException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Message sqlHandler(Exception e) {
        log.error(e.getMessage());
        return new Message(e.getMessage(), LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler({IOException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Message ioHandler(Exception e) {
        log.error(e.getMessage());
        return new Message(e.getMessage(), LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Message missingRequestParamHandler(Exception e) {
        log.error(e.getMessage());
        return new Message(e.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Message allExceptionHandler(Exception e){
        log.error(e.getMessage());
        return new Message(new Exception("Sorry, the server is temporary down. ").getMessage(), LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }



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
