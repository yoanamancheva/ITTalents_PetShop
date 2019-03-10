package ittalents.finalproject.service;

import ittalents.finalproject.controller.ProductController;
import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.dto.CartContentDto;
import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.InvalidInputException;
import ittalents.finalproject.util.exceptions.ProductOutOfStockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import static ittalents.finalproject.controller.BaseController.LOGGED_USER;
import static ittalents.finalproject.controller.CartController.PRODUCT_STR;

@Service
public class CartService {

    @Autowired
    private ProductController productController;


//    add product with quantity to cart
    public Object addProductsToCart(long id, int quantity, HttpSession session) throws BaseException {

        Product product = productController.getById(id, session);
        if(sessionContainsResourse(id, session, PRODUCT_STR) && product.getQuantity() >= quantity
                && product.getQuantity() > 0 && quantity > 0 &&
                (int)session.getAttribute(id + PRODUCT_STR) + quantity <= product.getQuantity()) {
            int previousNumber = (int)session.getAttribute(product.getId() + PRODUCT_STR);
            session.setAttribute(product.getId() + PRODUCT_STR, previousNumber + quantity);
            showCart(session);
            return new Message("You successfully added " + product.getName() + " to your cart",
                    LocalDateTime.now(), HttpStatus.OK.value());
        }
        else if(!sessionContainsResourse(id, session, PRODUCT_STR) && product.getQuantity() >= quantity
                && product.getQuantity() > 0 && quantity > 0) {
            session.setAttribute(product.getId() + PRODUCT_STR, quantity);
            showCart(session);

            return new Message("You successfully added " + product.getName() + " to your cart",
                                LocalDateTime.now(), HttpStatus.OK.value());
        }
        else {
            throw new ProductOutOfStockException("There are not enough pieces of this product.");
        }
    }

//    remove product quantity from cart
    public Object removeProductsFromCart(long id, int quantity, HttpSession session) throws BaseException {
        Product product = productController.getById(id, session);
        String attribute = product.getId()+ PRODUCT_STR;

        if(sessionContainsResourse(id, session, PRODUCT_STR) &&  (int)session.getAttribute(attribute) >= quantity && quantity > 0) {
            int previousNumber = (int)session.getAttribute(product.getId() + PRODUCT_STR);
            if(previousNumber == quantity){
                session.removeAttribute(id + PRODUCT_STR);
            }
            session.setAttribute(product.getId() + PRODUCT_STR, previousNumber - quantity);
            showCart(session);
            return new Message("You successfully removed " +quantity +" " + product.getName() + " from your cart",
                    LocalDateTime.now(), HttpStatus.OK.value());
        }
        else {
            throw new InvalidInputException("You can not remove more products than you have.");
        }
    }

//    returns list of products/pets in cart
    public Object getCartContent(HttpSession session) {
        List<CartContentDto> content = new LinkedList<>();
        Enumeration<String> attributes = session.getAttributeNames();


        while ((attributes.hasMoreElements())) {
            String attribute = attributes.nextElement();
            if(!attribute.contains(LOGGED_USER)) {
                content.add(new CartContentDto(attribute, (int)session.getAttribute(attribute)));
            }
            showCart(session);
        }
        if(!content.isEmpty()){
            return content;
        }
        else{
            return new Message("No items in cart", LocalDateTime.now(), HttpStatus.NOT_FOUND.value());
        }
    }

//    ------------------------------------------------------------------------------------------------------------------
    private boolean sessionContainsResourse(long id, HttpSession session, String type) {
        Enumeration<String> attributes = session.getAttributeNames();
        while ((attributes.hasMoreElements())) {
            String attribute = attributes.nextElement();
            if(attribute.equals(id + type)) {
                return true;
            }
        }
        return false;
    }

    private void showCart(HttpSession session) {
        Enumeration<String> attributes = session.getAttributeNames();
        while ((attributes.hasMoreElements())) {
            String attribute = attributes.nextElement();
            System.out.println(attribute + " : " + session.getAttribute(attribute));
        }
    }
}
