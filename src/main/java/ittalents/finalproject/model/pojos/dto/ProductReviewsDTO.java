package ittalents.finalproject.model.pojos.dto;


import ittalents.finalproject.model.pojos.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewsDTO {

    private long id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private String manufacturer;
    private Set<Review> reviews;


}
