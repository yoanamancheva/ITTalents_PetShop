package ittalents.finalproject.model.pojos.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
}
