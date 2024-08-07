package org.example.e_commerce.Controller;

import org.example.e_commerce.Entity.User;
import org.example.e_commerce.Service.UserServiceImp;
import org.example.e_commerce.dto.dtoRequest.SignUpRequestDTO;
import org.example.e_commerce.dto.dtoRequest.SignInRequestDTO;
import org.example.e_commerce.dto.dtoResponse.SignUpResponseDTO;
import org.example.e_commerce.dto.dtoResponse.SignInResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserServiceImp userServiceImp;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDTO> signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
        try {
            User user = new User();
            user.setFirstName(signUpRequestDTO.getFirstname());
            user.setLastName(signUpRequestDTO.getLastname());
            user.setUsername(signUpRequestDTO.getUsername());
            user.setEmail(signUpRequestDTO.getEmail());
            user.setPasswordHash(signUpRequestDTO.getPassword()); // Hash the password in service
            User savedUser = userServiceImp.saveUser(user);

            SignUpResponseDTO response = new SignUpResponseDTO();
            response.setUserId(savedUser.getUserid()); // Ensure this matches the field name
            response.setMessage("User registered successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            SignUpResponseDTO response = new SignUpResponseDTO();
            response.setMessage("Internal server error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDTO> signIn(@Valid @RequestBody SignInRequestDTO signInRequestDTO) {
        try {
            boolean isAuthenticated = userServiceImp.authenticateUser(signInRequestDTO.getUsername(), signInRequestDTO.getPassword());
            SignInResponseDTO response = new SignInResponseDTO();
            if (isAuthenticated) {
                response.setMessage("User authenticated successfully");
                // response.setToken("your-generated-token"); // Optional: Set token if using JWT
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.setMessage("Invalid username or password");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            SignInResponseDTO response = new SignInResponseDTO();
            response.setMessage("Internal server error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
