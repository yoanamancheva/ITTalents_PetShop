package ittalents.finalproject.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Enumeration;

import static ittalents.finalproject.controller.BaseController.LOGGED_USER;
import static ittalents.finalproject.service.CartService.PET_STR;

@Service
public class OrderPetService {

    @Autowired
    PetDao dao;

    @Transactional
    public Object makeOrder(FinalOrderPets order, HttpSession session) throws BaseException {
        double finalPrice = 0;
        Enumeration<String> attr = session.getAttributeNames();
        while(attr.hasMoreElements()){
            String key = attr.nextElement();
            if(key.contains(LOGGED_USER)){
                continue;
            }
            if(key.contains(PET_STR)) {
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
                removePetFromCartById(idPet, session);
            }
        }
        if(order != null && finalPrice != 0) {
            order.setFinalPrice(finalPrice);
            dao.updateOrder(order);
            return order;
        }
        else{
            return new Message("Cart is empty!", LocalDateTime.now(), HttpStatus.BAD_REQUEST.value());
        }
    }

    private void removePetFromCartById(Long id, HttpSession session)
            throws PetNotFoundException {
        if(id != null && dao.getById(id) != null) {
            Enumeration<String> pets = session.getAttributeNames();
            while (pets.hasMoreElements()) {
                String key = pets.nextElement();
                if (key.contains(PET_STR) && key.contains(id.toString())) {
                    session.removeAttribute(key);
                }
            }
        }
        else{
            throw new PetNotFoundException();
        }
    }
}
