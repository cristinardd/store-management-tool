package storemanagementtool.store.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import storemanagementtool.store.dto.AuthenticationRequest;
import storemanagementtool.store.dto.AuthenticationResponse;
import storemanagementtool.store.dto.RegisterRequest;
import storemanagementtool.store.model.Role;
import storemanagementtool.store.model.User;
import storemanagementtool.store.repository.UserRepository;
import storemanagementtool.store.security.JwtService;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Captor
    private ArgumentCaptor<User> userCaptor;
    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void givenValidUser_whenRegister_thenReturnsValidToken() {
        RegisterRequest request = new RegisterRequest("user", "123", Role.USER);
        String encodedPassword = "encodedPassword";
        User user = buildUserRegister(request, encodedPassword);
        String expectedToken = "token";

        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn(expectedToken);

        AuthenticationResponse response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals(expectedToken, response.getAccessToken());

        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertEquals(encodedPassword, capturedUser.getPassword());
        assertEquals(request.getUsername(), capturedUser.getUsername());

        verify(passwordEncoder).encode(request.getPassword());
        verify(jwtService).generateToken(user);

        verifyNoMoreInteractions(userRepository, passwordEncoder, jwtService);
    }


    @Test
    void givenUsername_whenCheckExists_thenTrue() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertTrue(authenticationService.isUsernameExists(anyString()));
    }

    @Test
    void givenValidCredentials_whenAuthenticate_thenReturnsAccessToken() {
        AuthenticationRequest request = new AuthenticationRequest("user", "123");
        User user = buildUserAuthentication();
        String expectedToken = "token";

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn(expectedToken);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals(expectedToken, response.getAccessToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByUsername("user");
        verify(jwtService).generateToken(user);
        verifyNoMoreInteractions(userRepository, jwtService, authenticationManager);
    }

    @Test
    void givenInvalidUsername_whenAuthenticate_thenThrowsEntityNotFoundException() {
        AuthenticationRequest request = new AuthenticationRequest("user", "123");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> authenticationService.authenticate(request));
    }

    private User buildUserAuthentication() {
        return User.builder()
                .username("user")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
    }

    private User buildUserRegister(RegisterRequest request, String encodedPassword) {
        return User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .role(request.getRole())
                .build();
    }
}
