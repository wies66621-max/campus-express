package com.school.campusexpress.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.campusexpress.annotation.RequireAuth;
import com.school.campusexpress.common.R;
import com.school.campusexpress.entity.User;
import com.school.campusexpress.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理员用户管理", description = "管理员用户查询、添加、更新、删除、启用/禁用、重置密码接口")
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户列表查询")
    @RequireAuth
    @GetMapping("/list")
    public R<Page<User>> getUserList(
            @Parameter(description = "角色") @RequestParam(required = false) String role,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Page<User> page = userService.listUsersByPage(pageNum, pageSize, role, status);
            return R.success(page);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "用户详情查询")
    @RequireAuth
    @GetMapping("/{id}")
    public R<User> getUserById(@Parameter(description = "用户ID") @PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                user.setPassword(null);
                return R.success(user);
            }
            return R.error("用户不存在");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "添加用户")
    @RequireAuth
    @PostMapping("/add")
    public R<String> addUser(@Valid @RequestBody User user) {
        try {
            boolean success = userService.addUser(user);
            if (success) {
                return R.success("添加成功");
            }
            return R.error("添加失败");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "更新用户信息")
    @RequireAuth
    @PutMapping("/update")
    public R<String> updateUser(@RequestBody User user) {
        try {
            boolean success = userService.updateUser(user);
            if (success) {
                return R.success("更新成功");
            }
            return R.error("更新失败");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "删除用户")
    @RequireAuth
    @DeleteMapping("/delete/{id}")
    public R<String> deleteUser(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "当前登录用户ID") @RequestParam Long currentUserId) {
        try {
            boolean success = userService.deleteUser(id, currentUserId);
            if (success) {
                return R.success("删除成功");
            }
            return R.error("删除失败");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "启用/禁用用户")
    @RequireAuth
    @PutMapping("/status/{id}")
    public R<String> updateStatus(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "状态(0=禁用,1=启用)") @RequestParam Integer status) {
        try {
            boolean success = userService.updateStatus(id, status);
            if (success) {
                return R.success("状态更新成功");
            }
            return R.error("状态更新失败");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "重置用户密码")
    @RequireAuth
    @PutMapping("/reset-password/{id}")
    public R<String> resetPassword(@Parameter(description = "用户ID") @PathVariable Long id) {
        try {
            boolean success = userService.resetPassword(id);
            if (success) {
                return R.success("密码重置成功，新密码为：123456");
            }
            return R.error("密码重置失败");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "搜索用户")
    @RequireAuth
    @GetMapping("/search")
    public R<Page<User>> searchUsers(
            @Parameter(description = "搜索关键词（用户名/手机号/真实姓名）") @RequestParam String keyword,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Page<User> page = userService.searchUsers(pageNum, pageSize, keyword);
            return R.success(page);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }
}
