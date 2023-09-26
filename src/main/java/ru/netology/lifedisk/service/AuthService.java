package ru.netology.lifedisk.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import ru.netology.lifedisk.dto.JwtRequest;
import ru.netology.lifedisk.dto.JwtResponse;
import ru.netology.lifedisk.entity.UserToken;
import ru.netology.lifedisk.dto.ErrorDto;
import ru.netology.lifedisk.repository.UserTokenRepository;
import ru.netology.lifedisk.utils.JwtTokenUtils;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserTokenRepository userTokenRepository;

    public ResponseEntity<?> login(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorDto(HttpStatus.BAD_REQUEST.value(), "Wrong login and password!"), HttpStatus.BAD_REQUEST);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getLogin());
        String token = jwtTokenUtils.generateToken(userDetails);
        userTokenRepository.save(new UserToken(userDetails.getUsername(), token));
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @Transactional
    public void logout(String authToken) {
        final String authTokenWithoutBearer = authToken.split(" ")[1];
        userTokenRepository.deleteByAuthToken(authTokenWithoutBearer);
    }
}
