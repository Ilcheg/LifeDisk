package ru.netology.lifedisk.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import ru.netology.lifedisk.repository.UserTokenRepository;
import ru.netology.lifedisk.utils.JwtTokenUtils;

import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.lifedisk.TestDataFile.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private JwtTokenUtils jwtTokenUtils;
    @InjectMocks
    private AuthService authService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserTokenRepository userTokenRepository;

    @Test
    void login() {
        Mockito.when(userService.loadUserByUsername(TEST_NAME)).thenReturn(USER_DETAILS);
        Mockito.when(jwtTokenUtils.generateToken(USER_DETAILS)).thenReturn(TEST_TOKEN);
        assertEquals(ResponseEntity.ok(JWT_RESPONSE), authService.login(JWT_REQUEST));
        Mockito.verify(authenticationManager, Mockito.times(1)).authenticate(USERNAME_PASSWORD_AUTHENTICATION_TOKEN);
    }
}