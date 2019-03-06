package ittalents.finalproject.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import ittalents.finalproject.exceptions.BaseException;
import ittalents.finalproject.exceptions.InvalidInputException;
import ittalents.finalproject.exceptions.NotLoggedInException;
import ittalents.finalproject.exceptions.ProductNotFoundException;
import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.dto.ProductReviewsDTO;
import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.model.pojos.products.ProductInSale;
import ittalents.finalproject.model.repos.ProductInSaleRepository;
import ittalents.finalproject.model.repos.ProductRepository;
import ittalents.finalproject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;


    @Autowired
    private ProductRepository productRepository;

    @GetMapping(value = "/products")
    public List<Product> getAll(HttpSession session) throws BaseException {
        validateLogin(session);
        return productRepository.findAll();
    }



    @GetMapping(value = "/products/{id}")
    public Product getById(@PathVariable("id") long id, HttpSession session) throws BaseException{
        validateLogin(session);
        Optional<Product> obj = productRepository.findById(id);
        if(obj.isPresent()) {
            return obj.get();
        }
        else {
            throw new ProductNotFoundException("Product not found with that id.");
        }
    }

    @PostMapping(value = "/products/filter")
    public Optional<Product> showProductByName(@RequestParam("name") String name) throws BaseException{
        Optional<Product> product = productRepository.findByName(name);
        if(product.isPresent()) {
            return product;
        }
        else {
            throw new ProductNotFoundException("Product not found with that name.");
        }
    }


    @PostMapping(value = "/products/filter/category")
    public List<Product> filterByPrice(@RequestParam("category") String category) throws BaseException {
        List<Product> products = productRepository.findAllByCategoryOrderByPrice(category);
        if(products.isEmpty()) {
            throw new ProductNotFoundException("No products found out of that category.");
        }
        return products;
    }


    @PostMapping(value = "/products/add")
    public Product add(@RequestBody Product product, HttpSession session) throws BaseException {
        validateLoginAdmin(session);
        validateProductInput(product);
        validateProductByName(product);
        productRepository.save(product);
        return product;
    }

    @PutMapping(value = "/products/update")
    public Product update(@RequestBody Product product, HttpSession session) throws BaseException{
        validateLoginAdmin(session);
        validateProductInput(product);
        if (!productRepository.findById(product.getId()).isPresent()) {
            throw new InvalidInputException("There is not product with this id in the database.");
        }
        productRepository.save(product);
        return product;
    }

    //todo to update in sale table and review
    //1 to many
    @DeleteMapping(value = "/products/remove/{id}")
    public Object remove(@PathVariable("id") long id, HttpSession session) throws BaseException {
        validateLoginAdmin(session);
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()) {
            productRepository.delete(product.get());
            return new Message(product.get().getName() + " was successfully removed from the database.",
                    LocalDateTime.now(), HttpStatus.OK.value());
        }
        else {
            throw new InvalidInputException("The product is not present in the database.");
        }
    }

    @Autowired
    private ProductInSaleRepository productInSaleRepository;

    @PostMapping(value = "/products/sale/add")
    public ProductInSale addProductToSale(@RequestBody ProductInSale productInSale, HttpSession session) throws BaseException{
        validateLoginAdmin(session);

        if(productRepository.findById(productInSale.getProductId()).isPresent()) {
            if(productInSale.getStartDate().compareTo(productInSale.getEndDate()) > 0) {
                throw new InvalidInputException("The start date can not be after the end date.");
            }
            productInSaleRepository.save(productInSale);
            return productInSale;
        }
        else {
            throw new InvalidInputException("There is no product with that id in the main table.");
        }
    }


    private void validateProductInput(Product product)throws BaseException {
        if(product.getName() == null || product.getCategory() == null || product.getPrice() < 0
                || product.getQuantity() < 0 || product.getManifacturer() == null
                || product.getDescription() == null || product.getPhoto() == null){
            throw new InvalidInputException("Invalid input for the product input.");
        }
    }
    private void validateProductByName(Product product) throws BaseException {
        if (getProductByName(product.getName()) != null) {
            throw new InvalidInputException("Product with that name already exists.");
        }
    }

    private Product getProductByName(String name) {
        Optional<Product> product = productRepository.findByName(name);
        if (product.isPresent()) {
            return product.get();
        }
        else {
            return null;
        }
    }
}
