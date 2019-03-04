package ittalents.finalproject.model.pojos.dto;


import ittalents.finalproject.model.pojos.User;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ListProduct {

    @Id
    private long id;
    private String productName;
    private String description;
    private List<ListReview> reviews;

    public ListProduct(long id, String productName, String description) {
        this.id = id;
        this.productName = productName;
        this.description = description;
    }

    public void addReviews(List<ListReview> reviews) {
        this.reviews = new ArrayList<>();
        for (ListReview listReview : reviews) {
            this.reviews.add(listReview);
        }
    }
}
