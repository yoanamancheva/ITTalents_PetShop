package ittalents.finalproject.model.repos;

import ittalents.finalproject.model.pojos.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>  {

//    Product findByProduct_name(String name);
}
