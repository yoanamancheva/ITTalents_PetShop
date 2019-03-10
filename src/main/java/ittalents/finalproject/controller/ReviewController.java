package ittalents.finalproject.controller;


import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.dto.ListReview;
import ittalents.finalproject.service.ReviewService;
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
import javax.websocket.server.PathParam;
import java.util.Optional;

@RestController
public class ReviewController extends BaseController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

//    get product info + reviews for it
    @GetMapping("/product/{id}/reviews")
    public ListProduct getAllInfoForProduct(@PathVariable long id, HttpSession session) throws BaseException {
        return this.reviewService.getAllInfoForProduct(id, session);
    }

//    add review to a product
    @PostMapping("/product/reviews")
    public ListReview addReview(@RequestBody AddReviewDTO addReviewDTO, HttpSession session) throws BaseException{
        validateLogin(session);
        return reviewService.addReviewToProduct(addReviewDTO);
    }

//    delete review
    @DeleteMapping("/products/reviews/{id}")
    public Message removeProductReview(HttpSession session, @PathVariable("id") long id) throws BaseException {
        validateLogin(session);
        User user = (User)session.getAttribute(LOGGED_USER);
        return reviewService.removeReviewFromProduct(user, id);
    }

}
