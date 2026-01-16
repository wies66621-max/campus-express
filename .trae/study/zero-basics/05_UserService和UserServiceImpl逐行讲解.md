# UserService 和 UserServiceImpl 逐行讲解（零基础版）

## 📚 学习目标
- 理解什么是Service（业务逻辑层）
- 理解UserService接口和UserServiceImpl实现类的关系
- 理解为什么需要Service层
- 理解三层架构：Controller -> Service -> Mapper
- 能在答辩时用自己的话说出Service的作用

---

## 1️⃣ 这个文件是干什么的（白话）

**UserService** 是一个**接口**，定义了用户相关的业务逻辑方法。

**UserServiceImpl** 是一个**实现类**，实现了UserService接口，具体实现业务逻辑。

就像生活中：
- **Service接口** = 服务清单（列出所有提供的服务）
- **ServiceImpl实现类** = 服务员（具体执行服务）

在程序中：
- **UserService** = 定义用户相关的业务逻辑方法（注册、登录、查询用户等）
- **UserServiceImpl** = 具体实现这些业务逻辑方法

---

## 2️⃣ 不写这个文件会出现什么问题

1. **无法实现业务逻辑**：Controller无法调用业务逻辑方法
2. **代码混乱**：业务逻辑和Controller混在一起，难以维护
3. **无法复用**：业务逻辑无法被多个Controller复用
4. **无法测试**：无法单独测试业务逻辑

---

## 3️⃣ 为什么需要 Service 层

### ① 三层架构

```
Controller（控制器层）
    ↓
Service（业务逻辑层）
    ↓
Mapper（数据访问层）
    ↓
数据库
```

**Controller（控制器层）**：
- 作用：接收HTTP请求，调用Service处理业务逻辑，返回结果给前端
- 就像：接待员（接收客人的请求，安排相应的服务）

**Service（业务逻辑层）**：
- 作用：处理业务逻辑，调用Mapper操作数据库
- 就像：服务员（处理具体的业务逻辑，比如验证用户名是否存在、验证密码是否正确）

**Mapper（数据访问层）**：
- 作用：操作数据库，增删改查
- 就像：厨师（操作数据库，获取数据）

### ② 为什么不能直接在 Controller 写数据库代码？

```java
// 错误示例：直接在Controller写数据库代码
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody User user) {
        // 问题1：Controller直接操作数据库，违反三层架构原则
        User dbUser = userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, user.getUsername())
        );

        // 问题2：业务逻辑和Controller混在一起，代码混乱
        if (dbUser == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!user.getPassword().equals(dbUser.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 问题3：业务逻辑无法被多个Controller复用
        // 比如管理员Controller也需要登录逻辑，但无法复用
        // ...
    }
}

// 正确示例：在Service处理业务逻辑
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody User user) {
        // 优点1：Controller只负责接收HTTP请求，不处理业务逻辑
        String result = userService.login(user.getUsername(), user.getPassword());
        // ...
    }
}

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public String login(String username, String password) {
        // 优点2：业务逻辑在Service中处理，代码清晰
        User dbUser = userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
        );

        if (dbUser == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!password.equals(dbUser.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return "登录成功";
    }
}
```

### ③ Service 在三层架构中的位置

| 层 | 作用 | 例子 |
|---|---|---|
| Controller | 接收HTTP请求，调用Service | UserController |
| Service | 处理业务逻辑，调用Mapper | UserServiceImpl |
| Mapper | 操作数据库，增删改查 | UserMapper |

---

## 4️⃣ UserService 接口逐行讲解

### 第1-34行：package、import和接口定义

```java
package com.school.campusexpress.service;  // 第1行

// 第1行详细解释：
// package：Java关键字，表示"包"
// com.school.campusexpress.service：包名
// 作用：告诉Java"这个UserService接口在com.school.campusexpress.service包中"
// 如果删掉会怎样：编译错误，Java不知道这个接口在哪个包中

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;  // 第2行

// 第2行详细解释：
// import：Java关键字，表示"导入"
// com.baomidou.mybatisplus.extension.plugins.pagination.Page：要导入的类
// 作用：导入Page类，用来分页查询
// 如果删掉会怎样：无法使用Page类

import com.school.campusexpress.entity.User;  // 第3行

// 第3行详细解释：
// 导入User实体类，用来表示用户
// 如果删掉会怎样：无法使用User类

import java.util.List;  // 第4行

// 第4行详细解释：
// 导入List接口，用来存储列表数据
// 如果删掉会怎样：无法使用List接口

public interface UserService {  // 第6行

// 第6行详细解释：
// public：访问修饰符，表示"公开的"
// interface：Java关键字，表示"接口"
// UserService：接口名
// 作用：定义一个名为UserService的接口
// 如果删掉public会怎样：其他类无法访问UserService接口

// 什么是接口？
// 接口是一种抽象类型，用来定义一组方法
// 接口只定义方法，不实现方法
// 实现类需要实现接口中定义的所有方法

// 为什么要定义接口？
// 1. 面向接口编程，降低耦合度
// 2. 方便扩展和维护
// 3. 支持多态
```

