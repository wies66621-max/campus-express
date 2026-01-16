# UserController 逐行讲解（零基础版）

## 📚 学习目标
- 理解什么是Controller（控制器）
- 理解UserController中每一行代码的作用
- 理解Controller、Service、Mapper的关系
- 能在答辩时用自己的话说出UserController的作用

---

## 1️⃣ 这个文件是干什么的（白话）

UserController 是一个**控制器类**，用来处理用户相关的HTTP请求。

就像生活中：
- **Controller** = 接待员（接收客人的请求，安排相应的服务）
- **Service** = 服务员（处理具体的业务逻辑）
- **Mapper** = 厨师（操作数据库，获取数据）

在程序中：
- **UserController** = 接收前端的HTTP请求（注册、登录、查询用户等），调用Service处理业务逻辑，返回结果给前端

---

## 2️⃣ 不写这个文件会出现什么问题

1. **无法处理HTTP请求**：前端无法调用用户相关的接口
2. **无法返回数据**：前端无法获取用户数据
3. **前后端无法交互**：前端和后端无法通信

---

## 3️⃣ 文件整体结构说明

```
UserController.java
├── package声明（第1行）
├── import导入（第2-15行）
├── 类定义和注解（第17-18行）
├── 字段定义（第20-22行）
├── 方法定义（第24-141行）
│   ├── register方法（第24-31行）
│   ├── login方法（第33-46行）
│   ├── getUserById方法（第48-53行）
│   ├── listUsers方法（第55-61行）
│   ├── addUser方法（第63-68行）
│   ├── updateUser方法（第70-75行）
│   ├── deleteUser方法（第77-82行）
│   ├── logout方法（第84-86行）
│   ├── getUserInfo方法（第88-96行）
│   └── getUserInfoWithoutPassword方法（第98-100行）
```

---

## 4️⃣ 逐行代码解释

### 第1-15行：package和import

```java
package com.school.campusexpress.controller;  // 第1行

// 第1行详细解释：
// package：Java关键字，表示"包"
// com.school.campusexpress.controller：包名
// 作用：告诉Java"这个UserController类在com.school.campusexpress.controller包中"
// 为什么这么写：按照公司域名倒序 + 项目名 + 模块名
// 如果删掉会怎样：编译错误，Java不知道这个类在哪个包中

import com.school.campusexpress.annotation.RequireAuth;  // 第2行

// 第2行详细解释：
// import：Java关键字，表示"导入"
// com.school.campusexpress.annotation.RequireAuth：要导入的类
// 作用：导入RequireAuth注解，用来验证用户权限
// 如果删掉会怎样：无法使用@RequireAuth注解

import com.school.campusexpress.common.R;  // 第3行

// 第3行详细解释：
// 导入R类，用来封装返回结果
// R类包含success和error方法，用来返回成功或失败的结果
// 如果删掉会怎样：无法使用R类

import com.school.campusexpress.entity.User;  // 第4行

// 第4行详细解释：
// 导入User实体类，用来表示用户
// 如果删掉会怎样：无法使用User类

import com.school.campusexpress.service.UserService;  // 第5行

// 第5行详细解释：
// 导入UserService接口，用来调用用户相关的业务逻辑
// 如果删掉会怎样：无法使用UserService接口

import com.school.campusexpress.util.JwtUtil;  // 第6行

// 第6行详细解释：
// 导入JwtUtil工具类，用来生成和解析JWT token
// 如果删掉会怎样：无法使用JwtUtil类

import io.swagger.v3.oas.annotations.Operation;  // 第7行

// 第7行详细解释：
// 导入Operation注解，用来生成API文档
// 如果删掉会怎样：无法使用@Operation注解

import io.swagger.v3.oas.annotations.Parameter;  // 第8行

// 第8行详细解释：
// 导入Parameter注解，用来生成API文档的参数说明
// 如果删掉会怎样：无法使用@Parameter注解

import io.swagger.v3.oas.annotations.tags.Tag;  // 第9行

// 第9行详细解释：
// 导入Tag注解，用来生成API文档的分组
// 如果删掉会怎样：无法使用@Tag注解

import jakarta.servlet.http.HttpServletRequest;  // 第10行

// 第10行详细解释：
// 导入HttpServletRequest类，用来获取HTTP请求的信息
// 如果删掉会怎样：无法使用HttpServletRequest类

import jakarta.validation.Valid;  // 第11行

// 第11行详细解释：
// 导入Valid注解，用来验证参数
// 如果删掉会怎样：无法使用@Valid注解

import org.springframework.beans.factory.annotation.Autowired;  // 第12行

// 第12行详细解释：
// 导入Autowired注解，用来自动注入对象
// 如果删掉会怎样：无法使用@Autowired注解

import org.springframework.web.bind.annotation.*;  // 第13行

// 第13行详细解释：
// 导入Spring MVC的注解，包括@RestController、@RequestMapping、@PostMapping、@GetMapping等
// *：通配符，表示导入这个包下的所有类
// 如果删掉会怎样：无法使用Spring MVC的注解

import java.util.HashMap;  // 第14行

// 第14行详细解释：
// 导入HashMap类，用来存储键值对数据
// 如果删掉会怎样：无法使用HashMap类

import java.util.List;  // 第15行

// 第15行详细解释：
// 导入List接口，用来存储列表数据
// 如果删掉会怎样：无法使用List接口

import java.util.Map;  // 第16行

// 第16行详细解释：
// 导入Map接口，用来存储键值对数据
// 如果删掉会怎样：无法使用Map接口

import java.util.stream.Collectors;  // 第17行

// 第17行详细解释：
// 导入Collectors类，用来进行流式操作
// 如果删掉会怎样：无法使用Collectors类
```

