# JWT 工具类逐行讲解（零基础版）

## 📚 学习目标
- 从零开始理解什么是Token
- 理解JWT（JSON Web Token）的概念
- 理解Token和Session的区别
- 理解为什么Token要放在请求头
- 理解claims是什么（用"字典/Map"类比）
- 理解JwtUtil中每一行代码的作用
- 能在答辩时用自己的话说出JWT的作用

---

## 1️⃣ 这个文件是干什么的（白话）

JwtUtil 是一个**JWT工具类**，用来生成和解析JWT token。

就像生活中：
- **Token** = 通行证（比如景区门票、演唱会门票）
- **JWT** = 一种格式的通行证（JSON格式的通行证）
- **JwtUtil** = 生成和验证通行证的机器

在程序中：
- **JWT** = 一种JSON格式的token，用来在前后端之间传递用户信息
- **JwtUtil** = 生成JWT token和解析JWT token的工具类

---

## 2️⃣ 不写这个文件会出现什么问题

1. **无法生成token**：用户登录后，无法生成token返回给前端
2. **无法解析token**：前端请求时，无法解析token获取用户信息
3. **无法验证token**：无法验证token是否有效、是否过期
4. **前后端无法通信**：前端和后端无法通过token进行身份认证

---

## 3️⃣ 什么是 Token（从零开始）

### ① Token 是什么（白话）

**Token（令牌）** = 通行证

就像生活中：
- **景区门票** = 进入景区的token
- **演唱会门票** = 进入演唱会的token
- **身份证** = 证明身份的token

在程序中：
- **Token** = 证明身份的字符串
- 前端登录成功后，后端返回token给前端
- 前端保存token，每次请求都带上token
- 后端解析token，获取用户信息，判断用户是否有权限

### ② Token 是什么（技术角度）

**Token** 是一个**字符串**，用来在前后端之间传递用户信息。

```java
// Token示例
String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiYWRtaW4iLCJyb2xlIjoiYWRtaW4ifQ.signature";

// Token由三部分组成，用点号（.）分隔
// 第一部分：Header（头部）
// 第二部分：Payload（负载）
// 第三部分：Signature（签名）

// Header（头部）：描述token的元数据
// {
//   "alg": "HS512",  // 签名算法
//   "typ": "JWT"     // token类型
// }

// Payload（负载）：存储用户信息
// {
//   "userId": 1,       // 用户id
//   "username": "admin",  // 用户名
//   "role": "admin",       // 角色
//   "exp": 1704067200  // 过期时间
// }

// Signature（签名）：防止token被篡改
// 用密钥对Header和Payload进行签名
```

### ③ Token 和 Session 的区别（只讲概念）

| | Token | Session |
|---|---|---|
| 存储位置 | 存储在客户端（浏览器） | 存储在服务端（服务器） |
| 存储方式 | 存储在localStorage、cookie等 | 存储在内存、数据库等 |
| 传输方式 | 每次请求都带上token | 每次请求都带上sessionId |
| 跨域支持 | 支持跨域 | 不支持跨域（需要特殊处理） |
| 扩展性 | 容易扩展（微服务） | 不容易扩展（需要共享Session） |

**白话解释**：
- **Token** = 通行证（自己拿着，去哪里都行）
- **Session** = 存包柜（存在服务端，每次都要去服务端查）

**举例**：
- **Token** = 景区门票（自己拿着，去哪个景点都可以）
- **Session** = 存包柜（存在景区，每次都要去服务台查）

### ④ Token 为什么要放在请求头

