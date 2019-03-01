package ittalents.finalproject.model.pojos.products;


import ittalents.finalproject.controller.ProductController;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "products_in_sale")
public class ProductInSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long productId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double discountPrice;
    }
