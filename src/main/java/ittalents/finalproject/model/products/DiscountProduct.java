package ittalents.finalproject.model.products;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@Setter
@Getter
public class DiscountProduct {

    private long id;
    private long productId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double discountPrice;

    public DiscountProduct(long productId, LocalDateTime startDate, LocalDateTime endDate, double discountPrice) {
        this.productId = productId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountPrice = discountPrice;
    }
}
