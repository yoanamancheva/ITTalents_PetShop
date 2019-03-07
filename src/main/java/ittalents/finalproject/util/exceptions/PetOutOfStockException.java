package ittalents.finalproject.util.exceptions;

import ittalents.finalproject.util.exceptions.BaseException;

public class PetOutOfStockException extends BaseException {

    public PetOutOfStockException(){
        super("The pet is out of stock!");
    }
}
