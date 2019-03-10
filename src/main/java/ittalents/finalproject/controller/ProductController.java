package ittalents.finalproject.controller;


import com.mysql.cj.xdevapi.SessionFactory;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.InvalidInputException;
import ittalents.finalproject.util.exceptions.ProductNotFoundException;
import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.model.pojos.products.ProductInSale;
import ittalents.finalproject.model.repos.ProductInSaleRepository;
import ittalents.finalproject.model.repos.ProductRepository;
import ittalents.finalproject.service.ProductService;
import ittalents.finalproject.util.mail.Notificator;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ittalents.finalproject.util.mail.MailUtil.NEW_PROMOTIONS_PRODUCTS_CONTENT;
import static ittalents.finalproject.util.mail.MailUtil.NEW_PROMOTIONS_SUBJECT;

@RestController
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;


    @Autowired
    private ProductRepository productRepository;


    @PostMapping(value = "products/{id}/add/photo")
    public Product addPhotoToProduct(@PathVariable("id") long id,
                                     @RequestParam MultipartFile img,
                                     HttpSession session) throws BaseException, IOException {
        validateLoginAdmin(session);
       return productService.setProductPhoto(id, img, session);
    }

    @GetMapping(value = "products/{id}/photo", produces = "image/png")
    public @ResponseBody byte[] showProductImage(@PathVariable("id") long id) throws IOException, BaseException{
        return productService.getProductImage(id);
    }

    @GetMapping(value = "products/filter")
    public List<Product> getAllProductsFiltered(@RequestParam(name = "sortBy", required = false) String sortBy,
                                                @RequestParam(name = "minPrice", required = false) Double minPrice,
                                                @RequestParam(name = "maxPrice", required = false) Double maxPrice,
                                                @RequestParam(name = "category", required = false) String category,
                                                @RequestParam(name = "name", required = false) String name) {

        return productService.filterProducts(sortBy, minPrice, maxPrice, category, name);
    }


    @GetMapping(value = "/products/search/{name}")
    public List<Product> findProducts(@PathVariable("name") String name) throws BaseException{
        return productService.getProductsByNameContains(name);
    }

    @GetMapping(value = "/products/sort/price")
    public List<Product> sortByPrice() throws BaseException{
        return productService.sortByPriceAsc();
    }

    @GetMapping(value = "/products")
    public List<Product> getAll(HttpSession session) throws BaseException {
        validateLogin(session);
        return productService.getAllProducts();
    }

    @GetMapping(value = "/products/{id}")
    public Product getById(@PathVariable("id") long id, HttpSession session) throws BaseException{
        validateLogin(session);
        return productService.getProductById(id);
    }

//    done
    @PostMapping(value = "/products/byName")
    public Optional<Product> showProductByName(@RequestParam("name") String name) throws BaseException{
        return productService.returnProductByName(name);
    }

// not used
//    @PostMapping(value = "/products/filter/category")
//    public List<Product> filterByPrice(@RequestParam("category") String category) throws BaseException {
//        List<Product> products = productRepository.findAllByCategoryOrderByPrice(category);
//        if(products.isEmpty()) {
//            throw new ProductNotFoundException("No products found out of that category.");
//        }
//        return products;
//    }


    @PostMapping(value = "/products/add")
    public Product add(@RequestBody Product product, HttpSession session) throws BaseException {
        validateLoginAdmin(session);
        return productService.addProduct(product);
    }

    @PutMapping(value = "/products/update")
    public Product update(@RequestBody Product product, HttpSession session) throws BaseException{
        validateLoginAdmin(session);
        return productService.updateProduct(product);
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

    @PostMapping(value = "/products/sale/add")
    public ProductInSale addProductToSale(@RequestBody ProductInSale productInSale, HttpSession session) throws BaseException{
        validateLoginAdmin(session);
        return productService.addProductIntoSale(productInSale);
    }
}