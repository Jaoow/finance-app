package dev.jaoow.financeapp.controller;

import dev.jaoow.financeapp.model.dto.request.AuthenticationRequest;
import dev.jaoow.financeapp.model.dto.request.UserRequestDTO;
import dev.jaoow.financeapp.model.dto.response.AuthenticationResponse;
import dev.jaoow.financeapp.model.dto.response.UserResponseDTO;
import dev.jaoow.financeapp.service.CustomUserDetailsService;
import dev.jaoow.financeapp.service.UserService;
import dev.jaoow.financeapp.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "API for user authentication and registration")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @SecurityRequirements() // This is a workaround to remove the security requirement for this endpoint
    @Operation(summary = "Authenticate user", description = "Authenticates the user and returns a JWT token.")
    @PostMapping("/authenticate")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        String jwt = jwtTokenUtil.generateToken(userDetails);

        return new AuthenticationResponse(jwt);
    }

    @SecurityRequirements() // This is a workaround to remove the security requirement for this endpoint
    @Operation(summary = "Register new user", description = "Registers a new user with the provided email and password.")
    @PostMapping("/register")
    public UserResponseDTO registerUser(@RequestBody UserRequestDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    @Operation(summary = "Get self user", description = "Retrieves the currently authenticated user.")
    @GetMapping("/self")
    public UserResponseDTO getAllAccountsForCurrentUser(Principal principal) {
        return userService.getByUsername(principal.getName());
    }

    @Operation(summary = "Assign role to user", description = "Assigns a role to an existing user. Only accessible by admins.")
    @Secured("ROLE_ADMIN")
    @PostMapping("/assign-role")
    public UserResponseDTO assignRoleToUser(@RequestParam String email, @RequestParam String roleName) {
        return userService.assignRoleToUser(email, roleName);
    }
}
