package ittalents.finalproject.controller;

import ittalents.finalproject.service.CartService;
import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.util.exceptions.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@RestController
public class CartController extends BaseController {

    @Autowired
    private CartService cartService;


    @PostMapping(value = "cart/add/products/{id}")
    public Object addProductsToCart(@PathVariable("id") long id, @RequestParam("quantity") int quantity,
                                     HttpSession session) throws BaseException{

        validateLogin(session);
        return cartService.addProductsToCart(id, quantity, session);
    }


    @PostMapping(value = "cart/add/pets/{id}")
    public Message addPetToCart(@PathVariable long id, @RequestParam int quantity, HttpSession session)
            throws BaseException{
        validateLogin(session);
        return cartService.addPetToCart(id, quantity, session);
    }


    @PostMapping(value = "cart/remove/products/{id}")
    public Object removeProductsFromCart(@PathVariable("id") long id, @RequestParam("quantity") int quantity, HttpSession session)
                                        throws BaseException{
        validateLogin(session);
        return cartService.removeProductsFromCart(id, quantity, session);
    }


    @DeleteMapping(value = "/cart/remove/pets/{id}")
    public Message removePetFromCart(@PathVariable long id, @RequestParam int quantity, HttpSession session)
        throws BaseException{
        validateLogin(session);
        return cartService.removePetFromCart(id, quantity, session);
    }

    @GetMapping(value = "/cart")
    public Object cartContent(HttpSession session) throws BaseException{
        validateLogin(session);
        return cartService.getCartContent(session);
    }
}
