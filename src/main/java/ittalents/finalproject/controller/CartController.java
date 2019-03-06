package ittalents.finalproject.controller;

import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.InvalidInputException;
import ittalents.finalproject.util.exceptions.ProductOutOfStockException;
import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.products.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Enumeration;

@RestController
public class CartController extends BaseController {

    @Autowired
    private ProductController productController;


    private boolean sessionContainsProduct(long id, HttpSession session) throws BaseException {
        Product product = productController.getById(id, session);
        Enumeration<String> attributes = session.getAttributeNames();
        while ((attributes.hasMoreElements())) {
            String attribute = attributes.nextElement();
            if(attribute.equals(product.getId() + "_product")) {
                return true;
            }
        }
        return false;
    }

    private void showCart(HttpSession session) {
        Enumeration<String> attributes = session.getAttributeNames();
        System.out.println("=========================================================================");
        while ((attributes.hasMoreElements())) {
            String attribute = attributes.nextElement();
            System.out.println(attribute + " : " + session.getAttribute(attribute));
        }
    }

    @PostMapping(value = "cart/add/{id}/{quantity}")
    public Object addProductsToCart(@PathVariable("id") long id, @PathVariable("quantity") int quantity, HttpSession session)
                                    throws BaseException{
        System.out.println("-----------------------------------------");
        System.out.println(session.getAttribute(LOGGED_USER));
        validateLogin(session);
        Product product = productController.getById(id, session);
        if(sessionContainsProduct(id, session) && product.getQuantity() >= quantity && product.getQuantity() > 0) {
            int previousNumber = (int)session.getAttribute(product.getId() + "_product");
            session.setAttribute(product.getId() + "_product", previousNumber + quantity);
            showCart(session);
            return new Message("You successfully added " + product.getName() + " to your cart",
                                LocalDateTime.now(), HttpStatus.OK.value());
        }
        else if(product.getQuantity() >= quantity && product.getQuantity() > 0) {
            session.setAttribute(product.getId() + "_product", quantity);
            showCart(session);

            return new Message("You successfully added " + product.getName() + " to your cart", LocalDateTime.now(),
                                HttpStatus.OK.value());
        }
        else {
            throw new ProductOutOfStockException("There are not enough pieces of this product.");
        }
    }

    @PostMapping(value = "cart/remove/{id}/{quantity}")
    public Object removeProductsFromCart(@PathVariable("id") long id, @PathVariable("quantity") int quantity, HttpSession session)
                                        throws BaseException{
        validateLogin(session);
        Product product = productController.getById(id, session);
        String attribute = product.getId()+ "_product";

        if(sessionContainsProduct(id, session) &&  (int)session.getAttribute(attribute) >= quantity && quantity > 0) {
            int previousNumber = (int)session.getAttribute(product.getId() + "_product");

            session.setAttribute(product.getId() + "_product", previousNumber - quantity);
            showCart(session);
            return new Message("You successfully removed " +quantity +" " + product.getName() + " from your cart",
                                LocalDateTime.now(), HttpStatus.OK.value());
        }
        else {
            throw new InvalidInputException("You can not remove more products than you have.");
        }
    }

}
