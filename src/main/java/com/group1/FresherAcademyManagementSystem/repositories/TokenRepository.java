package com.group1.FresherAcademyManagementSystem.repositories;

import com.group1.FresherAcademyManagementSystem.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t from Token t inner join UserEntity u on t.user.id=u.id " +
            "where u.id= :userId and (t.expired =false or t.revoked =false)")
    List<Token> findAllValidTokensByUser(Long userId);

    Optional<Token> findByToken(String token);

    Optional<Token> findByRefreshToken(String refreshToken);
}
