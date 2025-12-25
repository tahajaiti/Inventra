package com.hnaya.inventra.service;

import com.hnaya.inventra.dto.request.LoginRequest;
import com.hnaya.inventra.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);
}
