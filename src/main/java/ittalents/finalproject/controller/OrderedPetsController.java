package ittalents.finalproject.controller;

import ittalents.finalproject.model.pojos.orders.FinalOrderPets;
import ittalents.finalproject.service.OrderPetService;
import ittalents.finalproject.util.exceptions.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
public class OrderedPetsController extends BaseController {

    @Autowired
    OrderPetService service;

    @Transactional
    @PostMapping(value = "/orders/add")
    public Object makeOrder(@RequestBody FinalOrderPets order, HttpSession session) throws BaseException {
        validateLogin(session);
        return service.makeOrder(order, session);
    }
}
