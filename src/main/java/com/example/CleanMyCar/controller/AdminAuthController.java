package com.example.CleanMyCar.controller;

import com.example.CleanMyCar.model.AdminUser;
import com.example.CleanMyCar.repository.AdminUserRepository;
import com.example.CleanMyCar.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    private final AdminUserRepository repo;
    private final JwtUtil jwt;

    public AdminAuthController(AdminUserRepository repo, JwtUtil jwt) {
        this.repo = repo;
        this.jwt = jwt;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

        String username = body.get("username");
        String password = body.get("password");

        AdminUser admin = repo.findByUsername(username);

        if (admin == null) {
            System.out.println("username is doesn't exist");
            return ResponseEntity.status(401).body(
                    Map.of("success", false, "message", "Admin not found")
            );
        }

        if (!admin.getPassword().equals(password)) {
            System.out.println("the password is incorrect");
            return ResponseEntity.status(401).body(
                    Map.of("success", false, "message", "Incorrect password")
            );
        }

        String token = jwt.generateToken(username);
        System.out.println("succesfull:"+username+" "+password);

        return ResponseEntity.ok(
                Map.of("success", true, "token", token)
        );
    }

    @GetMapping("/decode")
    public ResponseEntity<?> decode(@RequestParam String token) {
        try {
            String user = jwt.extractUsername(token);
            boolean valid = jwt.isValidToken(token);

            return ResponseEntity.ok(Map.of("username", user, "valid", valid));

        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                    Map.of("valid", false, "message", "Invalid or expired token")
            );
        }
    }
}
