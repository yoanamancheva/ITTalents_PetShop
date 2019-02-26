package ittalents.finalproject.controller;

import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.model.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(value = "/products")
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @GetMapping(value = "/products/{id}")
    public Product getById(@PathVariable("id") long id) {
        return productRepository.findById(id).get();//todo fix if absent
    }

    @PostMapping(value = "/products")
    public Product save(@RequestBody Product product) {
        productRepository.save(product);
        return product;
    }
}
