package ittalents.finalproject.exceptions;

public class NotFoundPetException extends BaseException {
    public NotFoundPetException(){
        super("Pet not found.");
    }
}
