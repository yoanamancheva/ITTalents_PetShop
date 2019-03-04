package ittalents.finalproject.controller;


import ittalents.finalproject.exceptions.InvalidInputException;
import ittalents.finalproject.model.pojos.Review;
import ittalents.finalproject.model.pojos.dto.ListProduct;
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


    @GetMapping("/info/{id}")
    public ListProduct getAllInfoForProduct(@PathVariable long id) throws InvalidInputException {
        return this.productService.getAllInfoForProduct(id);
    }
}
