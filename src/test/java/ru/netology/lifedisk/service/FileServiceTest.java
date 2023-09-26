package ru.netology.lifedisk.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.netology.lifedisk.exceptions.UnauthorizedException;
import ru.netology.lifedisk.repository.FileRepository;
import ru.netology.lifedisk.repository.UserRepository;
import ru.netology.lifedisk.repository.UserTokenRepository;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.lifedisk.TestDataFile.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FileServiceTest {
    @InjectMocks
    private FileService fileService;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserTokenRepository userTokenRepository;

    @BeforeEach
    void setUp() {
        Mockito.when(userTokenRepository.findByAuthToken(BEARER_TOKEN_SPLIT)).thenReturn(Optional.ofNullable(TOKEN_TEST_USER));
        Mockito.when(userRepository.findByUsername(TEST_NAME)).thenReturn(Optional.ofNullable(TEST_USER));
    }

    @Test
    void uploadFile() throws IOException {
        assertTrue(fileService.uploadFile(BEARER_TOKEN, TEST_FILENAME, TEST_MULTIPART_FILE.getContentType(), TEST_MULTIPART_FILE.getSize(), TEST_MULTIPART_FILE.getBytes()));
    }

    @Test
    void uploadFileUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> fileService.uploadFile(TEST_TOKEN, TEST_FILENAME, TEST_MULTIPART_FILE.getContentType(), TEST_MULTIPART_FILE.getSize(), TEST_MULTIPART_FILE.getBytes()));
    }

    @Test
    void downloadFile() {
        Mockito.when(fileRepository.findByOwnerAndFileName(TEST_USER, TEST_FILENAME)).thenReturn(TEST_STORAGE_FILE);
        assertEquals(TEST_FILE_CONTENT, fileService.downloadFile(BEARER_TOKEN, TEST_FILENAME));
    }

    @Test
    void downloadFileUnauthorized() {
        Mockito.when(fileRepository.findByOwnerAndFileName(TEST_USER, TEST_FILENAME)).thenReturn(TEST_STORAGE_FILE);
        assertThrows(UnauthorizedException.class, () -> fileService.downloadFile(TEST_TOKEN, TEST_FILENAME));
    }

    @Test
    void getAllFiles() {
        Mockito.when(fileRepository.findAllByOwner(TEST_USER)).thenReturn(TEST_FILE_LIST);
        assertEquals(TEST_FILE_LIST, fileService.getAllFileUser(BEARER_TOKEN, TEST_LIMIT));
    }

    @Test
    void deleteFile() {
        fileService.deleteFile(BEARER_TOKEN, TEST_FILENAME);
        Mockito.verify(fileRepository, Mockito.times(1)).deleteByOwnerAndFileName(TEST_USER, TEST_FILENAME);
    }

    @Test
    void deleteFileUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> fileService.deleteFile(TEST_TOKEN, TEST_FILENAME));
    }

    @Test
    void editFileNameUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> fileService.editFileName(TEST_TOKEN, TEST_FILENAME, TEST_NEW_FILENAME));
    }
}

