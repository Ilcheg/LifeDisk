package ru.netology.lifedisk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileResponse {
    private String filename;
    private String editedAt;
    private Long size;
}
