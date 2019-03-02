package ittalents.finalproject.model.repos;

import ittalents.finalproject.model.pojos.products.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {
    OrderedProduct findFirstByQuantity(int quantity);
}
