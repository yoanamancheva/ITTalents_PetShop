package ittalents.finalproject.model.pojos.dto;

import ittalents.finalproject.model.pojos.pets.Photo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PetForSaleDto {

    private Long petId;
    private String gender;
    private String breed;
    private String subBreed;
    private Integer age;
    private String petDesc;
    private Integer quantity;
    private Long discountId;
    private Timestamp startDate;
    private Timestamp endDate;
    private Double discountPrice;
    private List<Photo> photos;
}