### 第8-34行：方法定义

```java
List<User> listUsers();  // 第8行

// 第8行详细解释：
// List<User>：返回类型，表示返回一个User列表
// listUsers：方法名
// ()：空参数列表
// ;：语句结束符
// 作用：定义一个方法，返回用户列表

// 为什么要定义这个方法？
// 查询所有用户，返回用户列表


User getUserById(Long id);  // 第10行

// 第10行详细解释：
// User：返回类型，表示返回一个User对象
// getUserById：方法名
// (Long id)：参数列表，接收一个Long类型的参数，名为id
// 作用：定义一个方法，根据id查询用户

// 为什么要定义这个方法？
// 根据用户id查询用户，返回用户对象


User getUserByUsername(String username);  // 第12行

// 第12行详细解释：
// User：返回类型，表示返回一个User对象
// getUserByUsername：方法名
// (String username)：参数列表，接收一个String类型的参数，名为username
// 作用：定义一个方法，根据用户名查询用户

// 为什么要定义这个方法？
// 根据用户名查询用户，返回用户对象
// 登录时需要根据用户名查询用户


boolean addUser(User user);  // 第14行

// 第14行详细解释：
// boolean：返回类型，表示返回一个布尔值（true或false）
// addUser：方法名
// (User user)：参数列表，接收一个User类型的参数，名为user
// 作用：定义一个方法，添加用户

// 为什么要定义这个方法？
// 添加用户，返回是否成功
// true表示成功，false表示失败


boolean updateUser(User user);  // 第16行

// 第16行详细解释：
// boolean：返回类型，表示返回一个布尔值（true或false）
// updateUser：方法名
// (User user)：参数列表，接收一个User类型的参数，名为user
// 作用：定义一个方法，更新用户

// 为什么要定义这个方法？
// 更新用户信息，返回是否成功


boolean deleteUser(Long id);  // 第18行

// 第18行详细解释：
// boolean：返回类型，表示返回一个布尔值（true或false）
// deleteUser：方法名
// (Long id)：参数列表，接收一个Long类型的参数，名为id
// 作用：定义一个方法，删除用户

// 为什么要定义这个方法？
// 根据id删除用户，返回是否成功


String register(String username, String password, String realName, String phone);  // 第20行

// 第20行详细解释：
// String：返回类型，表示返回一个字符串
// register：方法名
// (String username, String password, String realName, String phone)：参数列表
// 作用：定义一个方法，注册用户

// 为什么要定义这个方法？
// 用户注册，传入用户名、密码、真实姓名、手机号，返回结果


String login(String username, String password);  // 第22行

// 第22行详细解释：
// String：返回类型，表示返回一个字符串
// login：方法名
// (String username, String password)：参数列表
// 作用：定义一个方法，用户登录

// 为什么要定义这个方法？
// 用户登录，传入用户名和密码，返回结果


Page<User> listUsersByPage(Integer pageNum, Integer pageSize, String role, Integer status);  // 第24行

// 第24行详细解释：
// Page<User>：返回类型，表示返回一个分页对象
// listUsersByPage：方法名
// (Integer pageNum, Integer pageSize, String role, Integer status)：参数列表
// 作用：定义一个方法，分页查询用户

// 为什么要定义这个方法？
// 分页查询用户，传入页码、每页大小、角色、状态，返回分页结果


boolean deleteUser(Long id, Long currentUserId);  // 第26行

// 第26行详细解释：
// boolean：返回类型，表示返回一个布尔值（true或false）
// deleteUser：方法名
// (Long id, Long currentUserId)：参数列表
// 作用：定义一个方法，删除用户（带当前用户id）

// 为什么要定义这个方法？
// 删除用户时，不能删除当前登录用户，需要传入当前用户id


boolean updateStatus(Long id, Integer status);  // 第28行

// 第28行详细解释：
// boolean：返回类型，表示返回一个布尔值（true或false）
// updateStatus：方法名
// (Long id, Integer status)：参数列表
// 作用：定义一个方法，更新用户状态

// 为什么要定义这个方法？
// 更新用户状态（启用/禁用），返回是否成功


boolean resetPassword(Long id);  // 第30行

// 第30行详细解释：
// boolean：返回类型，表示返回一个布尔值（true或false）
// resetPassword：方法名
// (Long id)：参数列表
// 作用：定义一个方法，重置用户密码

// 为什么要定义这个方法？
// 重置用户密码为默认密码（123456），返回是否成功


Page<User> searchUsers(Integer pageNum, Integer pageSize, String keyword);  // 第32行

// 第32行详细解释：
// Page<User>：返回类型，表示返回一个分页对象
// searchUsers：方法名
// (Integer pageNum, Integer pageSize, String keyword)：参数列表
// 作用：定义一个方法，搜索用户

// 为什么要定义这个方法？
// 根据关键词搜索用户（用户名、手机号、真实姓名），返回分页结果
```

