package ru.netology.lifedisk.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "users_token")
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String authToken;

    public UserToken(String login, String token) {
        this.login = login;
        this.authToken = token;
    }
}
