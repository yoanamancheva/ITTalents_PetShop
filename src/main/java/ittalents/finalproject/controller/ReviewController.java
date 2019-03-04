package ittalents.finalproject.controller;


import ittalents.finalproject.exceptions.BaseException;
import ittalents.finalproject.exceptions.InvalidInputException;
import ittalents.finalproject.model.pojos.Review;
import ittalents.finalproject.model.pojos.dto.ListProduct;
import ittalents.finalproject.model.repos.ProductRepository;
import ittalents.finalproject.model.repos.ReviewRepository;
import ittalents.finalproject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class ReviewController extends BaseController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

//    @Autowired
//    private AddReviewRepository addReviewRepository;


    @GetMapping("/product/{id}/reviews")
    public ListProduct getAllInfoForProduct(@PathVariable long id) throws BaseException {
        return this.productService.getAllInfoForProduct(id);
    }

    @PostMapping("/product/{id}/reviews")
    public Review addReview(@PathVariable long id, @RequestBody Review review) throws BaseException{
        if(productRepository.findById(id).isPresent()) {
            return reviewRepository.save(review);
        }
        else {
            throw new InvalidInputException("Invalid request. No product with that id found.");
        }
    }

}