---

## 5️⃣ UserServiceImpl 实现类逐行讲解

### 第1-12行：package、import和类定义

```java
package com.school.campusexpress.service.impl;  // 第1行

// 第1行详细解释：
// package：Java关键字，表示"包"
// com.school.campusexpress.service.impl：包名
// 作用：告诉Java"这个UserServiceImpl类在com.school.campusexpress.service.impl包中"
// 如果删掉会怎样：编译错误，Java不知道这个类在哪个包中

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;  // 第2行

// 第2行详细解释：
// import：Java关键字，表示"导入"
// com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper：要导入的类
// 作用：导入LambdaQueryWrapper类，用来构造查询条件
// 如果删掉会怎样：无法使用LambdaQueryWrapper类

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;  // 第3行

// 第3行详细解释：
// 导入Page类，用来分页查询
// 如果删掉会怎样：无法使用Page类

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;  // 第4行

// 第4行详细解释：
// 导入ServiceImpl类，MyBatis-Plus提供的Service基类
// 如果删掉会怎样：无法使用ServiceImpl类

import com.school.campusexpress.entity.User;  // 第5行

// 第5行详细解释：
// 导入User实体类，用来表示用户
// 如果删掉会怎样：无法使用User类

import com.school.campusexpress.mapper.UserMapper;  // 第6行

// 第6行详细解释：
// 导入UserMapper接口，用来操作数据库
// 如果删掉会怎样：无法使用UserMapper接口

import com.school.campusexpress.service.UserService;  // 第7行

// 第7行详细解释：
// 导入UserService接口，用来实现业务逻辑
// 如果删掉会怎样：无法使用UserService接口

import com.school.campusexpress.util.PasswordUtil;  // 第8行

// 第8行详细解释：
// 导入PasswordUtil工具类，用来加密密码
// 如果删掉会怎样：无法使用PasswordUtil类

import org.springframework.stereotype.Service;  // 第9行

// 第9行详细解释：
// 导入Service注解，用来标记业务逻辑类
// 如果删掉会怎样：无法使用@Service注解

import org.springframework.util.StringUtils;  // 第10行

// 第10行详细解释：
// 导入StringUtils工具类，用来判断字符串是否为空
// 如果删掉会怎样：无法使用StringUtils类

import java.time.LocalDateTime;  // 第11行

// 第11行详细解释：
// 导入LocalDateTime类，用来表示日期时间
// 如果删掉会怎样：无法使用LocalDateTime类

import java.util.List;  // 第12行

// 第12行详细解释：
// 导入List接口，用来存储列表数据
// 如果删掉会怎样：无法使用List接口

@Service  // 第14行
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {  // 第15行

// 第14行详细解释：
// @Service：Spring注解，表示"业务逻辑类"
// 作用：告诉Spring"这个类是一个业务逻辑类，需要被管理"
// 没有它会怎样：Spring不会扫描这个类，无法注入到Controller中
// 在本项目中的作用：UserServiceImpl实现用户相关的业务逻辑

// 为什么要加@Service注解？
// Spring会自动扫描带有@Service注解的类，创建对象并管理
// Controller可以通过@Autowired注入这个对象


// 第15行详细解释：
// public：访问修饰符，表示"公开的"
// class：Java关键字，表示"类"
// UserServiceImpl：类名
// extends：Java关键字，表示"继承"
// ServiceImpl<UserMapper, User>：继承的父类，ServiceImpl是MyBatis-Plus提供的Service基类
// implements：Java关键字，表示"实现"
// UserService：实现的接口
// 作用：定义一个名为UserServiceImpl的类，继承ServiceImpl，实现UserService接口

// 为什么要继承ServiceImpl？
// ServiceImpl是MyBatis-Plus提供的Service基类，已经实现了很多通用方法
// 比如list()、getById()、save()、updateById()、removeById()等
// 继承ServiceImpl后，可以直接使用这些方法，不需要自己写

// 为什么要实现UserService？
// UserServiceImpl是UserService的实现类，需要实现UserService接口中定义的所有方法
```

