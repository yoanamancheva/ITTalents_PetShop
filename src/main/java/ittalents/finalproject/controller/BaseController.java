package ittalents.finalproject.controller;

import ittalents.finalproject.exceptions.*;
import ittalents.finalproject.model.pojos.ErrorMsg;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RestController
public abstract class BaseController {

    @ExceptionHandler({NotLoggedInException.class, NotLoggedAdminException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMsg notLoggedHandler(Exception e){
        return new ErrorMsg(e.getMessage(), LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler({BaseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMsg basedHandler(){
        return new ErrorMsg(new BaseException("You are trying to input invalid properties. Try again!").getMessage(),
                LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
    }

//    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMsg allExceptionHandler(Exception e){
        return new ErrorMsg(e.getMessage(), LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    protected void validateLogin(HttpSession session) throws NotLoggedInException{
        if(session.getAttribute("username") == null) {
            throw new NotLoggedInException();
        }
    }

    protected void validateAdmin(HttpSession session)throws NotLoggedAdminException {
        if(session.getAttribute("username") == null){
            throw new NotLoggedAdminException();
        }
    }

    protected void validateProductInput(String name, String category, double price, int quantity, String manifacturer, String description,
                                String photo)throws InvalidInputException {
        if(name == null || category == null || price < 0 || quantity < 0 || manifacturer == null || description == null || photo == null){
            throw new InvalidInputException();
        }
    }
}
