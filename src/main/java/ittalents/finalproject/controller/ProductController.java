package ittalents.finalproject.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import ittalents.finalproject.exceptions.BaseException;
import ittalents.finalproject.exceptions.NotLoggedInException;
import ittalents.finalproject.exceptions.ProductNotFoundException;
import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.model.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController extends BaseController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(value = "/products")
    public List<Product> getAll(HttpSession session) throws BaseException {
        validateLogin(session);
        return productRepository.findAll();
    }



    @GetMapping(value = "/products/{id}")
    public Product getById(@PathVariable("id") long id) throws BaseException{
        Optional<Product> obj = productRepository.findById(id);
        if(obj.isPresent()) {
            return obj.get();
        }
        else {
            throw new ProductNotFoundException("Product not found with that id.");
        }
    }


    //working
    @PostMapping(value = "/products/filter")
    public Optional<Product> getProductByName(@RequestParam("name") String name) throws BaseException{
        Optional<Product> product = productRepository.findByName(name);
        if(product.isPresent()) {
            return product;
        }
        else {
            throw new ProductNotFoundException("Product not found with that name.");
        }
    }



    //working
    //to add exception when there arent any

    @PostMapping(value = "/products/filterByPriceAndCategory")
    public List<Product> filterByPrice(@RequestParam("category") String category) {
        return productRepository.findAllByCategoryOrderByPrice(category);
    }


    @PostMapping(value = "/products")
    public Product save(@RequestBody Product product) {
        productRepository.save(product);
        return product;
    }

}
