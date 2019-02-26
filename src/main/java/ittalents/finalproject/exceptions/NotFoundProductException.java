package ittalents.finalproject.exceptions;

public class NotFoundProductException extends BaseException {

    public NotFoundProductException(){
        super("Product not found");
    }
}
