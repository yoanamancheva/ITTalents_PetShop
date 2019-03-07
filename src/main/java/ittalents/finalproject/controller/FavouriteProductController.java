package ittalents.finalproject.controller;


import ittalents.finalproject.model.pojos.User;
import ittalents.finalproject.model.pojos.products.FavouriteProduct;
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
import java.util.List;
import java.util.Optional;

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

        if(productRepository.findById(id).isPresent()) {
            FavouriteProduct favouriteProduct = new FavouriteProduct();
            FavouriteProduct.FavouriteProductPK  pk = new FavouriteProduct.FavouriteProductPK();

            pk.setUserId(user.getId());
            pk.setProductId(id);

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
    public List<FavouriteProduct> showUserFavourites(HttpSession session) throws BaseException{
        validateLogin(session);
        User user = (User)session.getAttribute(LOGGED_USER);
        return favouriteProductRepository.getAllByFavouriteProductPK_UserId(user.getId());
    }
}
