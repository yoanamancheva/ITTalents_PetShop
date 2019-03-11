package ittalents.finalproject.service;

import ittalents.finalproject.controller.ProductController;
import ittalents.finalproject.model.pojos.User;
import ittalents.finalproject.model.pojos.orders.FinalOrderProducts;
import ittalents.finalproject.model.pojos.products.OrderedProduct;
import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.model.pojos.products.ProductInSale;
import ittalents.finalproject.model.repos.FinalOrderProductRepository;
import ittalents.finalproject.model.repos.OrderedProductRepository;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static ittalents.finalproject.controller.BaseController.LOGGED_USER;

@Service
public class OrderService {

    @Autowired
    private ProductController productController;

    @Autowired
    private FinalOrderProductRepository finalOrderProductRepository;

    @Autowired
    private OrderedProductRepository orderedProductRepository;


// make products order
    @Transactional
    public Object makeOrderProducts(FinalOrderProducts finalOrder, HttpSession session) throws BaseException {
        User user = (User)session.getAttribute(LOGGED_USER);
        validateVerifiedUser(user);
        
        List<OrderedProduct> list = new ArrayList<>();

        OrderedProduct orderedProduct1 = new OrderedProduct();
        OrderedProduct.OrderedProductPk pk = new OrderedProduct.OrderedProductPk();

        double money = 0;
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
                }
                else {
                    money += product.getPrice() * (int)session.getAttribute(attribute);
                }
            }
        }
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

    private void validateVerifiedUser(User user) throws BaseException {
        if(!user.isVerified()) {
            throw new UserNotFoundException("You have to verify your email address to make an order");
        }
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
}
