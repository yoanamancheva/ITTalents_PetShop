package ittalents.finalproject.model.pojos.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class GetPetsWithPhotosDto {
    private long id;
    private String gender;
    private String breed;
    private String subBreed;
    private int age;
    private Timestamp posted;
    private String petDesc;
    private boolean inSale;
    private double price;
    private int quantity;
    private int photoId;
    private String photoPath;
}