### 第17-18行：类定义和注解

```java
@Tag(name = "用户管理", description = "用户注册、登录、信息管理接口")  // 第17行
@RestController  // 第18行
@RequestMapping("/user")  // 第19行
public class UserController {  // 第20行

// 第17行详细解释：
// @Tag：Swagger注解，用来生成API文档
// (name = "用户管理")：注解的参数，表示API文档的分组名称
// (description = "用户注册、登录、信息管理接口")：注解的参数，表示API文档的分组描述
// 作用：告诉Swagger"这个UserController类的接口属于'用户管理'分组"
// 没有它会怎样：API文档中不会显示这个分组
// 在本项目中的作用：在Swagger API文档中，用户相关的接口会显示在"用户管理"分组下

// 什么是Swagger？
// Swagger是一个API文档生成工具，可以根据代码自动生成API文档
// 前端开发人员可以通过Swagger查看接口文档，了解接口的URL、参数、返回值等


// 第18行详细解释：
// @RestController：Spring MVC注解
// 作用：告诉Spring"这个类是一个控制器，处理HTTP请求，返回JSON数据"
// 没有它会怎样：Spring不会扫描这个类，无法处理HTTP请求
// 在本项目中的作用：UserController用来处理用户相关的HTTP请求，返回JSON数据

// @RestController和@Controller有什么区别？
// @Controller：返回视图（HTML页面）
// @RestController：返回JSON数据
// 本项目是前后端分离架构，所以用@RestController


// 第19行详细解释：
// @RequestMapping：Spring MVC注解
// ("/user")：注解的参数，表示URL路径
// 作用：告诉Spring"这个类中所有方法的URL都以/user开头"
// 没有它会怎样：无法统一设置URL前缀
// 在本项目中的作用：用户相关接口的URL都是 /user/xxx（如/user/login、/user/register）

// 举例：
// @PostMapping("/login") 的完整URL是 http://localhost:8080/user/login
// @GetMapping("/list") 的完整URL是 http://localhost:8080/user/list


// 第20行详细解释：
// public：访问修饰符，表示"公开的"
// class：Java关键字，表示"类"
// UserController：类名
// 作用：定义一个名为UserController的类
// 如果删掉public会怎样：其他类无法访问UserController类
```

### 第20-22行：字段定义

