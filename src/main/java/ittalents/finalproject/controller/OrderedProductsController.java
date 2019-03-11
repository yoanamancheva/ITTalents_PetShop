package ittalents.finalproject.controller;

import ittalents.finalproject.model.pojos.products.OrderedProduct;
import ittalents.finalproject.model.repos.OrderedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
public class OrderedProductsController extends BaseController {

    @Autowired
    private OrderedProductRepository orderedProductRepository;


    @GetMapping(value = "/products/ordered")
    public List<OrderedProduct> allOrderedProducts () {
        return orderedProductRepository.findAll();
    }

}
