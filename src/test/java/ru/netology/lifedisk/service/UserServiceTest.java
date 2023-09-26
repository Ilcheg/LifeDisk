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
import ru.netology.lifedisk.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.lifedisk.TestDataFile.TEST_NAME;
import static ru.netology.lifedisk.TestDataFile.TEST_USER;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        Mockito.when(userRepository.findByUsername(TEST_NAME)).thenReturn(Optional.ofNullable(TEST_USER));
    }

    @Test
    void loadUserByUsername() {
        assertEquals(TEST_USER.getUsername(), userService.loadUserByUsername(TEST_NAME).getUsername());
    }
}
