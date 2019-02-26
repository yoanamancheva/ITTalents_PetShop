package ittalents.finalproject.controller;

import ittalents.finalproject.exceptions.*;
import ittalents.finalproject.model.pojos.ErrorMsg;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RestController
public abstract class BaseController {

    @ExceptionHandler({NotLoggedInException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMsg notLoggedHandler(){
        return new ErrorMsg(new NotLoggedInException().getMessage(), LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler({NotLoggedInException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMsg notLoggedAdminHandler(){
        return new ErrorMsg(new NotLoggedAdminException().getMessage(), LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler({BaseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMsg basedHandler(){
        return new ErrorMsg(new BaseException("You are trying to input not valid properties. Try again!").getMessage(),
                LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMsg allExceptionHandler(){
        return new ErrorMsg(new Exception("Sorry, the server is temporary down. ").getMessage(), LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value());
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
    //TODO validates for product and pet

    //not sure are these two needed ?!
    protected void validateProduct(HttpSession session)throws NotLoggedInException, NotFoundProductException {

    }

    protected void validatePet(HttpSession session) throws NotLoggedAdminException, NotFoundPetException {

    }

}
