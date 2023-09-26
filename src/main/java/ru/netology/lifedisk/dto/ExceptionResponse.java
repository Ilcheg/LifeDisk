package ru.netology.lifedisk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponse {
    private String message;
    private Integer id;
}