```java
@Autowired  // 第22行
private UserService userService;  // 第23行

// 第22行详细解释：
// @Autowired：Spring注解，表示"自动注入"
// 作用：告诉Spring"自动帮我注入这个UserService对象"
// 没有它会怎样：userService会是null，无法使用
// 在本项目中的作用：自动注入UserService对象，用来调用业务逻辑方法

// 什么是依赖注入？
// 依赖注入（Dependency Injection，简称DI）是Spring的核心功能
// 不需要手动创建对象，Spring会自动创建并注入对象
// 比如不需要写 UserService userService = new UserServiceImpl();
// 只需要写 @Autowired private UserService userService;
// Spring会自动创建UserService对象并注入到UserController中


// 第23行详细解释：
// private：访问修饰符，表示"私有的"
// UserService：接口类型
// userService：变量名
// 作用：定义一个私有的UserService类型的变量，名为userService
// 为什么用接口而不是实现类？
// 面向接口编程，降低耦合度
// 如果将来需要更换实现类，只需要修改配置，不需要修改代码


@Autowired  // 第24行
private JwtUtil jwtUtil;  // 第25行

// 第24行详细解释：
// @Autowired：Spring注解，表示"自动注入"
// 作用：告诉Spring"自动帮我注入这个JwtUtil对象"
// 没有它会怎样：jwtUtil会是null，无法使用
// 在本项目中的作用：自动注入JwtUtil对象，用来生成和解析JWT token


// 第25行详细解释：
// private：访问修饰符，表示"私有的"
// JwtUtil：工具类类型
// jwtUtil：变量名
// 作用：定义一个私有的JwtUtil类型的变量，名为jwtUtil
```

### 第27-31行：register方法

```java
@Operation(summary = "用户注册")  // 第27行
@PostMapping("/register")  // 第28行
public R<String> register(@Valid @RequestBody User user) {  // 第29行
    try {  // 第30行
        String result = userService.register(user.getUsername(), user.getPassword(), user.getRealName(), user.getPhone());  // 第31行
        return R.success(result);  // 第32行
    } catch (RuntimeException e) {  // 第33行
        return R.error(e.getMessage());  // 第34行
    }  // 第35行
}  // 第36行

// 第27行详细解释：
// @Operation：Swagger注解，用来生成API文档
// (summary = "用户注册")：注解的参数，表示接口的简要说明
// 作用：告诉Swagger"这个方法的简要说明是'用户注册'"
// 没有它会怎样：API文档中不会显示这个接口的说明
// 在本项目中的作用：在Swagger API文档中，这个接口会显示为"用户注册"


// 第28行详细解释：
// @PostMapping：Spring MVC注解，表示"处理POST请求"
// ("/register")：注解的参数，表示URL路径
// 作用：告诉Spring"这个方法处理POST请求，URL是/user/register"
// 没有它会怎样：无法处理注册请求
// 在本项目中的作用：处理用户注册请求，URL是 http://localhost:8080/user/register

// POST请求和GET请求有什么区别？
// POST请求：用来提交数据，比如注册、登录、添加用户等
// GET请求：用来获取数据，比如查询用户、查询快递列表等
// 注册需要提交数据（用户名、密码、真实姓名、手机号），所以用POST请求


// 第29行详细解释：
// public：访问修饰符，表示"公开的"
// R<String>：返回类型，表示返回一个R对象，R对象的泛型参数是String
// register：方法名
// (@Valid @RequestBody User user)：方法参数
// 作用：定义一个公开的方法，返回类型是R<String>，方法名是register，接收一个User对象作为参数

// R<String>是什么意思？
// R是一个封装类，用来统一返回结果
// R.success(result)：返回成功结果，result是返回的数据
// R.error(message)：返回失败结果，message是错误信息
// R<String>表示R对象中封装的数据是String类型

// @Valid：验证注解，表示"验证这个参数"
// @RequestBody：Spring MVC注解，表示"把HTTP请求体中的JSON数据转换成User对象"
// User user：方法参数，类型是User，变量名是user


// 第30行详细解释：
// try：Java关键字，表示"尝试执行"
// 作用：尝试执行try块中的代码，如果出现异常，就跳转到catch块
// 如果删掉会怎样：无法捕获异常，程序会直接崩溃


// 第31行详细解释：
// String result = userService.register(user.getUsername(), user.getPassword(), user.getRealName(), user.getPhone());
// String result：声明一个String类型的变量，名为result
// =：赋值运算符
// userService.register()：调用UserService的register方法
// (user.getUsername(), user.getPassword(), user.getRealName(), user.getPhone())：传入的参数
// 作用：调用UserService的register方法，传入用户名、密码、真实姓名、手机号，返回结果赋给result变量

// user.getUsername()：获取user对象的username属性
// user.getPassword()：获取user对象的password属性
// user.getRealName()：获取user对象的realName属性
// user.getPhone()：获取user对象的phone属性

// 为什么要调用UserService的register方法？
// UserController只负责接收HTTP请求，不处理业务逻辑
// 业务逻辑由UserService处理
// 这是三层架构的设计原则：Controller -> Service -> Mapper


// 第32行详细解释：
// return R.success(result);
// return：Java关键字，表示"返回"
// R.success(result)：调用R类的success方法，传入result作为参数
// 作用：返回成功结果，result是返回的数据

// 如果result是"注册成功"，那么返回的结果是：
// {
//   "code": 200,
//   "message": "操作成功",
//   "data": "注册成功"
// }


// 第33行详细解释：
// } catch (RuntimeException e) {
// }：catch块，用来捕获异常
// RuntimeException e：捕获的异常类型，变量名是e
// 作用：如果try块中出现RuntimeException异常，就执行catch块中的代码

// 什么是异常？
// 异常就是程序运行时出现的错误
// 比如用户名已存在、密码错误、数据库连接失败等
// 如果不捕获异常，程序会直接崩溃


// 第34行详细解释：
// return R.error(e.getMessage());
// return：Java关键字，表示"返回"
// R.error(e.getMessage())：调用R类的error方法，传入e.getMessage()作为参数
// 作用：返回失败结果，e.getMessage()是异常的错误信息

// e.getMessage()：获取异常的错误信息
// 比如异常是"用户名已存在"，那么e.getMessage()就是"用户名已存在"

// 返回的结果是：
// {
//   "code": 500,
//   "message": "用户名已存在",
//   "data": null
// }
```

