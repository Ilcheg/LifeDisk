package ru.netology.lifedisk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.lifedisk.entity.UserToken;

import java.util.Optional;

@Repository
@Transactional
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByAuthToken(String authToken);

    void deleteByAuthToken(String authToken);
}
