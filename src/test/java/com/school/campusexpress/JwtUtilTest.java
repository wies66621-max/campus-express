package com.school.campusexpress;

import com.school.campusexpress.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testGenerateToken() {
        Long userId = 1L;
        String username = "testuser";
        String role = "user";

        String token = jwtUtil.generateToken(userId, username, role);

        assertNotNull(token, "Token should not be null");
        assertFalse(token.isEmpty(), "Token should not be empty");

        System.out.println("Generated Token: " + token);
    }

    @Test
    public void testParseToken() {
        Long userId = 1L;
        String username = "testuser";
        String role = "user";

        String token = jwtUtil.generateToken(userId, username, role);

        Long extractedUserId = jwtUtil.getUserIdFromToken(token);
        String extractedUsername = jwtUtil.getUsernameFromToken(token);
        String extractedRole = jwtUtil.getRoleFromToken(token);

        assertEquals(userId, extractedUserId, "User ID should match");
        assertEquals(username, extractedUsername, "Username should match");
        assertEquals(role, extractedRole, "Role should match");

        System.out.println("Token parsing successful:");
        System.out.println("  User ID: " + extractedUserId);
        System.out.println("  Username: " + extractedUsername);
        System.out.println("  Role: " + extractedRole);
    }

    @Test
    public void testValidateToken() {
        Long userId = 1L;
        String username = "testuser";
        String role = "user";

        String token = jwtUtil.generateToken(userId, username, role);

        assertTrue(jwtUtil.validateToken(token), "Valid token should pass validation");

        String invalidToken = "invalid.token.here";
        assertFalse(jwtUtil.validateToken(invalidToken), "Invalid token should fail validation");

        System.out.println("Token validation successful");
    }

    @Test
    public void testTokenExpiration() {
        Long userId = 1L;
        String username = "testuser";
        String role = "user";

        String token = jwtUtil.generateToken(userId, username, role);

        assertFalse(jwtUtil.isTokenExpired(token), "Freshly generated token should not be expired");

        System.out.println("Token expiration check successful");
    }
}
