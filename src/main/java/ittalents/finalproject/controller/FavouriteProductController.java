package ittalents.finalproject.controller;


import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.User;
import ittalents.finalproject.model.pojos.dto.ShowFavouriteProductsDTO;
import ittalents.finalproject.model.pojos.products.FavouriteProduct;
import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.model.repos.FavouriteProductRepository;
import ittalents.finalproject.model.repos.ProductRepository;
import ittalents.finalproject.model.repos.UserRepository;
import ittalents.finalproject.service.FavouriteProductService;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.toIntExact;

@RestController
public class FavouriteProductController extends BaseController{

    @Autowired
    private FavouriteProductService favouriteProductService;

//    add product to user's favourites
    @PostMapping(value = "products/favourites/add")
    public Object addProductToFavourite(@RequestParam("id") long id, HttpSession session) throws BaseException {
        validateLogin(session);
        User user = (User)session.getAttribute(LOGGED_USER);
        return favouriteProductService.addProductToFavourites(id, user);
    }

//  remove product from user's favourites
    @PostMapping(value = "products/favourites/remove")
    public Message removeProductFromFavourite(@RequestParam("id") long id, HttpSession session) throws BaseException{
        validateLogin(session);
        User user = (User)session.getAttribute(LOGGED_USER);
        return favouriteProductService.removeProductFromFavourites(id, user);
    }

//  get user's favourite products
    @GetMapping(value = "products/favourites")
    public ShowFavouriteProductsDTO showUserFavourites(HttpSession session) throws BaseException{
        validateLogin(session);
        User user = (User)session.getAttribute(LOGGED_USER);
        return favouriteProductService.getUsersFavouriteProducts(user);
    }
}
