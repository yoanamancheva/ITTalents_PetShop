package ittalents.finalproject.controller;

import ittalents.finalproject.exceptions.BaseException;
import ittalents.finalproject.model.pojos.ErrorMsg;
import ittalents.finalproject.model.pojos.products.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Enumeration;

@RestController
public class CartController extends BaseController {

    @Autowired
    private ProductController productController;


    @PostMapping(value = "card/add/{id}/{quantity}")
    public Object addProductToCart(@PathVariable("id") long id, @PathVariable("quantity") int quantity, HttpSession session) throws BaseException{
        validateLogin(session);
        Product product = productController.getById(id, session);
        session.setAttribute(product.getName(), quantity);
        System.out.println("=========================================================================");
        Enumeration<String> attributes = session.getAttributeNames();
        while ((attributes.hasMoreElements())) {
            String attribute = attributes.nextElement();
            System.out.println(attribute + " : " + session.getAttribute(attribute));
        }
        return new ErrorMsg("You successfully added " +product.getName() + " to your cart", LocalDateTime.now(), HttpStatus.OK.value());

    }
}
