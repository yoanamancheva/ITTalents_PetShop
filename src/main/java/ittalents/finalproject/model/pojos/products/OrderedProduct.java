package ittalents.finalproject.model.pojos.products;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "products_in_order")
public class OrderedProduct implements Serializable{


    @EmbeddedId
    private OrderedProductPk orderedProductPk;
    @Column(name = "quantity")
    private int quantity;

    @Transient
    @JsonIgnore
    private long midProductId;
    @Transient
    @JsonIgnore
    private long midOrderId;


    @ToString
    @AllArgsConstructor
    @Getter
    @Setter
    @Embeddable
    public static class OrderedProductPk implements Serializable {

        @Column (name = "order_id")
        protected Long orderId;

        @Column(name = "product_id")
        protected Long productId;

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public OrderedProductPk() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderedProductPk that = (OrderedProductPk) o;
            return Objects.equals(orderId, that.orderId) &&
                    Objects.equals(productId, that.productId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(orderId, productId);
        }
    }
}
