package ru.neoflex.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.neoflex.product.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor {
    Optional<User> findByUserLogin(String login);
    Optional<User> findByUserLoginAndUserPsw(String login, String psw);
}
