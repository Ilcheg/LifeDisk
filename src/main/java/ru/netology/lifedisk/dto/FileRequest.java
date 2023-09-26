package ru.netology.lifedisk.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FileRequest {
    private String fileName;

    @JsonCreator
    public FileRequest(@JsonProperty("filename") String fileName) {
        this.fileName = fileName;
    }
}
