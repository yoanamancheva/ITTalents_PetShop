package ittalents.finalproject.model.pojos;


import ittalents.finalproject.model.pojos.products.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
@Table(name = "reviews")
public class Review{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String review;
    private int rating;

    public Review(Product product, User user, String review, int rating) {
        super();
        this.product = product;
        this.user = user;
        this.review = review;
        this.rating = rating;
    }
}
