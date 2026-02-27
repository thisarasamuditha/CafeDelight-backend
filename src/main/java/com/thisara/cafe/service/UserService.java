package com.thisara.cafe.service;

import com.thisara.cafe.dto.LoginRequest;
import com.thisara.cafe.entity.User;
import com.thisara.cafe.repository.UserRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public @Nullable User registerUser(User user) {
        // In a real application, you would save the user to the database here
        // For this example, we'll just return the user object as is
        if(userRepository.existsByEmail(user.getEmail()))
            throw new IllegalArgumentException("Email already registered");

        User user1 = new User();
        user1.setUsername(user.getUsername());
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());
        userRepository.save(user1);
        return user1;
    }

    public @Nullable User login(LoginRequest loginRequest) {
        // In a real application, you would check the user's credentials against the database here
        // For this example, we'll just return a dummy user if the email and password match
        return userRepository.findByEmail(loginRequest.email)
                .filter(user -> user.getPassword().equals(loginRequest.password))
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

    }
}
