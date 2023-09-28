package clearsolutions.com.javatestassignment.repository;

import clearsolutions.com.javatestassignment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
