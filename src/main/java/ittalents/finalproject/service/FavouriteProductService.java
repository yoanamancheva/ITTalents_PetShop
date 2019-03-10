package ittalents.finalproject.service;

import ittalents.finalproject.model.pojos.Message;
import ittalents.finalproject.model.pojos.User;
import ittalents.finalproject.model.pojos.dto.ShowFavouriteProductsDTO;
import ittalents.finalproject.model.pojos.products.FavouriteProduct;
import ittalents.finalproject.model.pojos.products.Product;
import ittalents.finalproject.model.repos.FavouriteProductRepository;
import ittalents.finalproject.model.repos.ProductRepository;
import ittalents.finalproject.util.exceptions.BaseException;
import ittalents.finalproject.util.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavouriteProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FavouriteProductRepository favouriteProductRepository;

//    add product to user's favourites
    public Object addProductToFavourites(long id, User user) throws BaseException {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()) {
            FavouriteProduct favouriteProduct = new FavouriteProduct();
            FavouriteProduct.FavouriteProductPK  pk = new FavouriteProduct.FavouriteProductPK();
            pk.setUser(user);
            pk.setProduct(product.get());
            favouriteProduct.setFavouriteProductPK(pk);
            favouriteProductRepository.save(favouriteProduct);
            return new Message("Product with id " + id + " was successfully added to user's favourites.",
                                LocalDateTime.now(), HttpStatus.OK.value());
        }
        else {
            throw new ProductNotFoundException("No product with that id found. You cannot add it to favourites.");
        }
    }

//    remove product from favourites
    public Message removeProductFromFavourites(long id, User user) throws BaseException {
        if(productRepository.findById(id).isPresent()) {
            Optional<FavouriteProduct> product = favouriteProductRepository.findByFavouriteProductPK(user.getId(), id);
            if(product.isPresent()) {
                favouriteProductRepository.delete(product.get());
                return new Message("Product with id " + id + " was successfully removed from favourites.",
                        LocalDateTime.now(), HttpStatus.OK.value());
            } else {
                throw  new ProductNotFoundException("No product with that id found in user's favourites.");
            }
        }
        else {
            throw new ProductNotFoundException("No product found with that id this user's favourites.");
        }
    }

//    show user's favourite products + info for them
    public ShowFavouriteProductsDTO getUsersFavouriteProducts(User user) {
        ShowFavouriteProductsDTO userFavourites = new ShowFavouriteProductsDTO(user.getId());
        userFavourites.setUsername(user.getUsername());
        List<FavouriteProduct> favouriteProducts = favouriteProductRepository.getAllByFavouriteProductPK_User(user);
        List<Product> products = new ArrayList<>();

        for (FavouriteProduct favouriteProduct : favouriteProducts) {
            Optional<Product> product = productRepository.findById(favouriteProduct
                                                         .getFavouriteProductPK().getProduct().getId());
            if(product.isPresent()) {
                products.add(product.get());
            }
        }
        userFavourites.addFavourites(products);
        return userFavourites;
    }
}
