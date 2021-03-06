package ittalents.finalproject.model.pojos.products;


import ittalents.finalproject.controller.ProductController;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "products_in_sale")
public class ProductInSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double discountPrice;
    }
