package ittalents.finalproject.service;

import ittalents.finalproject.controller.ProductInSaleController;
import ittalents.finalproject.model.pojos.products.ProductInSale;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.InvalidInputException;

import ittalents.finalproject.model.pojos.dto.ListProduct;
import ittalents.finalproject.model.pojos.dto.ListReview;
import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.model.repos.ProductRepository;
import ittalents.finalproject.model.repos.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductInSaleController productInSaleController;



    private List<ListReview> getReviewsForProduct(long id) {

        return this.reviewRepository.findAll().stream()
                .filter(review ->  review.getProduct().getId() == id)
                .map(review ->  new ListReview(review.getId(), review.getReview(), review.getRating(), review.getUser().getUsername()))
                .collect(Collectors.toList());
    }


    public ListProduct getAllInfoForProduct(long id, HttpSession session) throws BaseException {
        ProductInSale productInSale = productInSaleController.getProductInSaleByProductId(id, session);
        Optional<Product> product = productRepository.findById(id);

            if(product.isPresent()) {
                if( productInSale != null) {
                    product.get().setPrice(productInSale.getDiscountPrice());
                }

//                Product p = this.productRepository.findById(id).get();
                List<ListReview> reviews = getReviewsForProduct(id);

                ListProduct productReviews = new ListProduct(product.get().getId(), product.get().getName(),
                                                             product.get().getDescription(), product.get().getPrice());
                productReviews.addReviews(reviews);

                return productReviews;

        }
        else {
            throw new InvalidInputException("No product found with that id.");
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
