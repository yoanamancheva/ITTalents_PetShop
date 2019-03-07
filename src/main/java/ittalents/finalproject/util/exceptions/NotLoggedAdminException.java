package ittalents.finalproject.util.exceptions;

public class NotLoggedAdminException extends BaseException {
    public NotLoggedAdminException()
    {
        super("You are not logged as admin.");
    }
}
