package com.spotfinderbackend.iam.interfaces;

import com.spotfinderbackend.iam.application.internal.commandservices.UserCommandServiceImpl;
import com.spotfinderbackend.iam.application.internal.queryservices.UserQueryServiceImpl;
import com.spotfinderbackend.iam.domain.model.aggregates.User;
import com.spotfinderbackend.iam.domain.model.commands.ChangePasswordCommand;
import com.spotfinderbackend.iam.domain.model.commands.SignInCommand;
import com.spotfinderbackend.iam.domain.model.commands.SignUpCommand;
import com.spotfinderbackend.iam.domain.model.commands.UpdateProfileCommand;
import com.spotfinderbackend.iam.domain.model.exceptions.InvalidCredentialsException;
import com.spotfinderbackend.iam.domain.model.exceptions.UserAccountDeactivatedException;
import com.spotfinderbackend.iam.domain.model.exceptions.UserAlreadyExistsException;
import com.spotfinderbackend.iam.domain.model.exceptions.UserNotFoundException;
import com.spotfinderbackend.iam.domain.model.queries.GetAllUsersQuery;
import com.spotfinderbackend.iam.domain.model.queries.GetUserByEmailQuery;
import com.spotfinderbackend.iam.domain.services.RoleValidationService;
import com.spotfinderbackend.iam.interfaces.rest.resources.AuthenticationResponseResource;
import com.spotfinderbackend.iam.interfaces.rest.resources.ChangePasswordResource;
import com.spotfinderbackend.iam.interfaces.rest.resources.SignInResource;
import com.spotfinderbackend.iam.interfaces.rest.resources.SignUpResource;
import com.spotfinderbackend.iam.interfaces.rest.resources.UpdateProfileResource;
import com.spotfinderbackend.iam.interfaces.rest.resources.UserResource;
import com.spotfinderbackend.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import com.spotfinderbackend.iam.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import com.spotfinderbackend.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Users REST Controller
 * <p>
 * This controller handles HTTP requests for user-related operations including
 * registration, authentication, and user management following REST principles.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

    private final UserCommandServiceImpl userCommandService;
    private final UserQueryServiceImpl userQueryService;
    private final RoleValidationService roleValidationService;

    public UsersController(
            UserCommandServiceImpl userCommandService,
            UserQueryServiceImpl userQueryService,
            RoleValidationService roleValidationService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
        this.roleValidationService = roleValidationService;
    }

    /**
     * Register a new user
     * @param signUpResource the user registration data
     * @return ResponseEntity with success message
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpResource signUpResource) {
        try {
            System.out.println("🚀 Entrando al endpoint /signup");
            LOGGER.info("Processing signup request for email: {}", signUpResource.email());
            
            SignUpCommand command = SignUpCommandFromResourceAssembler.toCommandFromResource(signUpResource);
            userCommandService.handle(command);
            
            LOGGER.info("User registered successfully: {}", signUpResource.email());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully");
                    
        } catch (UserAlreadyExistsException e) {
            LOGGER.warn("Signup failed for email {}: {}", signUpResource.email(), e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Signup failed for email {}: {}", signUpResource.email(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Unexpected error during signup for email {}: {}", signUpResource.email(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred during registration");
        }
    }

    /**
     * Authenticate a user and return JWT token
     * @param signInResource the user authentication data
     * @return ResponseEntity with JWT token and user information
     */
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInResource signInResource) {
        try {
            LOGGER.info("Processing signin request for email: {}", signInResource.email());
            
            SignInCommand command = SignInCommandFromResourceAssembler.toCommandfromResource(signInResource);
            userCommandService.handle(command);
            
            // Get user details for token generation
            Optional<User> userOptional = userQueryService.handle(new GetUserByEmailQuery(signInResource.email()));
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authentication failed");
            }
            
            User user = userOptional.get();
            String token = userCommandService.generateTokenForUser(user);
            UserResource userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user);
            
            // Token expires in 7 days (604800 seconds)
            AuthenticationResponseResource response = AuthenticationResponseResource.of(token, 604800L, userResource);
            
            LOGGER.info("User authenticated successfully: {}", signInResource.email());
            return ResponseEntity.ok(response);
                    
        } catch (InvalidCredentialsException e) {
            LOGGER.warn("Signin failed for email {}: {}", signInResource.email(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        } catch (UserAccountDeactivatedException e) {
            LOGGER.warn("Signin failed for email {}: {}", signInResource.email(), e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Signin failed for email {}: {}", signInResource.email(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Unexpected error during signin for email {}: {}", signInResource.email(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred during authentication");
        }
    }

    /**
     * Get user by email
     * @param email the user email
     * @return ResponseEntity with user information
     */
    @GetMapping("/by-email")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        try {
            LOGGER.debug("Processing getUserByEmail request for email: {}", email);
            
            Optional<User> userOptional = userQueryService.handle(new GetUserByEmailQuery(email));
            
            if (userOptional.isEmpty()) {
                throw new UserNotFoundException(email);
            }
            
            User user = userOptional.get();
            UserResource userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user);
            
            return ResponseEntity.ok(userResource);
                    
        } catch (UserNotFoundException e) {
            LOGGER.warn("User not found with email: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Unexpected error retrieving user by email {}: {}", email, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while retrieving user");
        }
    }

    /**
     * Get all users
     * @return ResponseEntity with list of all users
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            LOGGER.debug("Processing getAllUsers request");
            
            List<User> users = userQueryService.handle(new GetAllUsersQuery());
            List<UserResource> userResources = users.stream()
                    .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
            
            return ResponseEntity.ok(userResources);
                    
        } catch (Exception e) {
            LOGGER.error("Unexpected error retrieving all users: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while retrieving users");
        }
    }

    /**
     * Get available roles for registration
     * @return ResponseEntity with list of available roles
     */
    @GetMapping("/available-roles")
    public ResponseEntity<?> getAvailableRoles() {
        try {
            LOGGER.debug("Processing getAvailableRoles request");

            var availableRoles = roleValidationService.getAvailableRolesForRegistration();

            return ResponseEntity.ok(availableRoles);

        } catch (Exception e) {
            LOGGER.error("Unexpected error retrieving available roles: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while retrieving available roles");
        }
    }

    /**
     * Update the profile (first name / last name) of an authenticated user.
     * Email and roles are intentionally not editable here.
     *
     * @param userId   the id of the user being updated
     * @param resource body with the new `firstName` and `lastName`
     * @return 200 with the updated user, 404 if not found, 400 if input invalid.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(@PathVariable Long userId,
                                           @RequestBody UpdateProfileResource resource) {
        try {
            LOGGER.info("Processing update-profile request for user ID: {}", userId);

            var command = new UpdateProfileCommand(userId, resource.firstName(), resource.lastName());
            var user = userCommandService.handle(command);

            return user
                    .map(u -> ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(u)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Change the password of an authenticated user.
     * The driver must supply their current password for verification.
     *
     * @param userId   the id of the user whose password is being changed
     * @param resource body with `currentPassword` and `newPassword`
     * @return 204 No Content on success, 401 if the current password is wrong,
     *         404 if the user does not exist, 400 if the input is invalid.
     */
    @PostMapping("/{userId}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long userId,
                                            @RequestBody ChangePasswordResource resource) {
        try {
            LOGGER.info("Processing change-password request for user ID: {}", userId);

            var command = new ChangePasswordCommand(
                    userId,
                    resource.currentPassword(),
                    resource.newPassword()
            );
            userCommandService.handle(command);

            return ResponseEntity.noContent().build();

        } catch (UserNotFoundException e) {
            LOGGER.warn("Change-password failed for user ID {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidCredentialsException e) {
            LOGGER.warn("Change-password failed for user ID {}: current password mismatch", userId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Current password is incorrect");
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Change-password failed for user ID {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
