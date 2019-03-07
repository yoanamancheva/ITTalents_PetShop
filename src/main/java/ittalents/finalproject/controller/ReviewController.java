package ittalents.finalproject.controller;


import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.InvalidInputException;
import ittalents.finalproject.model.pojos.Review;
import ittalents.finalproject.model.pojos.User;
import ittalents.finalproject.model.pojos.dto.AddReviewDTO;
import ittalents.finalproject.model.pojos.dto.ListProduct;
import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.model.repos.ProductRepository;
import ittalents.finalproject.model.repos.ReviewRepository;
import ittalents.finalproject.model.repos.UserRepository;
import ittalents.finalproject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
public class ReviewController extends BaseController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/product/{id}/reviews")
    public ListProduct getAllInfoForProduct(@PathVariable long id, HttpSession session) throws BaseException {
        return this.productService.getAllInfoForProduct(id, session);
    }

    @PostMapping("/product/reviews")
    public Review addReview(@RequestBody AddReviewDTO addReviewDTO) throws BaseException{

        Optional<Product> product = productRepository.findById(addReviewDTO.getProductId());
        Optional<User> user = userRepository.findById(addReviewDTO.getUserId());

        if(product.isPresent() && user.isPresent()) {
            Review review = new Review(product.get(), user.get(), addReviewDTO.getReview(), addReviewDTO.getRating());
            return reviewRepository.save(review);
        }
        else {
            throw new InvalidInputException("Invalid request. No product/user with that id found.");
        }
    }

}
