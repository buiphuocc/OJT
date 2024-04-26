package com.group1.FresherAcademyManagementSystem.token;

import com.group1.FresherAcademyManagementSystem.models.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue
    private Long id;

    private String token;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private boolean expired;

    private boolean revoked;

}
