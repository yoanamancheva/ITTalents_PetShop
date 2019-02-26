package ittalents.finalproject.controller;

import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.model.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(value = "/products")
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @GetMapping(value = "/products/{id}")
    public Product getById(@PathVariable("id") long id) throws ProductNotFoundException {
        Optional<Product> obj = productRepository.findById(id);
        if(obj.isPresent()) {
            return obj.get();
        }
        else {
            throw new ProductNotFoundException("fu");
        }
    }


    //working
    @PostMapping(value = "/products/filter")
    public Optional<Product> getProductByName(@RequestParam("name") String name) throws ProductNotFoundException{
        Optional<Product> product = productRepository.findByName(name);
        if(product.isPresent()) {
            return product;
        }
        else {
            throw new ProductNotFoundException("Product not found");
        }
    }


    //not working
//    @GetMapping(value = "/products/byPrice")
//    public List<Product> filterByPrice() {
//        return productRepository.findAllByPrice();
//    }


    @PostMapping(value = "/products")
    public Product save(@RequestBody Product product) {
        productRepository.save(product);
        return product;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No product with that id")
    private class ProductNotFoundException extends Exception {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
}
