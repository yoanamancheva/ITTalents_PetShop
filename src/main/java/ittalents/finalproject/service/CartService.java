package ittalents.finalproject.service;

import ittalents.finalproject.controller.PetController;
import ittalents.finalproject.controller.ProductController;
import ittalents.finalproject.model.dao.PetDao;
import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.dto.CartContentDto;
import ittalents.finalproject.model.pojos.pets.Pet;
import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.util.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import static ittalents.finalproject.controller.BaseController.LOGGED_USER;

@Service
public class CartService {

    public static final String PET_STR = "_pet";
    public static final String PRODUCT_STR = "_product";

    @Autowired
    private ProductController productController;

    @Autowired
    private PetDao dao;

    @Autowired
    private PetController petCtrl;


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

//    add pet quantity to cart
    public Message addPetToCart(Long id, int quantity, HttpSession session)
            throws BaseException{
        Pet pet;
        if(id != null && petCtrl.getById(id) != null) {
            pet = dao.getById(id);
        }
        else{
            throw new PetNotFoundException();
        }
        if(sessionContainsResourse(id, session, PET_STR) && pet.getQuantity() >= quantity
                && pet.getQuantity() > 0 && quantity > 0
                && (int)session.getAttribute(id + PET_STR) + quantity <= pet.getQuantity()){
            int currQuantity = (int) session.getAttribute(pet.getId() + PET_STR);
            session.setAttribute(id + PET_STR, currQuantity + quantity);
            showCart(session);
            return new Message("Pet successfully modified in cart!", LocalDateTime.now(), HttpStatus.OK.value());
        }
        else if (!sessionContainsResourse(id, session, PET_STR) && quantity > 0 &&
                pet.getQuantity() >= quantity && pet.getQuantity() > 0) {
            session.setAttribute(id + PET_STR, quantity);
            showCart(session);
            return new Message("Pet successfully added to cart!", LocalDateTime.now(), HttpStatus.OK.value());
        }
        else{
            throw new PetOutOfStockException();
        }
    }

//    remove pet quantity from cart
    public Message removePetFromCart(@PathVariable Long id, @RequestParam int quantity, HttpSession session)
            throws BaseException{
        if(id == null || petCtrl.getById(id) == null){
            throw new PetNotFoundException();
        }
        if(sessionContainsResourse(id, session, PET_STR) && quantity > 0 &&
                (int)session.getAttribute(id + PET_STR) >= quantity){
            int currQuantity = (int)session.getAttribute(id + PET_STR);
            session.setAttribute(id + PET_STR, currQuantity - quantity);
            if(currQuantity == quantity){
                session.removeAttribute(id + PET_STR);
            }
            showCart(session);
            return new Message("Successfully removed items from cart!", LocalDateTime.now(), HttpStatus.OK.value());
        }
        else {
            throw new InvalidInputException("You can not remove more products than you have.");
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
