package com.agendafacil.api.modules.auth.service;

import com.agendafacil.api.modules.auth.controller.AuthController;
import com.agendafacil.api.modules.auth.dto.AuthResponseDTO;
import com.agendafacil.api.modules.auth.dto.LoginRequestDTO;
import com.agendafacil.api.modules.auth.dto.RegisterRequestDTO;
import com.agendafacil.api.modules.auth.security.JwtService;
import com.agendafacil.api.modules.user.entity.Role;
import com.agendafacil.api.modules.user.entity.User;
import com.agendafacil.api.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;


    public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        User user = User.builder()
            .name(registerRequestDTO.getName())
            .email(registerRequestDTO.getEmail())
            .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
            .role(Role.USER)
            .build();
        userRepository.save(user);
        return new AuthResponseDTO(jwtService.generateToken(user), Role.USER.toString());
    }

    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequestDTO.getEmail(),
            loginRequestDTO.getPassword()
        ));

        Optional<User> optionalUser = userRepository.findByEmail(loginRequestDTO.getEmail());
        User user = optionalUser
            .orElseThrow(() -> new UsernameNotFoundException("Username " + loginRequestDTO.getEmail() + " not found"));
        return new AuthResponseDTO(jwtService.generateToken(user), user.getRole().toString());
    }
}
