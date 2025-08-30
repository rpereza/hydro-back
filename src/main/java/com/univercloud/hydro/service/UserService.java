package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    
    /**
     * Create a new user
     */
    User createUser(User user);
    
    /**
     * Update an existing user
     */
    User updateUser(User user);
    
    /**
     * Find user by ID
     */
    Optional<User> findById(Long id);
    
    /**
     * Find user by username
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find user by username or email
     */
    Optional<User> findByUsernameOrEmail(String usernameOrEmail);
    
    /**
     * Get all users
     */
    List<User> findAllUsers();
    
    /**
     * Get all enabled users
     */
    List<User> findAllEnabledUsers();
    
    /**
     * Delete user by ID
     */
    void deleteUser(Long id);
    
    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Enable or disable user
     */
    User setUserEnabled(Long userId, boolean enabled);
    
    /**
     * Add role to user
     */
    User addRoleToUser(Long userId, String roleName);
    
    /**
     * Remove role from user
     */
    User removeRoleFromUser(Long userId, String roleName);
}
