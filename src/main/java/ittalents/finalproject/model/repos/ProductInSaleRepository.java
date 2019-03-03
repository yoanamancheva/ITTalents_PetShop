package ittalents.finalproject.model.repos;

import ittalents.finalproject.model.pojos.products.ProductInSale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductInSaleRepository extends JpaRepository<ProductInSale, Long> {
    List<ProductInSale> findByProductId(long id);

    ProductInSale findFirstByStartDate(LocalDateTime dateTime);
    ProductInSale findFirstByEndDate(LocalDateTime dateTime);
}
