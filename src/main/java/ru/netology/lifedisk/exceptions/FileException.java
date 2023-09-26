package ru.netology.lifedisk.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error input data")
public class FileException extends RuntimeException {
    public FileException(String message) {
        super(message);
    }
}
