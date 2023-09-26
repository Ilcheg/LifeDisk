package ru.netology.lifedisk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.lifedisk.dto.FileRequest;
import ru.netology.lifedisk.dto.FileResponse;
import ru.netology.lifedisk.exceptions.UnauthorizedException;
import ru.netology.lifedisk.service.FileService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class FileController {
    private final FileService fileService;

    @GetMapping("/list")
    public List<FileResponse> getAllFiles(@RequestHeader("auth-token") String authToken,
                                          @RequestParam("limit") int limit) throws UnauthorizedException {
        return fileService.getAllFileUser(authToken, limit).stream().limit(limit).map(o ->
                new FileResponse(o.getFileName(), o.getUploadDate().toString(), o.getSize())).collect(Collectors.toList());
    }

    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestHeader("auth-token") String authToken,
                                        @RequestParam("filename") String fileName, MultipartFile file) throws IOException {
        String type = file.getContentType();
        long size = file.getSize();
        byte[] bytes = file.getBytes();
        fileService.uploadFile(authToken, fileName, type, size, bytes);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestHeader("auth-token") String authToken,
                                        @RequestParam("filename") String fileName) throws UnauthorizedException {

        fileService.deleteFile(authToken, fileName);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestHeader("auth-token") String authToken,
                                                 @RequestParam("filename") String fileName) throws UnauthorizedException {
        byte[] file = fileService.downloadFile(authToken, fileName);
        return ResponseEntity.ok().body(new ByteArrayResource(file));
    }

    @PutMapping("/file")
    public ResponseEntity<?> editFileName(@RequestHeader("auth-Token") String authToken,
                                          @RequestParam("filename") String fileName,
                                          @RequestBody FileRequest fileRequest) throws UnauthorizedException {
        String newFileName = fileRequest.getFileName();
        fileService.editFileName(authToken, fileName, newFileName);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
