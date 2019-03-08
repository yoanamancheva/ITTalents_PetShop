package ittalents.finalproject.controller;

import ittalents.finalproject.model.dao.PetDao;
import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.User;
import ittalents.finalproject.model.pojos.orders.FinalOrderPets;
import ittalents.finalproject.model.pojos.pets.OrderedPet;
import ittalents.finalproject.model.pojos.pets.Pet;
import ittalents.finalproject.util.exceptions.PetNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.Enumeration;


@RestController
public class OrderedPetsController extends BaseController {

    @Autowired
    JdbcTemplate db;

    @Autowired
    PetDao dao;

    @PostMapping(value = "/orders/add")
    public Message makeOrder(@RequestBody String address, HttpSession session) throws Exception{
        //validate if the user is logged
        validateLogin(session);
        //transaction
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = db.getDataSource().getConnection();
            con.setAutoCommit(false);
            //make new FinalOrder
            FinalOrderPets order = new FinalOrderPets(address);
            double finalPrice = 0;
            //get user from session with id
            //set userId to FinalOrder
            User user = (User)session.getAttribute(LOGGED_USER);
            order.setUserId(user.getId());
            order.setFinalPrice(finalPrice);
            //get all pets from session
            Enumeration<String> attr = session.getAttributeNames();
            while(attr.hasMoreElements()){
                String key = attr.nextElement();
                System.out.println(key);
                if(key.contains("_pet")){
                    int quantity = (int)session.getAttribute(key);
                    key = key.replace("_pet", "");
                    OrderedPet orderedPet = new OrderedPet();
                    Pet pet = dao.getPetForSaleById(Long.getLong(key));
                    if(pet == null){
                        pet = dao.getById(Long.getLong(key));
                        if(pet == null) {
                            throw new PetNotFoundException();
                        }
                        else{
                            orderedPet.setPetId(Long.getLong(key));
                            finalPrice += pet.getPrice() * quantity;
                            orderedPet.setOrderId(order.getId());
                            orderedPet.setQuantity(quantity);
                        }
                    }
                    else{
                        orderedPet.setPetId(Long.getLong(key));
                        finalPrice += pet.getPrice() * quantity;
                        orderedPet.setOrderId(order.getId());
                        orderedPet.setQuantity(quantity);
                    }
                    dao.insertOrderedPet(orderedPet);
                }
            }
            //the length of array with pets the more products added in OrderedPet
            //every iteration set the pet_id with id of FinalOrder
            //check if the pet is in sale if yes take the price from there if not take the price from the main table
            //count the price of the pets in one variable
            //set the count to the final price of the FinalOrder

            //remove items from cart
            order.setFinalPrice(finalPrice);
            dao.insertOrder(order);

        }
        catch(Exception e){
            con.rollback();
            throw new Exception();
        }
        finally {
            con.setAutoCommit(true);
            con.close();
            ps.close();
        }

        return new Message("Order made succesfully", LocalDateTime.now(), HttpStatus.OK.value());
    }
}
