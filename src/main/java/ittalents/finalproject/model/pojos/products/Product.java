package ittalents.finalproject.model.pojos.products;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String product_name;
    private String category;
    private double price;
    private int quantity;
    private String manifacturer;
    private String description;
    private String photo_path;

    public Product(long id, String name, String category, double price, int quantity, String manufacturer, String description, String photo) {
        this.id = id;
        this.product_name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.manifacturer = manufacturer;
        this.description = description;
        this.photo_path = photo;
    }
}
