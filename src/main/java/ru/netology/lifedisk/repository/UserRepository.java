package ru.netology.lifedisk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.lifedisk.entity.User;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
