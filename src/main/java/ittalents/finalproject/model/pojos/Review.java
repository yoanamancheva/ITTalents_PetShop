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
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    @Column(name = "product_id", nullable=false)
//    @Transient
//    private long productId;
    private long userId;
    private String review;

    @ManyToOne
//    @JoinColumn(name="productId",referencedColumnName="id", nullable = false)
    private Product product;

//    public Review(long productId, long userId, String review) {
//        this.productId = productId;
//        this.userId = userId;
//        this.review = review;
//    }
}
