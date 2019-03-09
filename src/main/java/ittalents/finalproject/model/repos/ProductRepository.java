package ittalents.finalproject.model.repos;

import ittalents.finalproject.model.pojos.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>  {

    Optional<Product> findByName(String name);
    List<Product> findAllByCategoryOrderByPrice(String category);

    @Query(value = "SELECT * FROM products p WHERE p.name LIKE %:name%", nativeQuery = true)
    List<Product> search(@Param("name") String name);

    @Query(value = "SELECT * FROM products ORDER BY price ASC", nativeQuery = true)
    List<Product> sortByPrice();

    Optional<Product> findByPhoto(String photo);
}
