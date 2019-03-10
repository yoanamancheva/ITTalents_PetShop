package ittalents.finalproject.model.repos;

import ittalents.finalproject.model.pojos.Review;
import ittalents.finalproject.model.pojos.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUser(User user);
}
