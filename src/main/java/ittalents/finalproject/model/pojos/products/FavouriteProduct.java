package ittalents.finalproject.model.pojos.products;


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

        @Column (name = "user_id")
        protected Long userId;

        @Column (name = "product_id")
        protected Long productId;

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public FavouriteProductPK() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FavouriteProductPK that = (FavouriteProductPK) o;
            return Objects.equals(userId, that.userId) &&
                    Objects.equals(productId, that.productId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, productId);
        }
    }

}