### 第38-46行：login方法

```java
@Operation(summary = "用户登录")  // 第38行
@PostMapping("/login")  // 第39行
public R<Map<String, Object>> login(@Valid @RequestBody User user) {  // 第40行
    try {  // 第41行
        String result = userService.login(user.getUsername(), user.getPassword());  // 第42行
        User dbUser = userService.getUserByUsername(user.getUsername());  // 第43行
        
        String token = jwtUtil.generateToken(dbUser.getId(), dbUser.getUsername(), dbUser.getRole());  // 第45行
        
        Map<String, Object> data = new HashMap<>();  // 第47行
        data.put("token", token);  // 第48行
        data.put("user", getUserInfoWithoutPassword(dbUser));  // 第49行
        
        return R.success(data);  // 第51行
    } catch (RuntimeException e) {  // 第52行
        return R.error(e.getMessage());  // 第53行
    }  // 第54行
}  // 第55行

// 第38行详细解释：
// @Operation：Swagger注解，用来生成API文档
// (summary = "用户登录")：注解的参数，表示接口的简要说明
// 作用：告诉Swagger"这个方法的简要说明是'用户登录'"
// 没有它会怎样：API文档中不会显示这个接口的说明


// 第39行详细解释：
// @PostMapping：Spring MVC注解，表示"处理POST请求"
// ("/login")：注解的参数，表示URL路径
// 作用：告诉Spring"这个方法处理POST请求，URL是/user/login"
// 没有它会怎样：无法处理登录请求
// 在本项目中的作用：处理用户登录请求，URL是 http://localhost:8080/user/login


// 第40行详细解释：
// public：访问修饰符，表示"公开的"
// R<Map<String, Object>>：返回类型，表示返回一个R对象，R对象的泛型参数是Map<String, Object>
// login：方法名
// (@Valid @RequestBody User user)：方法参数
// 作用：定义一个公开的方法，返回类型是R<Map<String, Object>>，方法名是login，接收一个User对象作为参数

// Map<String, Object>是什么意思？
// Map是一个接口，用来存储键值对数据
// <String, Object>：泛型参数，表示键是String类型，值是Object类型
// Map<String, Object>表示一个键值对集合，键是字符串，值可以是任何类型

// 为什么要用Map<String, Object>？
// 因为登录需要返回多个数据：token和user信息
// 用Map可以存储多个键值对


// 第41行详细解释：
// try：Java关键字，表示"尝试执行"
// 作用：尝试执行try块中的代码，如果出现异常，就跳转到catch块


// 第42行详细解释：
// String result = userService.login(user.getUsername(), user.getPassword());
// String result：声明一个String类型的变量，名为result
// =：赋值运算符
// userService.login()：调用UserService的login方法
// (user.getUsername(), user.getPassword())：传入的参数
// 作用：调用UserService的login方法，传入用户名和密码，返回结果赋给result变量


// 第43行详细解释：
// User dbUser = userService.getUserByUsername(user.getUsername());
// User dbUser：声明一个User类型的变量，名为dbUser
// =：赋值运算符
// userService.getUserByUsername()：调用UserService的getUserByUsername方法
// (user.getUsername())：传入的参数
// 作用：调用UserService的getUserByUsername方法，传入用户名，返回用户对象赋给dbUser变量

// 为什么要查询用户？
// 登录成功后，需要生成JWT token，token中包含用户的信息（id、username、role）
// 所以需要查询用户，获取用户的信息


// 第45行详细解释：
// String token = jwtUtil.generateToken(dbUser.getId(), dbUser.getUsername(), dbUser.getRole());
// String token：声明一个String类型的变量，名为token
// =：赋值运算符
// jwtUtil.generateToken()：调用JwtUtil的generateToken方法
// (dbUser.getId(), dbUser.getUsername(), dbUser.getRole())：传入的参数
// 作用：调用JwtUtil的generateToken方法，传入用户id、用户名、角色，生成token赋给token变量

// 什么是JWT token？
// JWT（JSON Web Token）是一种JSON格式的token，用来在前后端之间传递用户信息
// token中包含用户的信息（id、username、role等）
// 前端每次请求都需要带上token，后端解析token获取用户信息

// 为什么要生成token？
// 前端登录成功后，后端返回token给前端
// 前端保存token，每次请求都带上token
// 后端解析token，获取用户信息，判断用户是否有权限


// 第47行详细解释：
// Map<String, Object> data = new HashMap<>();
// Map<String, Object> data：声明一个Map<String, Object>类型的变量，名为data
// =：赋值运算符
// new：创建新对象
// HashMap<>()：创建一个HashMap对象
// 作用：创建一个新的HashMap对象，用来存储token和user信息

// HashMap是什么？
// HashMap是Map接口的一个实现类，用来存储键值对数据
// 比如data.put("token", token)表示存储一个键值对，键是"token"，值是token变量


// 第48行详细解释：
// data.put("token", token);
// data.put()：调用HashMap的put方法，存储键值对
// ("token", token)：键是"token"，值是token变量
// 作用：在Map中存储一个键值对，键是"token"，值是token变量


// 第49行详细解释：
// data.put("user", getUserInfoWithoutPassword(dbUser));
// data.put()：调用HashMap的put方法，存储键值对
// ("user", getUserInfoWithoutPassword(dbUser))：键是"user"，值是getUserInfoWithoutPassword(dbUser)的返回值
// getUserInfoWithoutPassword(dbUser)：调用getUserInfoWithoutPassword方法，传入dbUser作为参数
// 作用：在Map中存储一个键值对，键是"user"，值是不包含密码的用户信息

// 为什么要调用getUserInfoWithoutPassword方法？
// 密码是敏感信息，不应该返回给前端
// 所以需要把密码设置为null，再返回给前端


// 第51行详细解释：
// return R.success(data);
// return：Java关键字，表示"返回"
// R.success(data)：调用R类的success方法，传入data作为参数
// 作用：返回成功结果，data是返回的数据

// 返回的结果是：
// {
//   "code": 200,
//   "message": "操作成功",
//   "data": {
//     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
//     "user": {
//       "id": 1,
//       "username": "admin",
//       "realName": "管理员",
//       "phone": "13800138000",
//       "role": "admin",
//       "status": 1,
//       "createTime": "2024-01-01T00:00:00",
//       "updateTime": "2024-01-01T00:00:00"
//     }
//   }
// }


// 第52行详细解释：
// } catch (RuntimeException e) {
// }：catch块，用来捕获异常


// 第53行详细解释：
// return R.error(e.getMessage());
// return：Java关键字，表示"返回"
// R.error(e.getMessage())：调用R类的error方法，传入e.getMessage()作为参数
// 作用：返回失败结果，e.getMessage()是异常的错误信息
```