```javascript
// 前端代码（Vue3 + axios）
axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');  // 从localStorage获取token
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;  // 把token放在请求头
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 为什么要把token放在请求头？
// 1. HTTP请求头是用来传递元数据的（比如认证信息、内容类型等）
// 2. Authorization是专门用来传递认证信息的请求头
// 3. 把token放在请求头，后端可以方便地获取token
// 4. 符合HTTP协议的标准

// 请求头示例
// GET /user/info HTTP/1.1
// Host: localhost:8080
// Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### ⑤ Claims 是什么（用"字典/Map"类比）

**Claims** = 存储用户信息的容器

**用"字典/Map"类比**：
- **Claims** = 字典（存储键值对）
- **Claims** = Map（存储键值对）

```java
// Claims示例
Map<String, Object> claims = new HashMap<>();
claims.put("userId", 1);        // 键是"userId"，值是1
claims.put("username", "admin"); // 键是"username"，值是"admin"
claims.put("role", "admin");     // 键是"role"，值是"admin"

// Claims和Map的区别
// Claims是JWT库提供的接口，专门用来存储JWT的负载
// Map是Java提供的接口，用来存储键值对
// Claims本质上就是一个Map，但提供了一些额外的方法

// Claims中的数据
// {
//   "userId": 1,       // 用户id
//   "username": "admin",  // 用户名
//   "role": "admin",       // 角色
//   "exp": 1704067200, // 过期时间（时间戳）
//   "iat": 1703980800  // 签发时间（时间戳）
// }
```

---

## 4️⃣ 文件整体结构说明

```
JwtUtil.java
├── package声明（第1行）
├── import导入（第2-10行）
├── 类定义和注解（第12行）
├── 常量定义（第13-15行）
├── 方法定义（第17-82行）
│   ├── generateToken方法（第17-22行）
│   ├── createToken方法（第24-33行）
│   ├── parseToken方法（第35-40行）
│   ├── getUserIdFromToken方法（第42-45行）
│   ├── getUsernameFromToken方法（第47-50行）
│   ├── getRoleFromToken方法（第52-55行）
│   ├── validateToken方法（第57-62行）
│   └── isTokenExpired方法（第64-70行）
```

---

## 5️⃣ 逐行代码解释

### 第1-10行：package和import

```java
package com.school.campusexpress.util;  // 第1行

// 第1行详细解释：
// package：Java关键字，表示"包"
// com.school.campusexpress.util：包名
// 作用：告诉Java"这个JwtUtil类在com.school.campusexpress.util包中"
// 如果删掉会怎样：编译错误，Java不知道这个类在哪个包中

import io.jsonwebtoken.Claims;  // 第2行

// 第2行详细解释：
// import：Java关键字，表示"导入"
// io.jsonwebtoken.Claims：要导入的接口
// 作用：导入Claims接口，用来存储JWT的负载
// 如果删掉会怎样：无法使用Claims接口

import io.jsonwebtoken.Jwts;  // 第3行

// 第3行详细解释：
// 导入Jwts类，用来生成和解析JWT token
// 如果删掉会怎样：无法使用Jwts类

import io.jsonwebtoken.security.Keys;  // 第4行

// 第4行详细解释：
// 导入Keys类，用来生成密钥
// 如果删掉会怎样：无法使用Keys类

import io.jsonwebtoken.SignatureAlgorithm;  // 第5行

// 第5行详细解释：
// 导入SignatureAlgorithm枚举，用来指定签名算法
// 如果删掉会怎样：无法使用SignatureAlgorithm枚举

import org.springframework.stereotype.Component;  // 第6行

// 第6行详细解释：
// 导入Component注解，用来标记组件
// 如果删掉会怎样：无法使用@Component注解

import javax.crypto.SecretKey;  // 第7行

// 第7行详细解释：
// 导入SecretKey接口，用来表示密钥
// 如果删掉会怎样：无法使用SecretKey接口

import java.util.Date;  // 第8行

// 第8行详细解释：
// 导入Date类，用来表示日期
// 如果删掉会怎样：无法使用Date类

import java.util.HashMap;  // 第9行

// 第9行详细解释：
// 导入HashMap类，用来存储键值对数据
// 如果删掉会怎样：无法使用HashMap类

import java.util.Map;  // 第10行

