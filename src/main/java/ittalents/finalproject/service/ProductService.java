package ittalents.finalproject.service;

import ittalents.finalproject.controller.ImageController;
import ittalents.finalproject.model.pojos.products.ProductInSale;
import ittalents.finalproject.model.repos.ProductInSaleRepository;
import ittalents.finalproject.model.repos.UserRepository;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.InvalidInputException;

import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.model.repos.ProductRepository;
import ittalents.finalproject.model.repos.ReviewRepository;
import ittalents.finalproject.util.exceptions.ProductNotFoundException;
import ittalents.finalproject.util.mail.Notificator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ittalents.finalproject.util.mail.MailUtil.NEW_PROMOTIONS_PRODUCTS_CONTENT;
import static ittalents.finalproject.util.mail.MailUtil.NEW_PROMOTIONS_SUBJECT;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ImageController imageController;

    @Autowired
    private ProductInSaleRepository productInSaleRepository;

    @Autowired
    private Notificator notificator;

    @Autowired
    private UserRepository userRepository;


//    sets photo to product
    public Product setProductPhoto(long id,  MultipartFile img, HttpSession session) throws BaseException, IOException {
        Optional<Product> product = productRepository.findById(id);
        if(!img.isEmpty() && id > 0 && product.isPresent()) {
            String imgTitle = imageController.uploadImage(img, session);
            product.get().setPhoto(imgTitle);
            productRepository.save(product.get());
            return product.get();
        }
        else {
            throw new ProductNotFoundException("No product found with that id. Cannot set image.");
        }
    }

//    gets product's photo
    public byte[] getProductImage(long id) throws BaseException, IOException{
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()) {
            String image = product.get().getPhoto();
            if(!image.equals("no photo")) {
                return imageController.downloadImage(image);
            }
            else {
                throw new ProductNotFoundException("No photo for this product.");
            }
        }
        else {
            throw new ProductNotFoundException("No product found with that id. Cannot show image.");
        }
    }

//    filter products

    public List<Product> filterProducts(String sortBy, Double minPrice, Double maxPrice, String category, String name) {
        return productRepository.findAll()
                .stream()
                .filter(product -> minPrice == null ||
                        product.getPrice() >= minPrice )
                .filter(product -> maxPrice == null ||
                        product.getPrice() <= maxPrice)
                .filter(product -> category == null ||
                        product.getCategory().equals(category))
                .filter(product -> name == null ||
                        product.getName().contains(name))
                .sorted((c1, c2) -> {
                    if(sortBy == null) return 1;
                    switch (sortBy) {
                        case "category" : return c1.getCategory().compareTo(c2.getCategory());
                        case "description" : return c1.getDescription().compareTo(c2.getDescription());
                        case "manufacturer" : return c1.getManifacturer().compareTo(c2.getManifacturer());
                        case "name" : return c1.getName().compareTo(c2.getName());
                        case "price" : return Double.compare(c1.getPrice(),c2.getPrice());
                        default: return 1;
                    }
                })
                .map(product -> new Product(product.getId(),
                        product.getName(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getManifacturer(),
                        product.getDescription(),
                        product.getPhoto()))
                .collect(Collectors.toList());
    }


// returns all products that contain given string
    public List<Product> getProductsByNameContains(String name) throws BaseException{
        List<Product> products = productRepository.search(name);
        if(products.isEmpty()) {
            throw new ProductNotFoundException("No products found containing that name.");
        }
        return products;
    }

// returns all products sorted by price ascending
    public List<Product> sortByPriceAsc() throws BaseException {
        List<Product> products = productRepository.sortByPrice();
        if(products.isEmpty()) {
            throw new ProductNotFoundException("No products found.");
        }
        return products;
    }

//    returns all products
    public List<Product> getAllProducts() throws BaseException{
        List<Product> allProducts = productRepository.findAll();
        if(allProducts.isEmpty()) {
            throw new ProductNotFoundException("No products found.");
        }
        return allProducts;
    }


//    returns product by given id
    public Product getProductById(long id) throws BaseException{
        Optional<Product> obj = productRepository.findById(id);
        if(obj.isPresent()) {
            return obj.get();
        }
        else {
            throw new ProductNotFoundException("Product not found with that id.");
        }
    }

// returns product by name if exists
    public Optional<Product> returnProductByName(String name) throws BaseException {
        Optional<Product> product = productRepository.findByName(name);
        if(product.isPresent()) {
            return product;
        }
        else {
            throw new ProductNotFoundException("Product not found with that name.");
        }
    }

// add product
    public Product addProduct(Product product) throws BaseException{
        validateProductInput(product);
        validateProductByName(product);
        productRepository.save(product);
        return product;
    }

//    update product specifications
    public Product updateProduct(Product product) throws BaseException{
        validateProductInput(product);
        if (!productRepository.findById(product.getId()).isPresent()) {
            throw new InvalidInputException("There is not product with this id in the database.");
        }
        productRepository.save(product);
        return product;
    }

// Products in sale ----------------------------------------------------------------------------------------------------

//    add existing product to sale
    public ProductInSale addProductIntoSale(ProductInSale productInSale) throws BaseException {
        if(productRepository.findById(productInSale.getProductId()).isPresent()) {
            if(productInSale.getStartDate().compareTo(productInSale.getEndDate()) > 0) {
                throw new InvalidInputException("The start date can not be after the end date.");
            }
            productInSaleRepository.save(productInSale);
            notificator.sendNews(NEW_PROMOTIONS_SUBJECT, NEW_PROMOTIONS_PRODUCTS_CONTENT);
            return productInSale;
        }
        else {
            throw new InvalidInputException("There is no product with that id in the main table.");
        }
    }



// validations-----------------------------------------------------------------------------------------
    private void validateProductInput(Product product)throws BaseException {
        if(product.getName() == null || product.getCategory() == null || product.getPrice() < 0
                || product.getQuantity() < 0 || product.getManifacturer() == null
                || product.getDescription() == null ){
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
    //!!!!
//    public List<ListProduct> getAllproducts(){
//        return this.productRepository.findAll().stream()
//                .filter(product -> product.getProductId() != null)
//                .map(product -> new ListProduct(product.getProductId(),
//                        product.getTitle(),
//                        product.getInformation()))
//                .collect(Collectors.toList());
//    }



// mine - almost working
//    public List<ProductReviewsDTO> getReviewsForAllProducts() {
//        return productRepository.findAll().stream().map(product ->
//                new ProductReviewsDTO(product.getId(), product.getName(), product.getCategory(),
//                        product.getPrice(), product.getQuantity(),
//                        product.getManifacturer(), product.getReviews()))
//                .collect(Collectors.toList());
//    }
}
