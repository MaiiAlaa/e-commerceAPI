package org.example.e_commerce.dto.dtoResponse;

public class SignInResponseDTO {
    private String message;
    private String token; // Optional if using JWT

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
