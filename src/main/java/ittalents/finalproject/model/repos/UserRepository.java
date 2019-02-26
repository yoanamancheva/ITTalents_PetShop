package ittalents.finalproject.model.repos;

import ittalents.finalproject.model.pojos.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