### 第17-20行：listUsers方法

```java
@Override  // 第17行
public List<User> listUsers() {  // 第18行
    return list();  // 第19行
}  // 第20行

// 第17行详细解释：
// @Override：Java注解，表示"重写"
// 作用：告诉编译器"这个方法是重写父类或接口的方法"
// 没有它会怎样：不影响运行，但如果方法名写错了，编译器不会提示
// 在本项目中的作用：重写UserService接口的listUsers方法


// 第18行详细解释：
// public：访问修饰符，表示"公开的"
// List<User>：返回类型，表示返回一个User列表
// listUsers：方法名
// ()：空参数列表
// 作用：定义一个方法，返回用户列表


// 第19行详细解释：
// return list();
// return：Java关键字，表示"返回"
// list()：调用父类ServiceImpl的list方法
// 作用：调用父类的list方法，返回用户列表

// 为什么要调用父类的list方法？
// ServiceImpl已经实现了list方法，直接调用即可
// 不需要自己写SQL语句，MyBatis-Plus会自动生成SQL


// 第20行详细解释：
// }：方法结束
```

### 第22-25行：getUserById方法

```java
@Override  // 第22行
public User getUserById(Long id) {  // 第23行
    return getById(id);  // 第24行
}  // 第25行

// 第22行详细解释：
// @Override：Java注解，表示"重写"


// 第23行详细解释：
// public：访问修饰符，表示"公开的"
// User：返回类型，表示返回一个User对象
// getUserById：方法名
// (Long id)：参数列表，接收一个Long类型的参数，名为id
// 作用：定义一个方法，根据id查询用户


// 第24行详细解释：
// return getById(id);
// return：Java关键字，表示"返回"
// getById(id)：调用父类ServiceImpl的getById方法，传入id作为参数
// 作用：调用父类的getById方法，根据id查询用户

// 为什么要调用父类的getById方法？
// ServiceImpl已经实现了getById方法，直接调用即可
// 不需要自己写SQL语句，MyBatis-Plus会自动生成SQL


// 第25行详细解释：
// }：方法结束
```

### 第27-32行：getUserByUsername方法

```java
@Override  // 第27行
public User getUserByUsername(String username) {  // 第28行
    LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();  // 第29行
    wrapper.eq(User::getUsername, username);  // 第30行
    return getOne(wrapper);  // 第31行
}  // 第32行

// 第27行详细解释：
// @Override：Java注解，表示"重写"


// 第28行详细解释：
// public：访问修饰符，表示"公开的"
// User：返回类型，表示返回一个User对象
// getUserByUsername：方法名
// (String username)：参数列表，接收一个String类型的参数，名为username
// 作用：定义一个方法，根据用户名查询用户


// 第29行详细解释：
// LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
// LambdaQueryWrapper<User>：类型，表示一个Lambda查询包装器
// wrapper：变量名
// =：赋值运算符
// new：创建新对象
// LambdaQueryWrapper<>()：创建一个LambdaQueryWrapper对象
// 作用：创建一个LambdaQueryWrapper对象，用来构造查询条件

// 什么是LambdaQueryWrapper？
// LambdaQueryWrapper是MyBatis-Plus提供的查询条件构造器
// 用来构造查询条件，比如eq（等于）、like（模糊查询）、gt（大于）等
// 使用Lambda表达式，类型安全，不容易出错


// 第30行详细解释：
// wrapper.eq(User::getUsername, username);
// wrapper.eq()：调用LambdaQueryWrapper的eq方法
// User::getUsername：方法引用，表示User类的getUsername方法
// username：参数
// 作用：构造查询条件，username字段等于传入的username参数

// User::getUsername是什么意思？
// 这是Java8的方法引用语法
// 表示引用User类的getUsername方法
// 相当于 user -> user.getUsername()
// 但更简洁

// eq是什么意思？
// eq是equal的缩写，表示"等于"
// 相当于SQL中的 WHERE username = ?


// 第31行详细解释：
// return getOne(wrapper);
// return：Java关键字，表示"返回"
// getOne(wrapper)：调用父类ServiceImpl的getOne方法，传入wrapper作为参数
// 作用：调用父类的getOne方法，根据查询条件查询用户

// 为什么要调用getOne方法？
// 根据用户名查询用户，用户名是唯一的，所以用getOne方法
// 如果查询到多条记录，会抛出异常


// 第32行详细解释：
// }：方法结束
```

