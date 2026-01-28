package com.scholarstack.service;

import com.scholarstack.model.User;
import com.scholarstack.model.Role;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    
    // In-memory storage for now (we'll replace with database later)
    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public UserService() {
        // Add some sample users for testing
        User user1 = new User("student1", "student1@university.edu", "password123", Role.STUDENT);
        user1.setId(idGenerator.getAndIncrement());
        users.put(user1.getId(), user1);
        
        User user2 = new User("faculty1", "faculty1@university.edu", "password123", Role.FACULTY);
        user2.setId(idGenerator.getAndIncrement());
        users.put(user2.getId(), user2);
        
        User user3 = new User("admin1", "admin1@university.edu", "password123", Role.ADMIN);
        user3.setId(idGenerator.getAndIncrement());
        users.put(user3.getId(), user3);
    }
    
    // Get all users
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    // Get user by ID
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }
    
    // Get user by username
    public Optional<User> getUserByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
    
    // Create new user
    public User createUser(User user) {
        // Check if username already exists
        if (existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email already exists
        if (existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Set ID and add to storage
        user.setId(idGenerator.getAndIncrement());
        users.put(user.getId(), user);
        
        return user;
    }
    
    // Update user
    public User updateUser(Long id, User userDetails) {
        User user = users.get(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        
        // Only update password if provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(userDetails.getPassword()); // Note: No encoding for now
        }
        
        return user;
    }
    
    // Delete user
    public void deleteUser(Long id) {
        if (!users.containsKey(id)) {
            throw new RuntimeException("User not found");
        }
        users.remove(id);
    }
    
    // Check if user exists
    public boolean existsByUsername(String username) {
        return users.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }
    
    public boolean existsByEmail(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
}