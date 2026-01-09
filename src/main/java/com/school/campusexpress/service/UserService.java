package com.school.campusexpress.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.campusexpress.entity.User;
import java.util.List;

public interface UserService {

    List<User> listUsers();

    User getUserById(Long id);

    User getUserByUsername(String username);

    boolean addUser(User user);

    boolean updateUser(User user);

    boolean deleteUser(Long id);

    String register(String username, String password, String realName, String phone);

    String login(String username, String password);

    Page<User> listUsersByPage(Integer pageNum, Integer pageSize, String role, Integer status);

    boolean deleteUser(Long id, Long currentUserId);

    boolean updateStatus(Long id, Integer status);

    boolean resetPassword(Long id);

    Page<User> searchUsers(Integer pageNum, Integer pageSize, String keyword);
}
