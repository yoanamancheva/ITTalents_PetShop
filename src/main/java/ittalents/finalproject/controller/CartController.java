package ittalents.finalproject.controller;

import ittalents.finalproject.service.CartService;
import ittalents.finalproject.util.exceptions.PetOutOfStockException;
import ittalents.finalproject.model.dao.PetDao;
import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.dto.CartContentDto;
import ittalents.finalproject.model.pojos.pets.Pet;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.InvalidInputException;
import ittalents.finalproject.util.exceptions.ProductOutOfStockException;
import ittalents.finalproject.model.pojos.products.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

@RestController
public class CartController extends BaseController {

    public static final String PET_STR = "_pet";
    public static final String PRODUCT_STR = "_product";

    @Autowired
    private ProductController productController;

    @Autowired
    private PetDao dao;

    @Autowired
    private CartService cartService;


    @PostMapping(value = "cart/add/products/{id}")
    public Object addProductsToCart(@PathVariable("id") long id, @RequestParam("quantity") int quantity,
                                     HttpSession session) throws BaseException{

        validateLogin(session);
        return cartService.addProductsToCart(id, quantity, session);
    }

//    todo move to service
//    @PostMapping(value = "cart/add/pets/{id}")
//    public Message addPetToCart(@PathVariable long id, @RequestParam int quantity, HttpSession session)
//                throws BaseException{
//        validateLogin(session);
//        Pet pet = dao.getById(id);
//        if(sessionContainsResourse(id, session, PET_STR) && pet.getQuantity() >= quantity
//                && pet.getQuantity() > 0 && quantity > 0
//                && (int)session.getAttribute(id + PET_STR) + quantity <= pet.getQuantity()){
//            int currQuantity = (int) session.getAttribute(pet.getId() + PET_STR);
//            session.setAttribute(id + PET_STR, currQuantity + quantity);
//            showCart(session);
//            return new Message("Pet successfully modified in cart!", LocalDateTime.now(), HttpStatus.OK.value());
//        }
//        else if (!sessionContainsResourse(id, session, PET_STR) && quantity > 0 &&
//                pet.getQuantity() >= quantity && pet.getQuantity() > 0) {
//            session.setAttribute(id + PET_STR, quantity);
//            showCart(session);
//            return new Message("Pet successfully added to cart!", LocalDateTime.now(), HttpStatus.OK.value());
//        }
//        else{
//            throw new PetOutOfStockException();
//        }
//    }


    @PostMapping(value = "cart/remove/products/{id}")
    public Object removeProductsFromCart(@PathVariable("id") long id, @RequestParam("quantity") int quantity, HttpSession session)
                                        throws BaseException{
        validateLogin(session);
        return cartService.removeProductsFromCart(id, quantity, session);
    }


//        todo move to service
//    @DeleteMapping(value = "/cart/remove/pets/{id}")
//    public Message removePetFromCart(@PathVariable long id, @RequestParam int quantity, HttpSession session)
//        throws BaseException{
//        validateLogin(session);
//        Pet pet = dao.getById(id);
//        if(sessionContainsResourse(id, session, PET_STR) && quantity > 0 &&
//                (int)session.getAttribute(id + PET_STR) >= quantity){
//            int currQuantity = (int)session.getAttribute(id + PET_STR);
//            session.setAttribute(id + PET_STR, currQuantity - quantity);
//            if(currQuantity == quantity){
//                session.removeAttribute(id + PET_STR);
//            }
//            showCart(session);
//            return new Message("Successfully removed items from cart!", LocalDateTime.now(), HttpStatus.OK.value());
//        }
//        else {
//            throw new InvalidInputException("You can not remove more products than you have.");
//        }
//    }

    @GetMapping(value = "/cart")
    public Object cartContent(HttpSession session) throws BaseException{
        validateLogin(session);
        return cartService.getCartContent(session);
    }
}
