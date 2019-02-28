package ittalents.finalproject.model.repos;

import ittalents.finalproject.model.pojos.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>  {

    Optional<Product> findByName(String name);
    List<Product> findAllByCategoryOrderByPrice(String category);
}
