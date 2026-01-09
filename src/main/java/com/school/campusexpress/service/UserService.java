package com.school.campusexpress.service;

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
}