// 第10行详细解释：
// 导入Map接口，用来存储键值对数据
// 如果删掉会怎样：无法使用Map接口
```

### 第12行：类定义和注解

```java
@Component  // 第12行
public class JwtUtil {  // 第13行

// 第12行详细解释：
// @Component：Spring注解，表示"组件"
// 作用：告诉Spring"这个类是一个组件，需要被管理"
// 没有它会怎样：Spring不会扫描这个类，无法注入到Controller中
// 在本项目中的作用：JwtUtil是一个工具类，用来生成和解析JWT token，Spring会自动创建对象并管理

// @Component和@Service有什么区别？
// @Component：通用的组件注解
// @Service：专门用于业务逻辑层的组件注解
// 本质上是一样的，但@Service更语义化


// 第13行详细解释：
// public：访问修饰符，表示"公开的"
// class：Java关键字，表示"类"
// JwtUtil：类名
// 作用：定义一个名为JwtUtil的类
// 如果删掉public会怎样：其他类无法访问JwtUtil类
```

### 第13-15行：常量定义

```java
private static final long EXPIRATION_TIME = 30L * 24 * 60 * 60 * 1000;  // 第14行

// 第14行详细解释：
// private：访问修饰符，表示"私有的"，只有本类可以访问
// static：Java关键字，表示"静态的"，属于类，不属于对象
// final：Java关键字，表示"最终的"，值不能被修改
// long：数据类型，表示"长整型"
// EXPIRATION_TIME：常量名，表示"过期时间"
// =：赋值运算符
// 30L * 24 * 60 * 60 * 1000：常量的值
// 作用：定义一个私有的静态最终常量，名为EXPIRATION_TIME，值为30天的毫秒数

// 30L * 24 * 60 * 60 * 1000 是什么意思？
// 30L：30天（L表示long类型）
// 24：24小时
// 60：60分钟
// 60：60秒
// 1000：1000毫秒
// 整体意思：30天的毫秒数

// 为什么要用毫秒？
// Java中的时间通常用毫秒表示
// System.currentTimeMillis()返回的是当前时间的毫秒数


private static final String SECRET_KEY_STRING = "campus-express-secret-key-for-jwt-token-generation-2024-secure-enough-for-hs512-algorithm-requirement";  // 第15行

// 第15行详细解释：
// private：访问修饰符，表示"私有的"
// static：Java关键字，表示"静态的"
// final：Java关键字，表示"最终的"
// String：数据类型，表示"字符串"
// SECRET_KEY_STRING：常量名，表示"密钥字符串"
// =：赋值运算符
// "campus-express-secret-key-for-jwt-token-generation-2024-secure-enough-for-hs512-algorithm-requirement"：常量的值
// 作用：定义一个私有的静态最终常量，名为SECRET_KEY_STRING，值为密钥字符串

// 什么是密钥？
// 密钥是用来签名和验证JWT token的字符串
// 就像钥匙，用来锁和开锁
// 只有知道密钥的人才能生成和验证token

// 为什么要用这么长的密钥？
// HS512算法要求密钥长度至少512位（64字节）
// 这个字符串的长度是104个字符，满足要求


private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());  // 第16行

// 第16行详细解释：
// private：访问修饰符，表示"私有的"
// static：Java关键字，表示"静态的"
// final：Java关键字，表示"最终的"
// SecretKey：接口类型，表示"密钥"
// SECRET_KEY：常量名，表示"密钥"
// =：赋值运算符
// Keys.hmacShaKeyFor()：调用Keys的hmacShaKeyFor方法
// (SECRET_KEY_STRING.getBytes())：传入的参数，把密钥字符串转换成字节数组
// 作用：定义一个私有的静态最终常量，名为SECRET_KEY，值为密钥对象

// Keys.hmacShaKeyFor()是干什么的？
// Keys.hmacShaKeyFor()用来生成HMAC-SHA算法的密钥
// 传入一个字节数组，返回一个SecretKey对象

// SECRET_KEY_STRING.getBytes()是干什么的？
// SECRET_KEY_STRING.getBytes()把字符串转换成字节数组
// 比如"abc"转换成字节数组[97, 98, 99]

