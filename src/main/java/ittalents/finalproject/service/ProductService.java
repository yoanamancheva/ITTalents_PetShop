package ittalents.finalproject.service;

import ittalents.finalproject.exceptions.InvalidInputException;

import ittalents.finalproject.model.pojos.dto.ListProduct;
import ittalents.finalproject.model.pojos.dto.ListReview;
import ittalents.finalproject.model.pojos.dto.ProductReviewsDTO;
import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.model.repos.ProductRepository;
import ittalents.finalproject.model.repos.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;



    private List<ListReview> getReviewsForProduct(long id) {

        return this.reviewRepository.findAll().stream()
                .filter(review ->  review.getProduct().getId() == id)
                .map(review ->  new ListReview(review.getId(), review.getReview(), review.getRating(), review.getUser().getUsername()))
                .collect(Collectors.toList());
    }


    public ListProduct getAllInfoForProduct(long id) throws InvalidInputException {

        if(this.productRepository.findById(id).isPresent()) {
            Product p = this.productRepository.findById(id).get();
            List<ListReview> reviews = getReviewsForProduct(id);

            ListProduct productReviews = new ListProduct(p.getId(), p.getName(), p.getDescription());
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
