package application.service;

import application.model.JwtRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<JwtRequest, Long> {
    JwtRequest findByUsername(String username);

    Boolean existsByUsername(String username);

}
