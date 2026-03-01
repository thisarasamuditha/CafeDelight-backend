package com.thisara.cafe.service;

import com.thisara.cafe.dto.AuthResponse;
import com.thisara.cafe.dto.LoginRequest;
import com.thisara.cafe.entity.User;
import com.thisara.cafe.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse registerUser(User user) {
        if(userRepository.existsByEmail(user.getEmail()))
            throw new IllegalArgumentException("Email already registered");

        User user1 = new User();
        user1.setUsername(user.getUsername());
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());
        User savedUser = userRepository.save(user1);
        return toAuthResponse(savedUser);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email)
            .filter(foundUser -> foundUser.getPassword().equals(loginRequest.password))
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        return toAuthResponse(user);
    }

    private AuthResponse toAuthResponse(User user) {
        return new AuthResponse(user.getUserId(), user.getUsername(), user.getEmail());
    }
}