### 第34-50行：addUser方法

```java
@Override  // 第34行
public boolean addUser(User user) {  // 第35行
    User existUser = getUserByUsername(user.getUsername());  // 第36行
    if (existUser != null) {  // 第37行
        throw new RuntimeException("用户名已存在");  // 第38行
    }  // 第39行
    
    user.setRole("user");  // 第41行
    user.setStatus(1);  // 第42行
    user.setCreateTime(LocalDateTime.now());  // 第43行
    user.setUpdateTime(LocalDateTime.now());  // 第44行
    return save(user);  // 第45行
}  // 第46行

// 第34行详细解释：
// @Override：Java注解，表示"重写"


// 第35行详细解释：
// public：访问修饰符，表示"公开的"
// boolean：返回类型，表示返回一个布尔值（true或false）
// addUser：方法名
// (User user)：参数列表，接收一个User类型的参数，名为user
// 作用：定义一个方法，添加用户


// 第36行详细解释：
// User existUser = getUserByUsername(user.getUsername());
// User existUser：声明一个User类型的变量，名为existUser
// =：赋值运算符
// getUserByUsername(user.getUsername())：调用getUserByUsername方法，传入user.getUsername()作为参数
// 作用：根据用户名查询用户，判断用户名是否已存在

// 为什么要查询用户？
// 添加用户前，需要判断用户名是否已存在
// 如果用户名已存在，就不能添加


// 第37行详细解释：
// if (existUser != null) {
// if：Java关键字，表示"如果"
// (existUser != null)：判断条件，意思是"existUser不为空"
// 作用：如果existUser不为空，就执行大括号里的代码

// !=：不等于运算符
// null：表示"空"、"没有值"
// existUser != null：existUser不为空，即用户名已存在


// 第38行详细解释：
// throw new RuntimeException("用户名已存在");
// throw：Java关键字，表示"抛出异常"
// new：创建新对象
// RuntimeException：运行时异常类
// ("用户名已存在")：给异常添加错误信息
// 作用：抛出一个异常，告诉调用者"用户名已存在"

// 为什么要抛出异常？
// 用户名已存在，不能添加用户，需要告诉调用者
// 抛出异常后，Controller会捕获异常，返回错误信息给前端


// 第39行详细解释：
// }：if语句结束


// 第41行详细解释：
// user.setRole("user");
// user.setRole()：调用User对象的setRole方法
// ("user")：传入的参数，表示设置为"user"
// 作用：设置用户角色为"user"（普通用户）

// 为什么要设置角色？
// 添加用户时，默认角色是"user"（普通用户）
// 管理员角色需要手动设置


// 第42行详细解释：
// user.setStatus(1);
// user.setStatus()：调用User对象的setStatus方法
// (1)：传入的参数，表示设置为1
// 作用：设置用户状态为1（正常）

// 为什么要设置状态？
// 添加用户时，默认状态是1（正常）
// 0表示禁用


// 第43行详细解释：
// user.setCreateTime(LocalDateTime.now());
// user.setCreateTime()：调用User对象的setCreateTime方法
// LocalDateTime.now()：获取当前时间
// 作用：设置用户创建时间为当前时间

// 为什么要设置创建时间？
// 记录用户什么时候注册的


// 第44行详细解释：
// user.setUpdateTime(LocalDateTime.now());
// user.setUpdateTime()：调用User对象的setUpdateTime方法
// LocalDateTime.now()：获取当前时间
// 作用：设置用户更新时间为当前时间

// 为什么要设置更新时间？
// 记录用户信息最后一次什么时候修改的


// 第45行详细解释：
// return save(user);
// return：Java关键字，表示"返回"
// save(user)：调用父类ServiceImpl的save方法，传入user作为参数
// 作用：调用父类的save方法，保存用户到数据库

// 为什么要调用save方法？
// save方法会自动生成INSERT语句，插入数据到数据库


// 第46行详细解释：
// }：方法结束
```

