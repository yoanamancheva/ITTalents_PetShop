package ittalents.finalproject.service;

import ittalents.finalproject.controller.ProductController;
import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.Review;
import ittalents.finalproject.model.pojos.User;
import ittalents.finalproject.model.pojos.dto.AddReviewDTO;
import ittalents.finalproject.model.pojos.dto.ListProduct;
import ittalents.finalproject.model.pojos.dto.ListReview;
import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.model.pojos.products.ProductInSale;
import ittalents.finalproject.model.repos.ProductRepository;
import ittalents.finalproject.model.repos.ReviewRepository;
import ittalents.finalproject.model.repos.UserRepository;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductController productController;


    //    add review to a product
    public ListReview addReviewToProduct(AddReviewDTO addReviewDTO) throws BaseException {

        Optional<Product> product = productRepository.findById(addReviewDTO.getProductId());
        Optional<User> user = userRepository.findById(addReviewDTO.getUserId());

        if(product.isPresent() && user.isPresent()) {
            if(addReviewDTO.getRating() > 0 && addReviewDTO.getRating() < 6) {
                Review review = new Review(product.get(), user.get(), addReviewDTO.getReview(), addReviewDTO.getRating());
                reviewRepository.save(review);
                return new ListReview(addReviewDTO.getReview(), addReviewDTO.getRating(), user.get().getUsername());
            }
            else {
                throw new InvalidInputException("Rating must be between 1 and 5.");
            }
        }
        else {
            throw new InvalidInputException("Invalid request. No product/user with that id found.");
        }
    }


    //    remove review from product
    public Message removeReviewFromProduct(User user, long id) throws BaseException {
        Optional<Review> review = reviewRepository.findById(id);

        if(review.isPresent()) {
            if(review.get().getUser().getId() == user.getId()) {
                reviewRepository.deleteById(id);
                return new Message("Review with id " + id + " was deleted successfully.", LocalDateTime.now(),
                        HttpStatus.OK.value());
            }
            else {
                throw new InvalidInputException("Can not delete other user's review.");
            }
        }
        else {
            throw new InvalidInputException("No review found with that id.");
        }
    }

    //returns dto product with it's reviews
    public ListProduct getAllInfoForProduct(long id, HttpSession session) throws BaseException {
        ProductInSale productInSale = productController.getProductInSaleByProductId(id, session);
        Optional<Product> product = productRepository.findById(id);

        if(product.isPresent()) {
            if( productInSale != null) {
                product.get().setPrice(productInSale.getDiscountPrice());
            }
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

    private List<ListReview> getReviewsForProduct(long id) {
        return this.reviewRepository.findAll().stream()
                .filter(review ->  review.getProduct().getId() == id)
                .map(review ->  new ListReview(review.getId(), review.getReview(), review.getRating(), review.getUser().getUsername()))
                .collect(Collectors.toList());
    }
}
