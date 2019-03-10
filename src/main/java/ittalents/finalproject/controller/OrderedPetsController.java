package ittalents.finalproject.controller;

import ittalents.finalproject.model.dao.PetDao;
import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.User;
import ittalents.finalproject.model.pojos.orders.FinalOrderPets;
import ittalents.finalproject.model.pojos.pets.OrderedPet;
import ittalents.finalproject.model.pojos.pets.Pet;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.PetNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Enumeration;

import static ittalents.finalproject.controller.CartController.PET_STR;


@RestController
public class OrderedPetsController extends BaseController {

    @Autowired
    PetDao dao;

    @Autowired
    private TransactionTemplate trans;

    @Transactional
    @PostMapping(value = "/orders/add")
    public Message makeOrder(@RequestBody String address, HttpSession session) throws Exception {
        validateLogin(session);
        FinalOrderPets order = null;
        double finalPrice = 0;
        Enumeration<String> attr = session.getAttributeNames();
        while(attr.hasMoreElements()){
            String key = attr.nextElement();
            if(key.contains(LOGGED_USER)){
                continue;
            }
            if(key.contains(PET_STR)) {
                order = new FinalOrderPets(address);
                User user = (User)session.getAttribute(LOGGED_USER);
                order.setUserId(user.getId());
                order.setFinalPrice(finalPrice);
                dao.insertOrder(order);
                int quantity = (int) session.getAttribute(key);
                key = key.replace(PET_STR, "");
                long idPet = Long.valueOf(key);
                OrderedPet orderedPet = new OrderedPet();
                Pet petInSale = dao.getPetForSaleById(idPet);
                Pet pet = dao.getById(idPet);
                if (petInSale != null) {
                    finalPrice += petInSale.getPrice() * quantity;
                }
                else if(pet != null) {
                    finalPrice += pet.getPrice() * quantity;
                }
                else{
                    throw new PetNotFoundException();
                }
                orderedPet.setPetId(idPet);
                orderedPet.setOrderId(order.getId());
                orderedPet.setQuantity(quantity);
                dao.insertOrderedPet(orderedPet);
            }
        }
        removePetsFromCart(session);
        if(order != null && finalPrice != 0) {
            order.setFinalPrice(finalPrice);
            dao.updateOrder(order);
            return new Message("Order made successfully", LocalDateTime.now(), HttpStatus.OK.value());
        }
        else{
            return new Message("Cart is empty!", LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
        }
    }

    private void removePetsFromCart(HttpSession session) {
        Enumeration<String> pets = session.getAttributeNames();
        while(pets.hasMoreElements()){
            String key = pets.nextElement();
            if(key.contains(PET_STR)){
                session.removeAttribute(key);
            }
        }
    }
}
