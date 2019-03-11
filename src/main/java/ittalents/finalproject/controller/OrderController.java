package ittalents.finalproject.controller;

import ittalents.finalproject.service.OrderService;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.model.pojos.User;
import ittalents.finalproject.model.pojos.orders.FinalOrderProducts;
import ittalents.finalproject.model.pojos.products.OrderedProduct;
import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.model.pojos.products.ProductInSale;
import ittalents.finalproject.model.repos.FinalOrderProductRepository;
import ittalents.finalproject.model.repos.OrderedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@RestController
public class OrderController  extends BaseController{

    @Autowired
    private FinalOrderProductRepository finalOrderProductRepository;


    @Autowired
    private OrderedProductRepository orderedProductRepository;

    @Autowired
    private OrderService orderService;


    @PostMapping(value = "finalOrder/product")
    public Object addFinalOrderProduct (@RequestBody FinalOrderProducts finalOrderProduct) {
        return finalOrderProductRepository.save(finalOrderProduct);
    }

    @PostMapping(value = "cart/order")
    public Object makeOrder(@RequestBody FinalOrderProducts finalOrder,HttpSession session) throws BaseException {
        validateLogin(session);
        return orderService.makeOrderProducts(finalOrder, session);
    }


//    testing
    @PostMapping(value = "products/order/test")
    public Object test(@RequestBody OrderedProduct orderedProduct) {
        OrderedProduct orderedProduct1 = new OrderedProduct();
        OrderedProduct.OrderedProductPk pk = new OrderedProduct.OrderedProductPk();
        pk.setOrderId((long) 1);
        pk.setProductId((long)15);

        orderedProduct1.setOrderedProductPk(pk);
        orderedProduct1.setQuantity(33);
        return orderedProductRepository.save(orderedProduct1);
    }
}
