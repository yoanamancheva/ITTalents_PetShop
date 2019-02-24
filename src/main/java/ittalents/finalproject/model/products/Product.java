package ittalents.finalproject.model.products;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class Product {

    private long id;
    private String name;
    private String category;
    private double price;
//    private int quantity;
    private String manufacturer;
    private String description;
    private String photo;

    public Product(long id, String name, String category, double price, String manufacturer, String description, String photo) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
//        this.quantity = quantity;
        this.manufacturer = manufacturer;
        this.description = description;
        this.photo = photo;
    }
}
