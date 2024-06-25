package dev.jaoow.financeapp.controller;

import dev.jaoow.financeapp.dto.AuthenticationRequest;
import dev.jaoow.financeapp.dto.AuthenticationResponse;
import dev.jaoow.financeapp.dto.UserDTO;
import dev.jaoow.financeapp.model.Role;
import dev.jaoow.financeapp.model.User;
import dev.jaoow.financeapp.repository.RoleRepository;
import dev.jaoow.financeapp.repository.UserRepository;
import dev.jaoow.financeapp.service.CustomUserDetailsService;
import dev.jaoow.financeapp.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/authenticate")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return new AuthenticationResponse(jwt);
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody UserDTO userDTO) {
        // Verifica se o usuário já existe
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Atribuir a role padrão "USER"
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));
        user.setRoles(new HashSet<>(Set.of(userRole)));

        return userRepository.save(user);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/assign-role")
    public User assignRoleToUser(@RequestParam String email, @RequestParam String roleName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
        return userRepository.save(user);
    }
}
