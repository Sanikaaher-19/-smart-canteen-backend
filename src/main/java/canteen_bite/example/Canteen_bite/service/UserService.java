package canteen_bite.example.Canteen_bite.service;

import canteen_bite.example.Canteen_bite.dto.RegisterRequest;
import canteen_bite.example.Canteen_bite.entity.Role;
import canteen_bite.example.Canteen_bite.entity.User;
import canteen_bite.example.Canteen_bite.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest req) {
        logger.debug("Registering user with email: {}", req.getEmail());

        if (userRepository.existsByEmail(req.getEmail())) {
            logger.warn("Email already registered: {}", req.getEmail());
            throw new RuntimeException("Email already registered");
        }

        // Strict validation for role
        Role userRole;
        if (req.getRole() == null) {
            throw new RuntimeException("Role must be specified");
        } else {
            try {
                userRole = Role.valueOf(req.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid role specified: {}", req.getRole());
                throw new RuntimeException("Invalid role specified");
            }
        }

        User u = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(userRole)
                .build();

        logger.debug("Saving new user: {}", u.getEmail());
        return userRepository.save(u);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
