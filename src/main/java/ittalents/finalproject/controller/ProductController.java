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

    //not working
//    @GetMapping(value = "/products/filter")
//    public Product getProductByName(@RequestParam("product_name") String product_name) {
//        Product product = productRepository.findByProduct_name(product_name);
//        return product;
//    }


    @PostMapping(value = "/products")
    public Product save(@RequestBody Product product) {
        productRepository.save(product);
        return product;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "no product with that id")
    private class ProductNotFoundException extends Exception {
        public ProductNotFoundException(String fu) {
        }
    }
}
