package ittalents.finalproject.model.pojos.products;


import ittalents.finalproject.model.pojos.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private String manifacturer;
    private String description;
    private String photo = "no photo";



    public Product(long id, String name, String category, double price, int quantity, String manufacturer,
                   String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.manifacturer = manufacturer;
        this.description = description;
        this.photo = "no photo";
    }

}