// 为什么要用SecretKey而不是String？
// SecretKey是Java加密库提供的接口，专门用来表示密钥
// String是普通的字符串，不能直接用于加密操作
```

### 第17-22行：generateToken方法

```java
public String generateToken(Long userId, String username, String role) {  // 第17行
    Map<String, Object> claims = new HashMap<>();  // 第18行
    claims.put("userId", userId);  // 第19行
    claims.put("username", username);  // 第20行
    claims.put("role", role);  // 第21行
    return createToken(claims, username);  // 第22行
}  // 第23行

// 第17行详细解释：
// public：访问修饰符，表示"公开的"
// String：返回类型，表示返回一个字符串（JWT token）
// generateToken：方法名，表示"生成token"
// (Long userId, String username, String role)：参数列表
// 作用：定义一个方法，生成JWT token

// 为什么要传入userId、username、role？
// JWT token中需要存储用户的信息
// userId：用户id
// username：用户名
// role：角色


// 第18行详细解释：
// Map<String, Object> claims = new HashMap<>();
// Map<String, Object> claims：声明一个Map<String, Object>类型的变量，名为claims
// =：赋值运算符
// new：创建新对象
// HashMap<>()：创建一个HashMap对象
// 作用：创建一个新的HashMap对象，用来存储用户信息

// 为什么要用Map？
// Claims本质上就是一个Map
// 用Map存储用户信息，方便后续操作


// 第19行详细解释：
// claims.put("userId", userId);
// claims.put()：调用HashMap的put方法，存储键值对
// ("userId", userId)：键是"userId"，值是userId变量
// 作用：在Map中存储一个键值对，键是"userId"，值是userId变量

// 为什么要存储userId？
// 后续解析token时，需要获取userId
// 比如查询用户信息、验证权限等


// 第20行详细解释：
// claims.put("username", username);
// claims.put()：调用HashMap的put方法，存储键值对
// ("username", username)：键是"username"，值是username变量
// 作用：在Map中存储一个键值对，键是"username"，值是username变量


// 第21行详细解释：
// claims.put("role", role);
// claims.put()：调用HashMap的put方法，存储键值对
// ("role", role)：键是"role"，值是role变量
// 作用：在Map中存储一个键值对，键是"role"，值是role变量


// 第22行详细解释：
// return createToken(claims, username);
// return：Java关键字，表示"返回"
// createToken(claims, username)：调用createToken方法，传入claims和username作为参数
// 作用：调用createToken方法，生成JWT token，返回token字符串

// 为什么要调用createToken方法？
// generateToken方法负责准备数据（claims）
// createToken方法负责生成token
// 分离关注点，代码更清晰


// 第23行详细解释：
// }：方法结束
```

### 第24-33行：createToken方法

```java
private String createToken(Map<String, Object> claims, String subject) {  // 第24行
    long nowMillis = System.currentTimeMillis();  // 第25行
    long expiryMillis = nowMillis + EXPIRATION_TIME;  // 第26行

    return Jwts.builder()  // 第28行
            .setClaims(claims)  // 第29行
            .setSubject(subject)  // 第30行
            .setIssuedAt(new Date(nowMillis))  // 第31行
            .setExpiration(new Date(expiryMillis))  // 第32行
            .signWith(SECRET_KEY, SignatureAlgorithm.HS512)  // 第33行
            .compact();  // 第34行
}  // 第35行

// 第24行详细解释：
// private：访问修饰符，表示"私有的"，只有本类可以访问
// String：返回类型，表示返回一个字符串（JWT token）
// createToken：方法名，表示"创建token"
// (Map<String, Object> claims, String subject)：参数列表
// 作用：定义一个私有的方法，创建JWT token

// 为什么要设为private？
// createToken是内部方法，只被generateToken调用
// 外部不需要直接调用createToken


