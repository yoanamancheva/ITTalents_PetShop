package ittalents.finalproject.controller;


import ittalents.finalproject.model.pojos.User;
import ittalents.finalproject.model.pojos.dto.ShowFavouriteProductsDTO;
import ittalents.finalproject.model.pojos.products.FavouriteProduct;
import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.model.repos.FavouriteProductRepository;
import ittalents.finalproject.model.repos.ProductRepository;
import ittalents.finalproject.model.repos.UserRepository;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.toIntExact;

@RestController
public class FavouriteProductController extends BaseController{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FavouriteProductRepository favouriteProductRepository;

    @PostMapping(value = "products/favourites/add")
    public void addProductToFavourite(@RequestParam("id") long id, HttpSession session) throws BaseException {
        validateLogin(session);
        User user = (User)session.getAttribute(LOGGED_USER);
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()) {
            FavouriteProduct favouriteProduct = new FavouriteProduct();
            FavouriteProduct.FavouriteProductPK  pk = new FavouriteProduct.FavouriteProductPK();

            pk.setUser(user);
            pk.setProduct(product.get());

            favouriteProduct.setFavouriteProductPK(pk);
            favouriteProductRepository.save(favouriteProduct);
        }
        else {
            throw new ProductNotFoundException("No product with that id found. You cannot add it to favourites.");
        }
    }

    @PostMapping(value = "products/favourites/remove")
    public void removeProductFromFavourite(@RequestParam("id") long id, HttpSession session) throws BaseException{
        validateLogin(session);
        User user = (User)session.getAttribute(LOGGED_USER);

        if(productRepository.findById(id).isPresent()) {
            Optional<FavouriteProduct> product = favouriteProductRepository.findByFavouriteProductPK(user.getId(), id);
            if(product.isPresent()) {
                favouriteProductRepository.delete(product.get());
            }
        }
        else {
            throw new ProductNotFoundException("No product found with that id this user's favourites.");
        }
    }

    @GetMapping(value = "products/favourites")
    public ShowFavouriteProductsDTO showUserFavourites(HttpSession session) throws BaseException{
        validateLogin(session);
        User user = (User)session.getAttribute(LOGGED_USER);
        ShowFavouriteProductsDTO userFavourites = new ShowFavouriteProductsDTO(user.getId());

        userFavourites.setUsername(user.getUsername());

        List<FavouriteProduct> favouriteProducts = favouriteProductRepository.getAllByFavouriteProductPK_User(user);
        List<Product> products = new ArrayList<>();


        for (FavouriteProduct favouriteProduct : favouriteProducts) {

            Optional<Product> product = productRepository.findById(favouriteProduct.getFavouriteProductPK().getProduct().getId());

            if(product.isPresent()) {
                products.add(product.get());
            }
        }

        userFavourites.addFavourites(products);
        return userFavourites;
    }
}
