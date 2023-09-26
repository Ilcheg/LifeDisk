package ru.netology.lifedisk;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.lifedisk.dto.JwtRequest;
import ru.netology.lifedisk.dto.JwtResponse;
import ru.netology.lifedisk.entity.File;
import ru.netology.lifedisk.entity.User;
import ru.netology.lifedisk.entity.UserToken;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestDataFile {
    public static final String TEST_TOKEN = "token";
    public static final String TEST_NAME = "testName";
    public static final String TEST_PASSWORD = "testPassword";

    public static User TEST_USER = new User(1L, TEST_NAME, TEST_PASSWORD, new ArrayList<>());
    public static UserToken TOKEN_TEST_USER = new UserToken(1l, TEST_NAME, "Bearer Token");
    public static final String BEARER_TOKEN = "Bearer Token";
    public static final String BEARER_TOKEN_SPLIT = BEARER_TOKEN.split(" ")[1];
    public static final String TEST_FILENAME = "TestFilename";
    public static final byte[] TEST_FILE_CONTENT = TEST_FILENAME.getBytes();
    public static final MultipartFile TEST_MULTIPART_FILE = new MockMultipartFile(TEST_FILENAME, TEST_FILE_CONTENT);
    public static final Long TEST_SIZE = 100L;
    public static final File TEST_STORAGE_FILE = new File(1L,TEST_FILENAME,TEST_SIZE,"text/plain",TEST_FILE_CONTENT, LocalDateTime.now(), TEST_USER);
    public static final List<File> TEST_FILE_LIST = List.of(TEST_STORAGE_FILE);
    public static final Integer TEST_LIMIT = 3;
    public static final String TEST_NEW_FILENAME = "newFileName";
    public static final JwtResponse JWT_RESPONSE = new JwtResponse(TEST_TOKEN);
    public static final JwtRequest JWT_REQUEST = new JwtRequest(TEST_NAME, TEST_PASSWORD);
    public static final UsernamePasswordAuthenticationToken USERNAME_PASSWORD_AUTHENTICATION_TOKEN = new UsernamePasswordAuthenticationToken(TEST_NAME, TEST_PASSWORD);

    public static UserDetails USER_DETAILS = new UserDetails() {
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public String getPassword() {
            return TEST_PASSWORD;
        }

        @Override
        public String getUsername() {
            return TEST_NAME;
        }

        @Override
        public boolean isAccountNonExpired() {
            return false;
        }

        @Override
        public boolean isAccountNonLocked() {
            return false;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }
    };
}
