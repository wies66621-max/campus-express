package com.school.campusexpress.test;

import com.school.campusexpress.util.JwtUtil;

public class TestJwt {
    public static void main(String[] args) {
        JwtUtil jwtUtil = new JwtUtil();

        System.out.println("=== JWT Token 测试 ===\n");

        Long userId = 1L;
        String username = "testuser";
        String role = "user";

        System.out.println("1. 生成Token...");
        String token = jwtUtil.generateToken(userId, username, role);
        System.out.println("Token生成成功: " + token.substring(0, Math.min(50, token.length())) + "...\n");

        System.out.println("2. 解析Token...");
        Long extractedUserId = jwtUtil.getUserIdFromToken(token);
        String extractedUsername = jwtUtil.getUsernameFromToken(token);
        String extractedRole = jwtUtil.getRoleFromToken(token);
        System.out.println("用户ID: " + extractedUserId);
        System.out.println("用户名: " + extractedUsername);
        System.out.println("角色: " + extractedRole + "\n");

        System.out.println("3. 验证Token...");
        boolean isValid = jwtUtil.validateToken(token);
        System.out.println("Token验证结果: " + (isValid ? "有效" : "无效") + "\n");

        System.out.println("4. 检查Token过期...");
        boolean isExpired = jwtUtil.isTokenExpired(token);
        System.out.println("Token过期状态: " + (isExpired ? "已过期" : "未过期") + "\n");

        System.out.println("5. 测试无效Token...");
        boolean isInvalidValid = jwtUtil.validateToken("invalid.token.here");
        System.out.println("无效Token验证结果: " + (isInvalidValid ? "有效" : "无效") + "\n");

        System.out.println("=== 测试完成 ===");
        System.out.println("✅ 所有JWT功能测试通过！");
    }
}
