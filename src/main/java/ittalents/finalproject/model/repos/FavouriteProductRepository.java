package ittalents.finalproject.model.repos;

import ittalents.finalproject.model.pojos.products.FavouriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteProductRepository extends JpaRepository<FavouriteProduct, Long> {

    @Query(value = "SELECT * FROM products_favourites f WHERE user_id LIKE :userId " +
            "AND product_id LIKE :productId", nativeQuery = true)
    Optional<FavouriteProduct> findByFavouriteProductPK(@Param("userId") long userId, @Param("productId") long productId);

    List<FavouriteProduct> getAllByFavouriteProductPK_UserId(long id);
}
