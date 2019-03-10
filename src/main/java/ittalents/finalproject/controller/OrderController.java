package ittalents.finalproject.controller;

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

//    @Autowired
//    private ProductInSaleController productInSaleController;

    @Autowired
    private OrderedProductRepository orderedProductRepository;

    @Autowired
    private ProductController productController;




    //working
    @PostMapping(value = "finalOrder/product")
    public Object addFinalOrderProduct (@RequestBody FinalOrderProducts finalOrderProduct) {
        return finalOrderProductRepository.save(finalOrderProduct);
    }


    @PostMapping(value = "cart/order")
    public Object makeOrder(@RequestBody FinalOrderProducts finalOrder,HttpSession session) throws BaseException {
        List<OrderedProduct> list = new ArrayList<>();

        OrderedProduct orderedProduct1 = new OrderedProduct();
        OrderedProduct.OrderedProductPk pk = new OrderedProduct.OrderedProductPk();

        double money = 0;

        User user = (User)session.getAttribute(LOGGED_USER);
        long userId = user.getId();

        finalOrder.setUserId(userId);


        Enumeration<String> attributes = session.getAttributeNames();
        while ((attributes.hasMoreElements())) {

            String attribute = attributes.nextElement();

            if(attribute.contains("_product")) {
                String[] fullName = attribute.split("_");
                String id = fullName[0];
                long convertedId = Long.valueOf(id);

                orderedProduct1.setMidProductId(convertedId);
                orderedProduct1.setQuantity((int)session.getAttribute(attribute));

                list.add(orderedProduct1);
                orderedProduct1 = new OrderedProduct();


                Product product = productController.getById(convertedId, session);
                if(checkIfProductIsInSale(product, session) != -1) {
                    money += checkIfProductIsInSale(product, session) * (int)session.getAttribute(attribute);
//                    System.out.println("mid money = " + money);
                }
                else {
                    money += product.getPrice() * (int)session.getAttribute(attribute);
//                    System.out.println("mid money = " + money);
                }

            }
        }
//        System.out.println("All money: " + money);

        finalOrder.setFinalPrice(money);

//        to save in all_orders_products table
        long orderId =  finalOrderProductRepository.save(finalOrder).getId();

        for (OrderedProduct orderedProduct : list) {
            orderedProduct.setMidOrderId(orderId);

            pk.setOrderId(orderId);

            pk.setProductId(orderedProduct.getMidProductId());

            orderedProduct.setOrderedProductPk(pk);
            orderedProductRepository.save(orderedProduct);
        }
        removeAttributesFromSession(session, "_product");

        return finalOrder;
    }


    private double checkIfProductIsInSale(Product product, HttpSession session) throws BaseException{
        long id = product.getId();
        ProductInSale productInSale = productController.getProductInSaleByProductId(id, session);
        if(productInSale != null) {
            return productInSale.getDiscountPrice();
        }
        else return -1;
    }

    private void removeAttributesFromSession(HttpSession session, String name) {
        Enumeration<String> attributes = session.getAttributeNames();
        while ((attributes.hasMoreElements())) {
            String attribute = attributes.nextElement();
            if(attribute.contains(name)) {
                session.removeAttribute(attribute);
            }
        }
    }


    //working
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
