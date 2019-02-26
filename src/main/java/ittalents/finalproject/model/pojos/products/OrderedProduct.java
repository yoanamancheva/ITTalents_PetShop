package ittalents.finalproject.model.pojos.products;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class OrderedProduct {

    private long productId;
    private long userId;

    public OrderedProduct(long productId, long userId) {
        this.productId = productId;
        this.userId = userId;
    }
}
