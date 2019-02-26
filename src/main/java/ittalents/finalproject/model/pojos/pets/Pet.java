package ittalents.finalproject.model.pojos.pets;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Setter
@Getter
public class Pet {

    private long id;
    private String genderPet;
    private String breed;
    private String subBreed;
    private int age;
    private LocalDateTime datePosted;
    private String description;
    private boolean inSale;
    private double price;
    private int quantity;

    public Pet(String genderPet, String breed, int age, String subBreed, String description, boolean inSale, double price, int quantity) {
        this.genderPet = genderPet;
        this.breed = breed;
        this.age = age;
        this.subBreed = subBreed;
        this.description = description;
        this.inSale = inSale;
        this.price = price;
        this.quantity = quantity;
    }
}
