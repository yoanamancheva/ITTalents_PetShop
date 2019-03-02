package ittalents.finalproject.model.repos;

import ittalents.finalproject.model.pojos.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