// 第25行详细解释：
// long nowMillis = System.currentTimeMillis();
// long nowMillis：声明一个long类型的变量，名为nowMillis
// =：赋值运算符
// System.currentTimeMillis()：获取当前时间的毫秒数
// 作用：获取当前时间的毫秒数，赋给nowMillis变量

// System.currentTimeMillis()是干什么的？
// System.currentTimeMillis()返回当前时间的毫秒数
// 从1970年1月1日0点0分0秒（UTC）开始计算的毫秒数
// 比如当前时间是2024年1月1日0点0分0秒，返回的毫秒数是1704067200000


// 第26行详细解释：
// long expiryMillis = nowMillis + EXPIRATION_TIME;
// long expiryMillis：声明一个long类型的变量，名为expiryMillis
// =：赋值运算符
// nowMillis + EXPIRATION_TIME：当前时间的毫秒数 + 过期时间的毫秒数
// 作用：计算过期时间的毫秒数，赋给expiryMillis变量

// 为什么要计算过期时间？
// JWT token需要设置过期时间
// 过期时间 = 当前时间 + 有效期（30天）


// 第28行详细解释：
// return Jwts.builder()
// return：Java关键字，表示"返回"
// Jwts.builder()：调用Jwts的builder方法，创建一个JwtBuilder对象
// 作用：创建一个JwtBuilder对象，用来构建JWT token

// 什么是链式调用？
// 链式调用就是在一个对象上连续调用多个方法
// 比如 Jwts.builder().setClaims(claims).setSubject(subject).compact()
// 每个方法都返回对象本身，所以可以连续调用


// 第29行详细解释：
// .setClaims(claims)
// .setClaims()：调用JwtBuilder的setClaims方法
// (claims)：传入的参数，表示JWT的负载
// 作用：设置JWT的负载，存储用户信息

// 为什么要设置Claims？
// Claims中存储了用户的信息（userId、username、role）
// 后续解析token时，可以从Claims中获取用户信息


// 第30行详细解释：
// .setSubject(subject)
// .setSubject()：调用JwtBuilder的setSubject方法
// (subject)：传入的参数，表示JWT的主题
// 作用：设置JWT的主题，通常是用户名

// 什么是Subject？
// Subject是JWT的标准字段，表示JWT的主题
// 通常是用户名或用户id


// 第31行详细解释：
// .setIssuedAt(new Date(nowMillis))
// .setIssuedAt()：调用JwtBuilder的setIssuedAt方法
// (new Date(nowMillis))：传入的参数，表示JWT的签发时间
// 作用：设置JWT的签发时间

// new Date(nowMillis)是干什么的？
// new：创建新对象
// Date(nowMillis)：创建一个Date对象，传入毫秒数作为参数
// 作用：把毫秒数转换成Date对象

// 为什么要设置签发时间？
// 记录JWT是什么时候签发的
// 可以用来计算JWT的有效期


// 第32行详细解释：
// .setExpiration(new Date(expiryMillis))
// .setExpiration()：调用JwtBuilder的setExpiration方法
// (new Date(expiryMillis))：传入的参数，表示JWT的过期时间
// 作用：设置JWT的过期时间

// 为什么要设置过期时间？
// JWT需要有过期时间，防止token被永久使用
// 过期后，token失效，需要重新登录


// 第33行详细解释：
// .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
// .signWith()：调用JwtBuilder的signWith方法
// (SECRET_KEY, SignatureAlgorithm.HS512)：传入的参数
// SECRET_KEY：密钥
// SignatureAlgorithm.HS512：签名算法
// 作用：用密钥和签名算法对JWT进行签名

// 为什么要签名？
// 签名可以防止JWT被篡改
// 如果JWT被篡改，签名会失效，无法通过验证

// 什么是HS512？
// HS512是一种HMAC-SHA算法
// HMAC（Hash-based Message Authentication Code）：基于哈希的消息认证码
// SHA512：安全哈希算法，输出512位
// HS512：用SHA512算法生成HMAC


