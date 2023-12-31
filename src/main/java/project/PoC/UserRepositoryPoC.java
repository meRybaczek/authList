package project.PoC;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryPoC extends JpaRepository <User, Long> {
}
