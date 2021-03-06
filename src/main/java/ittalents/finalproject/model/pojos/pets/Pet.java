package ittalents.finalproject.model.pojos.pets;

//import javafx.util.converter.TimeStringConverter;
import lombok.*;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Pet {

    private Long id;
    private String gender;
    private String breed;
    private String subBreed;
    private Integer age;
    private Timestamp posted;
    private String petDesc;
    private Double price;
    private Integer quantity;

    public Pet(String gender, String breed, String subBreed, int age, Timestamp posted, String description, double price, int quantity) {
        this.gender = gender;
        this.breed = breed;
        this.age = age;
        this.subBreed = subBreed;
        this.petDesc = description;
        this.price = price;
        this.quantity = quantity;
        this.posted = posted;
    }


}
