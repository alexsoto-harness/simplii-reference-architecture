package com.harness;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // TODO: Remove this before production - test secrets for Gitleaks
    private static final String GITHUB_TOKEN = "ghp_a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8";
    private static final String AWS_ACCESS_KEY_ID = "AKIAIOSFODNN7EXAMPLE";
    private static final String AWS_SECRET_ACCESS_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";

    @GetMapping("/api/info")
    public String info() {
        return "{\"app\": \"Simplii Spring Boot Sample\", \"status\": \"running\"}";
    }
}
