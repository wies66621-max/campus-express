package com.school.campusexpress.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "欢迎使用校园快递管理系统");
        response.put("status", "running");
        response.put("version", "1.0.0");
        
        Map<String, String> apiInfo = new HashMap<>();
        apiInfo.put("user_api", "/user/**");
        apiInfo.put("express_api", "/express/** (待开发)");
        apiInfo.put("order_api", "/order/** (待开发)");
        response.put("api_endpoints", apiInfo);
        
        return response;
    }
}