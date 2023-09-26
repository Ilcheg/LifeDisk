package ru.netology.lifedisk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.lifedisk.entity.File;
import ru.netology.lifedisk.entity.User;
import ru.netology.lifedisk.exceptions.FileException;
import ru.netology.lifedisk.exceptions.InputDataException;
import ru.netology.lifedisk.exceptions.UnauthorizedException;
import ru.netology.lifedisk.repository.FileRepository;
import ru.netology.lifedisk.repository.UserRepository;
import ru.netology.lifedisk.repository.UserTokenRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final UserTokenRepository userTokenRepository;

    @Transactional(rollbackFor = Exception.class)
    public boolean uploadFile(String authToken, String fileName, String contentType, long size, byte[] bytes) {
        final User user = getUserByAuthToken(authToken);
        if (user == null) {
            log.error("Not authorized");
            throw new UnauthorizedException("Not authorized");
        }
        if (size == 0) {
            log.error("Empty files are permitted");
            throw new FileException("Empty files are permitted");
        }
        fileRepository.save(new File(LocalDateTime.now(), fileName, contentType, size, bytes, user));
        log.info("File added, user {}", user.getUsername());
        return true;
    }

    public List<File> getAllFileUser(String authToken, int limit) throws UnauthorizedException {
        final User user = getUserByAuthToken(authToken);
        if (user == null) {
            log.error("Not authorized");
            throw new UnauthorizedException("Not authorized");
        }
        return fileRepository.findAllByOwner(user);
    }

    @Transactional
    public void deleteFile(String authToken, String fileName) throws UnauthorizedException {
        final User user = getUserByAuthToken(authToken);
        if (user == null) {
            log.error("Not authorized");
            throw new UnauthorizedException("Not authorized");
        }
        fileRepository.deleteByOwnerAndFileName(user, fileName);
        log.info("File {} deleted. User {}", fileName, user.getUsername());
    }

    public byte[] downloadFile(String authToken, String fileName) throws UnauthorizedException {
        final User user = getUserByAuthToken(authToken);
        if (user == null) {
            log.error("Not authorized");
            throw new UnauthorizedException("Not authorized");
        }
        final File file = fileRepository.findByOwnerAndFileName(user, fileName);
        if (file == null) {
            log.error("Failed to download {}. User {}", fileName, user.getUsername());
            throw new InputDataException("Ошибка загрузки файла");
        }
        log.info("File {} download finished. User {}", file, user.getUsername());
        return file.getBytes();
    }

    @Transactional
    public void editFileName(String authToken, String fileName, String newFileName) throws UnauthorizedException {
        final User user = getUserByAuthToken(authToken);
        if (user == null) {
            log.error("Not authorized");
            throw new UnauthorizedException("Not authorized");
        }
        File file = fileRepository.findByOwnerAndFileName(user, fileName);
        file.setFileName(newFileName);
        fileRepository.save(file);
        log.info("File {} was successfully renamed to {}. User {}", fileName, newFileName, user.getUsername());
    }

    private User getUserByAuthToken(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            final String authTokenWithoutBearer = authToken.split(" ")[1];
            final String username = userTokenRepository.findByAuthToken(authTokenWithoutBearer).get().getLogin();
            return userRepository.findByUsername(username).get();
        }
        return null;
    }
}
