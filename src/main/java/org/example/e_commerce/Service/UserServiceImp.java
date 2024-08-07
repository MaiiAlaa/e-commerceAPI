package org.example.e_commerce.Service;

import org.example.e_commerce.Entity.User;
import org.example.e_commerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        try {
            // Hash password before saving
            user.setPasswordHash(hashPassword(user.getPasswordHash()));
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            System.err.println("Data integrity violation: " + e.getMessage());
            throw new RuntimeException("Error saving user due to data integrity issues.", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving user: " + e.getMessage(), e);
        }
    }

    private String hashPassword(String password) {
        // Implement your password hashing logic here
        return new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public Optional<User> getUserById(long userid) {
        try {
            return userRepository.findById(userid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving user by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving all users: " + e.getMessage(), e);
        }
    }

    @Override
    public User updateUser(long userid, User user) {
        try {
            if (userRepository.existsById(userid)) {
                user.setUserid(userid); // Use correct method name
                // Optionally hash the password if it's updated
                if (user.getPasswordHash() != null) {
                    user.setPasswordHash(hashPassword(user.getPasswordHash()));
                }
                return userRepository.save(user);
            }
            throw new RuntimeException("User not found with ID: " + userid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(long userid) {
        try {
            userRepository.deleteById(userid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean authenticateUser(String username, String password) {
        // Implement authentication logic, e.g., by comparing password hashes
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            // Compare the hashed password
            return new BCryptPasswordEncoder().matches(password, user.get().getPasswordHash());
        }
        return false;
    }
}
