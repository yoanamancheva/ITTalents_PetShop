package ittalents.finalproject.model.pets;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@Setter
@Getter
public class Pet {

    private enum GenderPet {
        F, M;
    }

    private long id;
    private GenderPet genderPet;
    private String breed;
    private String subBreed;
    private int age;
//    private LocalDateTime datePosted;
    private String description;
    private boolean inSale;
    private double price;

    public Pet(GenderPet genderPet, String breed, int age, String subBreed, String description, boolean inSale, double price) {
        this.genderPet = genderPet;
        this.breed = breed;
        this.age = age;
        this.subBreed = subBreed;
//        this.datePosted = datePosted;
        this.description = description;
        this.inSale = inSale;
        this.price = price;
    }
}
