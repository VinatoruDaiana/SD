package com.example.user.dtos.builders;

import com.example.user.dtos.UserDTO;
import com.example.user.dtos.UserDetailsDTO;
import com.example.user.entities.User;

public class UserBuilder {

    private UserBuilder() {

    }

    //  conversie User -> UserDTO
    public static UserDTO toUserDTO(User user) {
        if (user == null) return null;
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }

    //  conversie User -> UserDetailsDTO
    public static UserDetailsDTO toUserDetailsDTO(User user) {
        if (user == null) return null;
        return new UserDetailsDTO(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                user.getRole()
        );
    }

    //  conversie UserDetailsDTO -> User
    public static User toEntity(UserDetailsDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPasswordHash(dto.getPassword());
        user.setRole(dto.getRole());
        return user;
    }
}
