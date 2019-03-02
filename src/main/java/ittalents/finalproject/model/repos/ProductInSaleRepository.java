package ittalents.finalproject.model.repos;

import ittalents.finalproject.model.pojos.products.ProductInSale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ProductInSaleRepository extends JpaRepository<ProductInSale, Long> {
    ProductInSale findFirstByStartDate(LocalDateTime dateTime);
    ProductInSale findFirstByEndDate(LocalDateTime dateTime);
}
