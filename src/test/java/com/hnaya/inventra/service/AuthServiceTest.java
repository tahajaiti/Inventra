package com.hnaya.inventra.service;

import com.hnaya.inventra.dto.request.LoginRequest;
import com.hnaya.inventra.dto.response.AuthResponse;
import com.hnaya.inventra.entity.User;
import com.hnaya.inventra.entity.enums.Role;
import com.hnaya.inventra.exception.ResourceNotFoundException;
import com.hnaya.inventra.exception.UnauthorizedException;
import com.hnaya.inventra.exception.UserDisabledException;
import com.hnaya.inventra.mapper.AuthMapper;
import com.hnaya.inventra.repository.UserRepository;
import com.hnaya.inventra.security.JwtUtil;
import com.hnaya.inventra.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthMapper authMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequest loginRequest;
    private User user;
    private Authentication authentication;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("testuser", "password123");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .password("encodedPassword")
                .name("Test")
                .lastName("User")
                .email("test@example.com")
                .role(Role.MANAGER)
                .active(true)
                .build();

        authentication = mock(Authentication.class);

        authResponse = AuthResponse.builder()
                .token("jwt-token")
                .type("Bearer")
                .username("testuser")
                .role(Role.MANAGER)
                .build();
    }

    @Nested
    @DisplayName("Login Tests")
    class LoginTests {

        @Test
        @DisplayName("Should login successfully with valid credentials")
        void login_WithValidCredentials_ReturnsAuthResponse() {
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(jwtUtil.generateJwt(authentication)).thenReturn("jwt-token");
            when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
            when(authMapper.toAuthResponse(user, "jwt-token")).thenReturn(authResponse);

            AuthResponse result = authService.login(loginRequest);

            assertThat(result).isNotNull();
            assertThat(result.getToken()).isEqualTo("jwt-token");
            assertThat(result.getType()).isEqualTo("Bearer");
            assertThat(result.getUsername()).isEqualTo("testuser");
            assertThat(result.getRole()).isEqualTo(Role.MANAGER);

            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtUtil).generateJwt(authentication);
            verify(userRepository).findByUsername("testuser");
            verify(authMapper).toAuthResponse(user, "jwt-token");
        }

        @Test
        @DisplayName("Should login successfully as admin")
        void login_AsAdmin_ReturnsAuthResponse() {
            User adminUser = User.builder()
                    .id(2L)
                    .username("admin")
                    .password("encodedPassword")
                    .name("Admin")
                    .lastName("User")
                    .email("admin@example.com")
                    .role(Role.ADMIN)
                    .active(true)
                    .build();

            LoginRequest adminLoginRequest = new LoginRequest("admin", "adminpass");

            AuthResponse adminAuthResponse = AuthResponse.builder()
                    .token("admin-jwt-token")
                    .type("Bearer")
                    .username("admin")
                    .role(Role.ADMIN)
                    .build();

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(jwtUtil.generateJwt(authentication)).thenReturn("admin-jwt-token");
            when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));
            when(authMapper.toAuthResponse(adminUser, "admin-jwt-token")).thenReturn(adminAuthResponse);

            AuthResponse result = authService.login(adminLoginRequest);

            assertThat(result).isNotNull();
            assertThat(result.getRole()).isEqualTo(Role.ADMIN);
        }

        @Test
        @DisplayName("Should throw UnauthorizedException when credentials are invalid")
        void login_WithInvalidCredentials_ThrowsUnauthorizedException() {
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Bad credentials"));

            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContaining("Invalid credentials for user: testuser");

            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verifyNoInteractions(jwtUtil);
            verifyNoInteractions(userRepository);
            verifyNoInteractions(authMapper);
        }

        @Test
        @DisplayName("Should throw UserDisabledException when user account is disabled")
        void login_WithDisabledAccount_ThrowsUserDisabledException() {
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new DisabledException("User is disabled"));

            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(UserDisabledException.class)
                    .hasMessageContaining("User account is disabled: testuser");

            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verifyNoInteractions(jwtUtil);
            verifyNoInteractions(userRepository);
            verifyNoInteractions(authMapper);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when user not found after authentication")
        void login_WhenUserNotFoundAfterAuth_ThrowsResourceNotFoundException() {
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(jwtUtil.generateJwt(authentication)).thenReturn("jwt-token");
            when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("User not found with username: testuser");

            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtUtil).generateJwt(authentication);
            verify(userRepository).findByUsername("testuser");
            verifyNoInteractions(authMapper);
        }
    }

    @Nested
    @DisplayName("Authentication Manager Interaction Tests")
    class AuthenticationManagerTests {

        @Test
        @DisplayName("Should pass correct credentials to authentication manager")
        void login_ShouldPassCorrectCredentials() {
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(jwtUtil.generateJwt(authentication)).thenReturn("jwt-token");
            when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
            when(authMapper.toAuthResponse(user, "jwt-token")).thenReturn(authResponse);

            authService.login(loginRequest);

            verify(authenticationManager).authenticate(argThat(token ->
                    token instanceof UsernamePasswordAuthenticationToken &&
                            token.getPrincipal().equals("testuser") &&
                            token.getCredentials().equals("password123")
            ));
        }
    }
}