package com.school.campusexpress.controller;

import com.school.campusexpress.common.R;
import com.school.campusexpress.entity.User;
import com.school.campusexpress.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "用户管理", description = "用户注册、登录、信息管理接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public R<String> register(@Valid @RequestBody User user) {
        try {
            String result = userService.register(user.getUsername(), user.getPassword(), user.getRealName(), user.getPhone());
            return R.success(result);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public R<Map<String, Object>> login(@Valid @RequestBody User user) {
        try {
            String result = userService.login(user.getUsername(), user.getPassword());
            User dbUser = userService.getUserByUsername(user.getUsername());
            
            Map<String, Object> data = new HashMap<>();
            data.put("message", result);
            data.put("userInfo", getUserInfoWithoutPassword(dbUser));
            
            return R.success(data);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "根据ID查询用户")
    @GetMapping("/{id}")
    public R<User> getUserById(@Parameter(description = "用户ID") @PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return R.success(getUserInfoWithoutPassword(user));
        }
        return R.error("用户不存在");
    }

    @Operation(summary = "查询用户列表")
    @GetMapping("/list")
    public R<List<User>> listUsers() {
        List<User> users = userService.listUsers();
        List<User> result = users.stream()
                .map(this::getUserInfoWithoutPassword)
                .collect(Collectors.toList());
        return R.success(result);
    }

    @Operation(summary = "添加用户")
    @PostMapping
    public R<String> addUser(@RequestBody User user) {
        boolean success = userService.addUser(user);
        if (success) {
            return R.success("添加成功");
        }
        return R.error("添加失败");
    }

    @Operation(summary = "更新用户信息")
    @PutMapping
    public R<String> updateUser(@RequestBody User user) {
        boolean success = userService.updateUser(user);
        if (success) {
            return R.success("更新成功");
        }
        return R.error("更新失败");
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public R<String> deleteUser(@Parameter(description = "用户ID") @PathVariable Long id) {
        boolean success = userService.deleteUser(id);
        if (success) {
            return R.success("删除成功");
        }
        return R.error("删除失败");
    }

    private User getUserInfoWithoutPassword(User user) {
        user.setPassword(null);
        return user;
    }
}
