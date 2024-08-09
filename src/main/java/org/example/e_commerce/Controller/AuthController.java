package org.example.e_commerce.Controller;

import org.example.e_commerce.util.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/generate-token")
    public String generateToken(@RequestParam String username) {
        return jwtUtil.generateToken(username);
    }
}
