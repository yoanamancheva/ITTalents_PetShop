package ittalents.finalproject.exceptions;

public class PetOutOfStockException extends BaseException {

    public PetOutOfStockException(){
        super("The pet is out of stock!");
    }
}
