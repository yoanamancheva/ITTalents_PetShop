package ittalents.finalproject.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class Review {
    private long productId;
    private long userId;
    private String review;

    public Review(long productId, long userId, String review) {
        this.productId = productId;
        this.userId = userId;
        this.review = review;
    }
}