### 第98-100行：getUserInfoWithoutPassword方法

```java
private User getUserInfoWithoutPassword(User user) {  // 第98行
    user.setPassword(null);  // 第99行
    return user;  // 第100行
}  // 第101行

// 第98行详细解释：
// private：访问修饰符，表示"私有的"，只有本类可以访问
// User：返回类型，表示返回一个User对象
// getUserInfoWithoutPassword：方法名
// (User user)：方法参数
// 作用：定义一个私有的方法，返回类型是User，方法名是getUserInfoWithoutPassword，接收一个User对象作为参数

// 为什么要定义这个方法？
// 密码是敏感信息，不应该返回给前端
// 所以需要把密码设置为null，再返回给前端
// 这个方法被多次调用（login、getUserById、getUserInfo等），所以提取成一个方法


// 第99行详细解释：
// user.setPassword(null);
// user.setPassword()：调用User对象的setPassword方法
// (null)：传入的参数，表示设置为null
// 作用：把user对象的password属性设置为null

// null是什么？
// null表示"空"、"没有值"
// 把password设置为null，表示密码为空


// 第100行详细解释：
// return user;
// return：Java关键字，表示"返回"
// user：返回的值
// 作用：返回user对象

// 返回的user对象中，password是null
// 前端收到的用户信息中，不包含密码
```

