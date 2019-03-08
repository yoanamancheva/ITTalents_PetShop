package ittalents.finalproject.controller;

import ittalents.finalproject.model.dao.PetDao;
import ittalents.finalproject.model.pojos.orders.FinalOrderPets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.Message;
import javax.servlet.http.HttpSession;

public class OrderedPetsController extends BaseController {

    @Autowired
    PetDao dao;

//    @PostMapping(value = "/orders/add")
//    public Message addOrder(@RequestBody FinalOrderPets order, HttpSession session){
//
//    }
}