// 第34行详细解释：
// .compact()
// .compact()：调用JwtBuilder的compact方法
// 作用：构建JWT token，返回token字符串

// 为什么要调用compact？
// compact()会把Header、Payload、Signature三部分组合起来
// 用点号（.）分隔，生成最终的JWT token字符串


// 第35行详细解释：
// }：方法结束
```

### 第35-40行：parseToken方法

```java
public Claims parseToken(String token) {  // 第36行
    return Jwts.parserBuilder()  // 第37行
            .setSigningKey(SECRET_KEY)  // 第38行
            .build()  // 第39行
            .parseClaimsJws(token)  // 第40行
            .getBody();  // 第41行
}  // 第42行

// 第36行详细解释：
// public：访问修饰符，表示"公开的"
// Claims：返回类型，表示返回一个Claims对象
// parseToken：方法名，表示"解析token"
// (String token)：参数列表，接收一个String类型的参数，名为token
// 作用：定义一个方法，解析JWT token，返回Claims对象


// 第37行详细解释：
// return Jwts.parserBuilder()
// return：Java关键字，表示"返回"
// Jwts.parserBuilder()：调用Jwts的parserBuilder方法，创建一个JwtParserBuilder对象
// 作用：创建一个JwtParserBuilder对象，用来解析JWT token


// 第38行详细解释：
// .setSigningKey(SECRET_KEY)
// .setSigningKey()：调用JwtParserBuilder的setSigningKey方法
// (SECRET_KEY)：传入的参数，表示密钥
// 作用：设置密钥，用来验证JWT的签名

// 为什么要设置密钥？
// 解析JWT时，需要用密钥验证签名
// 如果签名验证失败，说明JWT被篡改


// 第39行详细解释：
// .build()
// .build()：调用JwtParserBuilder的build方法
// 作用：构建JwtParser对象，用来解析JWT token


// 第40行详细解释：
// .parseClaimsJws(token)
// .parseClaimsJws()：调用JwtParser的parseClaimsJws方法
// (token)：传入的参数，表示JWT token字符串
// 作用：解析JWT token，返回Jws对象

// 什么是Jws？
// Jws（JSON Web Signature）是JWT的一种
// 包含Header、Payload、Signature三部分


// 第41行详细解释：
// .getBody()
// .getBody()：调用Jws的getBody方法
// 作用：获取JWT的负载（Claims对象）

// 为什么要获取Body？
// Body中存储了用户的信息（userId、username、role）
// 后续可以从Claims中获取用户信息


// 第42行详细解释：
// }：方法结束
```

### 第42-45行：getUserIdFromToken方法

```java
public Long getUserIdFromToken(String token) {  // 第43行
    Claims claims = parseToken(token);  // 第44行
    return Long.valueOf(claims.get("userId").toString());  // 第45行
}  // 第46行

// 第43行详细解释：
// public：访问修饰符，表示"公开的"
// Long：返回类型，表示返回一个Long对象
// getUserIdFromToken：方法名，表示"从token中获取用户id"
// (String token)：参数列表
// 作用：定义一个方法，从token中获取用户id


// 第44行详细解释：
// Claims claims = parseToken(token);
// Claims claims：声明一个Claims类型的变量，名为claims
// =：赋值运算符
// parseToken(token)：调用parseToken方法，传入token作为参数
// 作用：解析token，获取Claims对象


// 第45行详细解释：
// return Long.valueOf(claims.get("userId").toString());
// return：Java关键字，表示"返回"
// Long.valueOf()：调用Long的valueOf方法，把字符串转换成Long对象
// claims.get("userId")：从Claims中获取userId
// .toString()：把userId转换成字符串
// 作用：从Claims中获取userId，转换成Long对象，返回

// claims.get("userId")是干什么的？
// claims.get()：调用Claims的get方法
// ("userId")：键是"userId"
// 作用：从Claims中获取userId的值

