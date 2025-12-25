package com.hnaya.inventra.service.impl;

import com.hnaya.inventra.dto.request.LoginRequest;
import com.hnaya.inventra.dto.response.AuthResponse;
import com.hnaya.inventra.entity.User;
import com.hnaya.inventra.exception.ResourceNotFoundException;
import com.hnaya.inventra.exception.UnauthorizedException;
import com.hnaya.inventra.exception.UserDisabledException;
import com.hnaya.inventra.mapper.AuthMapper;
import com.hnaya.inventra.repository.UserRepository;
import com.hnaya.inventra.security.JwtUtil;
import com.hnaya.inventra.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthMapper authMapper;

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            String token = jwtUtil.generateJwt(authentication);

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + request.getUsername()));

            log.info("User '{}' logged in successfully", request.getUsername());

            return authMapper.toAuthResponse(user, token);
        } catch (DisabledException e) {
            log.error("User account is disabled: {}", request.getUsername());
            throw new UserDisabledException("User account is disabled: " + request.getUsername());
        } catch (BadCredentialsException e) {
            log.error("Invalid credentials for user: {}", request.getUsername());
            throw new UnauthorizedException("Invalid credentials for user: " + request.getUsername());
        }
    }
}