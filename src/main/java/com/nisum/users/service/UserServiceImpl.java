package com.nisum.users.service;

import com.nisum.users.exception.DataValidationException;
import com.nisum.users.model.Phone;
import com.nisum.users.model.User;
import com.nisum.users.model.request.LoginRequest;
import com.nisum.users.model.request.SignupRequest;
import com.nisum.users.model.response.UserCreatedResponse;
import com.nisum.users.model.response.UserResponse;
import com.nisum.users.repository.UserRepository;
import com.nisum.users.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl {
    private UserRepository userRepository;

    private AuthenticationManager authenticationManager;

    private PasswordEncoder encoder;

    private JwtUtils jwtUtils;

    public UserServiceImpl(UserRepository userRepository,
                           AuthenticationManager authenticationManager,
                           PasswordEncoder encoder,
                           JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Validations and user creation
     *
     * @param signUpRequest
     * @return
     */
    public UserCreatedResponse creteUser(SignupRequest signUpRequest) {
        if (userRepository.existsByName(signUpRequest.getName())) {
            throw new DataValidationException("Error: Username already exists.");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DataValidationException("Error: Email is in use.");
        }

        Set<Phone> phoneSet = new HashSet<>();
        signUpRequest.getPhones().forEach(item -> {
            phoneSet.add(new Phone(item.getNumber(), item.getCitycode(), item.getContrycode()));
        });

        final User user = new User(signUpRequest.getName(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), phoneSet,
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), "", true);

        userRepository.save(user);
        final String jwt = generateToken(signUpRequest.getName(), signUpRequest.getPassword());
        final User userCreated = this.setUserJwt(user, jwt);

        return parseUserResponse(userCreated, Optional.of(jwt));

    }

    /**
     * authenticate User - login
     *
     * @param loginRequest
     * @return
     */
    public UserCreatedResponse authenticateUser(LoginRequest loginRequest) {
        final String jwt = generateToken(loginRequest.getName(), loginRequest.getPassword());

        User user = userRepository.findByName(loginRequest.getName())
                .orElseThrow(() -> new DataValidationException("User not found: " + loginRequest.getName()));

        user = this.setUserJwt(user, jwt);
        return new UserCreatedResponse(user.getId(), user.getCreated(), user.getModified(), user.getLast_login(), jwt, user.getIs_active());
    }

    /**
     * Get user for me method
     *
     * @param uuid
     * @return
     */
    public UserResponse getUser(String uuid) {

        User user = userRepository.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new DataValidationException("User not found: " + uuid));

        return new UserResponse(user.getId(), user.getCreated(), user.getModified(), user.getLast_login(),
                user.getName(), user.getEmail(), new ArrayList<>(user.getPhone()));
    }

    /**
     * Generate token for username
     *
     * @param name
     * @param pass
     * @return
     */
    private String generateToken(String name, String pass) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(name, pass));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtUtils.generateTokenFromUsername(name);

    }

    /**
     * Set jwt for user
     *
     * @param user
     * @param jwt
     * @return
     */
    private User setUserJwt(User user, String jwt) {
        user.setToken(jwt);
        user.setModified(LocalDateTime.now());
        user.setLast_login(LocalDateTime.now());
        return userRepository.save(user);
    }

    /**
     * Parse User objet to UserCreatedResponse
     */
    private UserCreatedResponse parseUserResponse(User user, Optional<String> jwt) {
        return new UserCreatedResponse(user.getId(),
                user.getCreated(),
                user.getModified(),
                user.getLast_login(),
                jwt.isPresent() ? jwt.get() : user.getToken(),
                user.getIs_active());
    }
}