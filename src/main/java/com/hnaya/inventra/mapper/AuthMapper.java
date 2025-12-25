package com.hnaya.inventra.mapper;

import com.hnaya.inventra.dto.response.AuthResponse;
import com.hnaya.inventra.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "token", source = "token")
    @Mapping(target = "type", constant = "Bearer")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "role", source = "user.role")
    AuthResponse toAuthResponse(User user, String token);
}