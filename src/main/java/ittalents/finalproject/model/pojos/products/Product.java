package ittalents.finalproject.model.pojos.products;


import ittalents.finalproject.model.pojos.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", nullable=false)
    private long id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private String manifacturer;
    private String description;
    private String photo;

    @OneToMany
//    @JoinColumn(name="id",referencedColumnName="productId")
    private Set<Review> reviews;


    public Product(long id, String name, String category, double price, int quantity, String manufacturer, String description, String photo) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.manifacturer = manufacturer;
        this.description = description;
        this.photo = photo;
    }
}
