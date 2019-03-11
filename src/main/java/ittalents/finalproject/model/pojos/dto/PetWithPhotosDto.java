package ittalents.finalproject.model.pojos.dto;

import ittalents.finalproject.model.pojos.pets.Photo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PetWithPhotosDto {
    private Long id;
    private String gender;
    private String breed;
    private String subBreed;
    private Integer age;
    private Timestamp posted;
    private String petDesc;
    private Double price;
    private Integer quantity;
    List<Photo> photos;

}
