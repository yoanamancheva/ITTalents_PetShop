package ittalents.finalproject.model.pojos.dto;

import ittalents.finalproject.model.pojos.products.Product;
import lombok.*;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShowFavouriteProductsDTO {

    @Id
    private long id;
    private String username;
    private List<Product> favouriteProducts;

    public ShowFavouriteProductsDTO(long id) {
        this.id = id;
    }

    public void addFavourites(List<Product> products) {
        this.favouriteProducts = new ArrayList<>();
        for (Product product : products) {
            this.favouriteProducts.add(product);
        }
    }
}