// 为什么要用Long.valueOf()？
// claims.get("userId")返回的是Object类型
// 需要转换成Long类型
// Long.valueOf()可以把字符串转换成Long对象


// 第46行详细解释：
// }：方法结束
```

---

## 6️⃣ 初学者常见误解

**误解1**：Token和Cookie是一样的
- ❌ 错误：Token是字符串，Cookie是存储机制
- ✅ 正确：Token可以存储在Cookie中，也可以存储在localStorage中

**误解2**：Token是绝对安全的
- ❌ 错误：Token可以被窃取，需要配合HTTPS使用
- ✅ 正确：Token比Session更安全，但不是绝对安全

**误解3**：Token可以存储敏感信息
- ❌ 错误：Token只是Base64编码，可以被解码，不应该存储敏感信息
- ✅ 正确：Token中只存储非敏感信息（如userId、username、role）

**误解4**：Token过期后无法使用
- ❌ 错误：Token过期后无法使用，但可以刷新
- ✅ 正确：Token过期后无法使用，需要重新登录或刷新token

**误解5**：Claims和Map是一样的
- ❌ 错误：Claims是JWT库提供的接口，Map是Java提供的接口
- ✅ 正确：Claims本质上就是一个Map，但提供了一些额外的方法

---

## 7️⃣ 答辩时一句话怎么说

"JWT是一种JSON格式的token，用来在前后端之间传递用户信息。JwtUtil是JWT工具类，用来生成和解析JWT token。用户登录成功后，后端生成token返回给前端，前端保存token，每次请求都带上token，后端解析token获取用户信息，判断用户是否有权限。"

---

## 8️⃣ 毕设答辩准备

**问题1**：请解释一下什么是Token
**回答**：Token是一个字符串，用来在前后端之间传递用户信息，就像通行证一样。用户登录成功后，后端返回token给前端，前端保存token，每次请求都带上token，后端解析token获取用户信息，判断用户是否有权限。

**问题2**：Token和Session有什么区别？
**回答**：Token存储在客户端（浏览器），Session存储在服务端（服务器）。Token支持跨域，Session不支持跨域。Token更容易扩展（微服务），Session不容易扩展（需要共享Session）。

**问题3**：为什么要把token放在请求头？
**回答**：HTTP请求头是用来传递元数据的（比如认证信息、内容类型等），Authorization是专门用来传递认证信息的请求头，把token放在请求头，后端可以方便地获取token，符合HTTP协议的标准。

**问题4**：Claims是什么？
**回答**：Claims是存储用户信息的容器，就像字典或Map一样，存储键值对数据。Claims中存储了用户的信息（userId、username、role等），后续可以从Claims中获取用户信息。

**问题5**：JWT由哪几部分组成？
**回答**：JWT由三部分组成，用点号（.）分隔：Header（头部）、Payload（负载）、Signature（签名）。Header描述token的元数据，Payload存储用户信息，Signature防止token被篡改。

**问题6**：为什么要签名？
**回答**：签名可以防止JWT被篡改，如果JWT被篡改，签名会失效，无法通过验证。签名用密钥对Header和Payload进行签名，只有知道密钥的人才能生成和验证token。

**问题7**：为什么要设置过期时间？
**回答**：JWT需要有过期时间，防止token被永久使用。过期后，token失效，需要重新登录。这样可以提高安全性，即使token被窃取，也只能在有效期内使用。

**问题8**：EXPIRATION_TIME是干什么的？
**回答**：EXPIRATION_TIME是过期时间，表示token的有效期，本项目设置为30天。过期时间 = 当前时间 + 有效期（30天）。

**问题9**：SECRET_KEY是干什么的？
**回答**：SECRET_KEY是密钥，用来签名和验证JWT token。只有知道密钥的人才能生成和验证token，密钥越长越安全。

**问题10**：generateToken方法是干什么的？
**回答**：generateToken方法是生成JWT token的方法，传入userId、username、role，返回JWT token字符串。它会创建Claims对象，存储用户信息，然后调用createToken方法生成token。
