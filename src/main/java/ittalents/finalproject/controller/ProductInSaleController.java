package ittalents.finalproject.controller;

import ittalents.finalproject.exceptions.BaseException;
import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.model.pojos.products.ProductInSale;
import ittalents.finalproject.model.repos.ProductInSaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
public class ProductInSaleController extends BaseController {

    @Autowired
    private ProductInSaleRepository productInSaleRepository;

    @GetMapping(value = "products/sale/{product_id}")
    public ProductInSale getProductInSaleByProductId(@PathVariable("product_id") long id, HttpSession session) throws BaseException {
        validateLogin(session);
        List<ProductInSale> list = productInSaleRepository.findByProductId(id);

        if(list.size() > 0) {
            for (ProductInSale productInSale : list) {
                if(productInSale.getStartDate().compareTo(LocalDateTime.now()) < 0
                && LocalDateTime.now().compareTo(productInSale.getEndDate()) < 0) {
                    return productInSale;
                }
            }
        }
        return null;
    }
}
