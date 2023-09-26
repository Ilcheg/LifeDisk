package ru.netology.lifedisk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    @JsonProperty("auth-token")
    private String token;
}
