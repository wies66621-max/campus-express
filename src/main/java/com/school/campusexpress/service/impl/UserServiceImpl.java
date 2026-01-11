package com.school.campusexpress.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school.campusexpress.entity.User;
import com.school.campusexpress.mapper.UserMapper;
import com.school.campusexpress.service.UserService;
import com.school.campusexpress.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public List<User> listUsers() {
        return list();
    }

    @Override
    public User getUserById(Long id) {
        return getById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return getOne(wrapper);
    }

    @Override
    public boolean addUser(User user) {
        User existUser = getUserByUsername(user.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        user.setRole("user");
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        return save(user);
    }

    @Override
    public boolean updateUser(User user) {
        User existingUser = getById(user.getId());
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (!existingUser.getUsername().equals(user.getUsername())) {
            User existUser = getUserByUsername(user.getUsername());
            if (existUser != null) {
                throw new RuntimeException("用户名已存在");
            }
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        }
        
        if (user.getRealName() == null || user.getRealName().trim().isEmpty()) {
            user.setRealName(existingUser.getRealName());
        }
        
        user.setUpdateTime(LocalDateTime.now());
        return updateById(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        return removeById(id);
    }

    @Override
    public String register(String username, String password, String realName, String phone) {
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }

        User existUser = getUserByUsername(username);
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRealName(realName);
        user.setPhone(phone);
        user.setRole("user");
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        boolean success = save(user);
        if (!success) {
            throw new RuntimeException("注册失败");
        }

        return "注册成功";
    }

    @Override
    public String login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }

        User user = getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }

        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return "登录成功";
    }

    @Override
    public Page<User> listUsersByPage(Integer pageNum, Integer pageSize, String role, Integer status) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(role), User::getRole, role)
               .eq(status != null, User::getStatus, status)
               .orderByDesc(User::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public boolean deleteUser(Long id, Long currentUserId) {
        if (id.equals(currentUserId)) {
            throw new RuntimeException("不能删除当前登录用户");
        }
        return removeById(id);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        User user = getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());
        return updateById(user);
    }

    @Override
    public boolean resetPassword(Long id) {
        User user = getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if ("admin".equals(user.getRole())) {
            throw new RuntimeException("不能重置管理员密码");
        }
        String defaultPassword = "123456";
        user.setPassword(defaultPassword);
        user.setUpdateTime(LocalDateTime.now());
        return updateById(user);
    }

    @Override
    public Page<User> searchUsers(Integer pageNum, Integer pageSize, String keyword) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(StringUtils.hasText(keyword), w -> w
                .like(User::getUsername, keyword)
                .or()
                .like(User::getPhone, keyword)
                .or()
                .like(User::getRealName, keyword)
        ).orderByDesc(User::getCreateTime);
        return page(page, wrapper);
    }
}