---

## 5️⃣ 初学者常见误解

**误解1**：Controller可以直接操作数据库
- ❌ 错误：Controller只负责接收HTTP请求，不操作数据库
- ✅ 正确：Controller -> Service -> Mapper -> 数据库

**误解2**：@Autowired可以省略
- ❌ 错误：省略@Autowired，对象会是null，无法使用
- ✅ 建议：始终写上@Autowired注解

**误解3**：@PostMapping和@GetMapping可以混用
- ❌ 错误：POST请求用@PostMapping，GET请求用@GetMapping
- ✅ 正确：根据请求类型选择对应的注解

**误解4**：返回类型可以是任意类型
- ⚠️ 可以是任意类型，但建议统一用R<T>封装返回结果
- ✅ 建议：统一用R<T>封装返回结果，方便前端处理

**误解5**：try-catch可以省略
- ⚠️ 可以省略，但建议加上，防止程序崩溃
- ✅ 建议：始终加上try-catch，捕获异常并返回友好的错误信息

---

## 6️⃣ 答辩时一句话怎么说

"UserController是控制器，用来处理用户相关的HTTP请求，比如注册、登录、查询用户等，它接收前端的请求，调用Service处理业务逻辑，返回结果给前端。"

---

## 7️⃣ 毕设答辩准备

**问题1**：请解释一下UserController的作用
**回答**：UserController是控制器，用来处理用户相关的HTTP请求，比如注册、登录、查询用户等，它接收前端的请求，调用Service处理业务逻辑，返回结果给前端。

**问题2**：@RestController注解有什么作用？
**回答**：@RestController注解告诉Spring这个类是一个控制器，处理HTTP请求，返回JSON数据。

**问题3**：@RequestMapping注解是干什么的？
**回答**：@RequestMapping注解告诉Spring这个类中所有方法的URL都以/user开头，比如/user/login、/user/register。

**问题4**：@PostMapping和@GetMapping有什么区别？
**回答**：@PostMapping处理POST请求，用来提交数据，比如注册、登录；@GetMapping处理GET请求，用来获取数据，比如查询用户。

**问题5**：@Autowired注解是干什么的？
**回答**：@Autowired注解告诉Spring自动帮我注入这个对象，这样就不需要手动创建对象了。

**问题6**：为什么要调用UserService的register方法？
**回答**：UserController只负责接收HTTP请求，不处理业务逻辑，业务逻辑由UserService处理，这是三层架构的设计原则。

**问题7**：为什么要生成JWT token？
**回答**：前端登录成功后，后端返回token给前端，前端保存token，每次请求都带上token，后端解析token，获取用户信息，判断用户是否有权限。

**问题8**：为什么要调用getUserInfoWithoutPassword方法？
**回答**：密码是敏感信息，不应该返回给前端，所以需要把密码设置为null，再返回给前端。

**问题9**：R<T>是什么意思？
**回答**：R是一个封装类，用来统一返回结果，R.success(data)返回成功结果，R.error(message)返回失败结果，R<T>表示R对象中封装的数据是T类型。

**问题10**：Map<String, Object>是什么意思？
**回答**：Map是一个接口，用来存储键值对数据，Map<String, Object>表示一个键值对集合，键是字符串，值可以是任何类型，登录需要返回多个数据（token和user信息），所以用Map存储。
