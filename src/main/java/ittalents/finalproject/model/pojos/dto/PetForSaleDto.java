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

    private long petId;
    private String gender;
    private String breed;
    private String subBreed;
    private int age;
    private String petDesc;
    private int quantity;
    private long discountId;
    private Timestamp startDate;
    private Timestamp endDate;
    private double discountPrice;
    private List<Photo> photos;
}