---

## 6️⃣ 初学者常见误解

**误解1**：Service接口可以省略，直接用实现类
- ❌ 错误：省略Service接口，违反面向接口编程原则
- ✅ 正确：应该定义Service接口，实现类实现接口

**误解2**：可以直接在Controller写业务逻辑
- ❌ 错误：违反三层架构原则，代码混乱，难以维护
- ✅ 正确：业务逻辑应该在Service中处理

**误解3**：@Service注解可以省略
- ❌ 错误：省略@Service注解，Spring不会扫描这个类
- ✅ 建议：始终写上@Service注解

**误解4**：可以不继承ServiceImpl
- ⚠️ 可以不继承，但需要自己实现很多方法
- ✅ 建议：继承ServiceImpl，使用MyBatis-Plus提供的通用方法

**误解5**：LambdaQueryWrapper和QueryWrapper是一样的
- ❌ 错误：LambdaQueryWrapper使用Lambda表达式，类型安全；QueryWrapper使用字符串，容易出错
- ✅ 正确：推荐使用LambdaQueryWrapper

---

## 7️⃣ 答辩时一句话怎么说

"UserService是业务逻辑接口，定义了用户相关的业务逻辑方法；UserServiceImpl是业务逻辑实现类，实现了UserService接口，具体实现业务逻辑。Service层在三层架构中位于Controller和Mapper之间，处理业务逻辑，调用Mapper操作数据库。"

---

## 8️⃣ 毕设答辩准备

**问题1**：请解释一下UserService和UserServiceImpl的区别
**回答**：UserService是业务逻辑接口，定义了用户相关的业务逻辑方法；UserServiceImpl是业务逻辑实现类，实现了UserService接口，具体实现业务逻辑。

**问题2**：为什么需要Service层？
**回答**：Service层处理业务逻辑，调用Mapper操作数据库，遵循三层架构原则，Controller只负责接收HTTP请求，不处理业务逻辑。

**问题3**：为什么不能直接在Controller写数据库代码？
**回答**：直接在Controller写数据库代码，违反三层架构原则，业务逻辑和Controller混在一起，代码混乱，难以维护，业务逻辑无法被多个Controller复用。

**问题4**：@Service注解有什么作用？
**回答**：@Service注解告诉Spring这个类是一个业务逻辑类，需要被管理，Spring会自动扫描这个类，创建对象并管理，Controller可以通过@Autowired注入这个对象。

**问题5**：为什么要继承ServiceImpl？
**回答**：ServiceImpl是MyBatis-Plus提供的Service基类，已经实现了很多通用方法，比如list()、getById()、save()、updateById()、removeById()等，继承ServiceImpl后，可以直接使用这些方法，不需要自己写。

**问题6**：什么是LambdaQueryWrapper？
**回答**：LambdaQueryWrapper是MyBatis-Plus提供的查询条件构造器，用来构造查询条件，比如eq（等于）、like（模糊查询）、gt（大于）等，使用Lambda表达式，类型安全，不容易出错。

**问题7**：User::getUsername是什么意思？
**回答**：User::getUsername是Java8的方法引用语法，表示引用User类的getUsername方法，相当于 user -> user.getUsername()，但更简洁。

**问题8**：为什么要调用getUserByUsername方法？
**回答**：添加用户前，需要判断用户名是否已存在，如果用户名已存在，就不能添加用户，所以需要调用getUserByUsername方法查询用户。

**问题9**：为什么要抛出异常？
**回答**：用户名已存在，不能添加用户，需要告诉调用者，抛出异常后，Controller会捕获异常，返回错误信息给前端。

**问题10**：LocalDateTime.now()是干什么的？
**回答**：LocalDateTime.now()是Java8的日期时间API，用来获取当前时间，设置用户的创建时间和更新时间。
