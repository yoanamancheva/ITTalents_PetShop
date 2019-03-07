package ittalents.finalproject.util.exceptions;

public class NotLoggedInException extends BaseException {
    public NotLoggedInException(){
        super("The user is not logged.");
    }
}
