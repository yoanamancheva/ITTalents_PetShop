package ittalents.finalproject.model.pojos.products;


import ittalents.finalproject.model.pojos.User;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "products_favourites")
public class FavouriteProduct implements Serializable {

    @EmbeddedId
    private FavouriteProductPK favouriteProductPK;


    @ToString
    @AllArgsConstructor
    @Getter
    @Setter
    @Embeddable
    public static class FavouriteProductPK implements Serializable {

        @ManyToOne
        @JoinColumn (name = "user_id")
        protected User user;

        @ManyToOne
        @JoinColumn (name = "product_id")
        protected Product product;

        public void setUser(User user) {
            this.user = user;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public FavouriteProductPK() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FavouriteProductPK that = (FavouriteProductPK) o;
            return Objects.equals(user, that.user) &&
                    Objects.equals(product, that.product);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, product);
        }
    }

}
