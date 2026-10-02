package org.example.springmvcdemo.service;

import org.example.springmvcdemo.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class QuizUserDetailsService implements UserDetailsService {

    // Almacén en memoria de usuarios (username -> User)
    private final Map<String, User> users = new HashMap<>();

    // Codificador de contraseñas
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Constructor: precarga un usuario de ejemplo para poder iniciar sesión
    public QuizUserDetailsService() {
        users.put("admin", new User(
                "admin",
                "admin@example.com",
                passwordEncoder.encode("admin123"),
                "ADMIN"
        ));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    public void registerUser(String username, String password, String email, String role) {
        User user = new User(username, email, passwordEncoder.encode(password), role);
        users.put(username, user);
    }
}