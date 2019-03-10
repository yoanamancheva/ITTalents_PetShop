package ittalents.finalproject.model.pojos.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Id;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ListReview {

    @Id
    private Long id;
    private String review;
    private int rating;
    private String user;

    public ListReview(String review, int rating , String username) {
        this.review = review;
        this.rating = rating;
        this.user = username;
    }

}
