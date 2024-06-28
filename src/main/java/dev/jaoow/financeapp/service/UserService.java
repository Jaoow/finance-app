package dev.jaoow.financeapp.service;

import dev.jaoow.financeapp.entity.Role;
import dev.jaoow.financeapp.entity.User;
import dev.jaoow.financeapp.exception.EmailAlreadyInUseException;
import dev.jaoow.financeapp.exception.ResourceNotFoundException;
import dev.jaoow.financeapp.model.dto.request.UserRequestDTO;
import dev.jaoow.financeapp.model.dto.response.UserResponseDTO;
import dev.jaoow.financeapp.repository.RoleRepository;
import dev.jaoow.financeapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserResponseDTO registerUser(UserRequestDTO userDTO) {
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new EmailAlreadyInUseException("Email already taken");
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Atribuir a role padrão "ROLE_USER"
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role ROLE_USER not found"));
        user.setRoles(new HashSet<>(Set.of(userRole)));

        user = userRepository.save(user);
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public UserResponseDTO assignRoleToUser(String email, String roleName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        user.getRoles().add(role);

        user = userRepository.save(user);

        return modelMapper.map(user, UserResponseDTO.class);
    }

    public User findByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    public boolean isAdmin(String email) {
        User user = findByUsername(email);
        return user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

    public UserResponseDTO getByUsername(String username) {
        User user = findByUsername(username);
        return modelMapper.map(user, UserResponseDTO.class);
    }
}
