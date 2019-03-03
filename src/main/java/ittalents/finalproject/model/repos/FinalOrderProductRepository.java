package ittalents.finalproject.model.repos;

import ittalents.finalproject.model.pojos.orders.FinalOrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinalOrderProductRepository extends JpaRepository<FinalOrderProducts, Long> {

}
