# DTO 和 Entity 的区别讲解（零基础版）

## 📚 学习目标
- 理解什么是DTO（Data Transfer Object）
- 理解什么是Entity（实体类）
- 理解DTO和Entity的区别
- 理解为什么登录应该用LoginDTO而不是User
- 能在答辩时用自己的话说出区别

---

## 1️⃣ 这个文件是干什么的（白话）

**DTO（Data Transfer Object）**：数据传输对象，用来在不同层之间传输数据。

**Entity（实体类）**：实体类，用来表示数据库中的表。

就像生活中：
- **DTO** = 申请表（用来填写信息，提交给某个部门）
- **Entity** = 档案（存储在数据库中，包含所有信息）

在程序中：
- **LoginDTO** = 登录申请表（只包含用户名和密码）
- **User** = 用户档案（包含用户名、密码、真实姓名、手机号、角色、状态等所有信息）

---

## 2️⃣ 不用 DTO 会怎样

1. **安全问题**：前端可以传入不应该修改的字段（比如修改别人的密码）
2. **验证困难**：无法针对不同场景设置不同的验证规则
3. **代码混乱**：同一个Entity在不同场景下使用，代码难以维护
4. **性能问题**：传输不必要的数据，浪费带宽

---

## 3️⃣ DTO 和 Entity 的区别

### ① 用途不同

| | DTO | Entity |
|---|---|---|
| 用途 | 数据传输 | 数据库映射 |
| 场景 | Controller接收前端数据、返回数据给前端 | Service处理业务逻辑、Mapper操作数据库 |
| 字段 | 只包含需要的字段 | 包含数据库表的所有字段 |
| 验证 | 针对场景设置验证规则 | 通用的验证规则 |

### ② 举例说明

**场景1：用户登录**

```java
// DTO：LoginDTO（登录申请表）
@Data
public class LoginDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;  // 只需要用户名

    @NotBlank(message = "密码不能为空")
    private String password;  // 只需要密码
}

// Entity：User（用户档案）
@Data
@TableName("user")
public class User {
    @TableId
    private Long id;  // 登录时不需要

    @NotBlank(message = "用户名不能为空")
    private String username;  // 需要

    private String password;  // 需要

    @TableField("real_name")
    private String realName;  // 登录时不需要

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;  // 登录时不需要

    private String role;  // 登录时不需要

    private Integer status;  // 登录时不需要

    @TableField("create_time")
    private LocalDateTime createTime;  // 登录时不需要

    @TableField("update_time")
    private LocalDateTime updateTime;  // 登录时不需要
}

// 为什么登录用LoginDTO而不是User？
// 1. 登录只需要用户名和密码，不需要其他字段
// 2. 用User的话，前端可以传入不应该修改的字段（比如修改别人的id）
// 3. 用LoginDTO的话，前端只能传入用户名和密码，更安全
```

**场景2：快递入库**

```java
// DTO：ExpressInboundDTO（快递入库申请表）
@Data
@Schema(description = "快递入库DTO")
public class ExpressInboundDTO {
    @NotBlank(message = "快递单号不能为空")
    @Schema(description = "快递单号（扫码/输入）")
    private String trackingNumber;  // 只需要快递单号

    @NotBlank(message = "收件人手机号不能为空")
    @Schema(description = "收件人手机号（用于自动匹配用户）")
    private String receiverPhone;  // 只需要收件人手机号

    @Schema(description = "快递公司（可选）")
    private String company;  // 可选的快递公司
}

// Entity：Express（快递档案）
@Data
@TableName("express")
public class Express {
    @TableId
    private Long id;  // 入库时不需要

    private String trackingNumber;  // 需要

    private String receiverName;  // 入库时不需要（通过手机号自动匹配）

    private String receiverPhone;  // 需要

    private String company;  // 需要

    private String status;  // 入库时不需要（自动设置为"待取件"）

    private String pickupCode;  // 入库时不需要（自动生成）

    private Long stationId;  // 入库时不需要（从当前登录用户获取）

    private LocalDateTime inboundTime;  // 入库时不需要（自动设置为当前时间）

    private LocalDateTime pickupTime;  // 入库时不需要

    private LocalDateTime createTime;  // 入库时不需要

    private LocalDateTime updateTime;  // 入库时不需要
}

// 为什么入库用ExpressInboundDTO而不是Express？
// 1. 入库只需要快递单号、收件人手机号、快递公司
// 2. 其他字段（如status、pickupCode、inboundTime等）由后端自动设置
// 3. 用Express的话，前端可以传入不应该设置的字段（比如修改status为"已取件"）
```

---

## 4️⃣ 为什么登录用 LoginDTO 而不是 User

### ① 安全问题

```java
// 错误示例：直接用User接收登录参数
@PostMapping("/login")
public R<Map<String, Object>> login(@Valid @RequestBody User user) {
    // 问题1：前端可以传入id字段
    // 比如前端传入 {"id": 999, "username": "admin", "password": "123456"}
    // 这样可能导致安全问题（比如修改别人的信息）

    // 问题2：前端可以传入role字段
    // 比如前端传入 {"username": "admin", "password": "123456", "role": "admin"}
    // 这样可能导致权限提升

    // 问题3：前端可以传入status字段
    // 比如前端传入 {"username": "admin", "password": "123456", "status": 1}
    // 这样可能导致绕过禁用检查

    String result = userService.login(user.getUsername(), user.getPassword());
    // ...
}

// 正确示例：用LoginDTO接收登录参数
@PostMapping("/login")
public R<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
    // 优点1：前端只能传入username和password
    // 无法传入id、role、status等字段

    // 优点2：更安全，防止前端传入不应该修改的字段

    String result = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
    // ...
}
```

### ② 验证规则不同

```java
// LoginDTO：登录场景的验证规则
@Data
public class LoginDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;  // 登录时用户名不能为空

    @NotBlank(message = "密码不能为空")
    private String password;  // 登录时密码不能为空
}

// User：通用的验证规则
@Data
@TableName("user")
public class User {
    @NotBlank(message = "用户名不能为空")
    private String username;  // 用户名不能为空

    private String password;  // 密码可以为空（比如更新用户信息时，如果不修改密码，就不传密码）

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;  // 手机号格式必须正确
}

// 为什么验证规则不同？
// 1. 登录时，用户名和密码都不能为空
// 2. 更新用户信息时，密码可以为空（如果不修改密码，就不传密码）
// 3. 用LoginDTO的话，可以针对登录场景设置特定的验证规则
```

### ③ 代码清晰

```java
// 用LoginDTO：代码更清晰
@PostMapping("/login")
public R<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
    // 一眼就能看出这是登录接口，只需要用户名和密码
    String result = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
    // ...
}

// 用User：代码不够清晰
@PostMapping("/login")
public R<Map<String, Object>> login(@Valid @RequestBody User user) {
    // 需要看代码才知道这是登录接口，只用了username和password
    // 其他字段（realName、phone、role等）都没有用到
    String result = userService.login(user.getUsername(), user.getPassword());
    // ...
}
```

---

## 5️⃣ 本项目中的 DTO 示例

### ① ExpressInboundDTO（快递入库DTO）

```java
package com.school.campusexpress.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data  // Lombok注解，自动生成getter和setter
@Schema(description = "快递入库DTO")  // Swagger注解，用于生成API文档
public class ExpressInboundDTO {

    @NotBlank(message = "快递单号不能为空")  // 验证注解，快递单号不能为空
    @Schema(description = "快递单号（扫码/输入）")  // Swagger注解，用于生成API文档
    private String trackingNumber;  // 快递单号

    @NotBlank(message = "收件人手机号不能为空")  // 验证注解，收件人手机号不能为空
    @Schema(description = "收件人手机号（用于自动匹配用户）")  // Swagger注解，用于生成API文档
    private String receiverPhone;  // 收件人手机号

    @Schema(description = "快递公司（可选）")  // Swagger注解，用于生成API文档
    private String company;  // 快递公司（可选）
}

// 逐行解释：
// 第1行：package声明，表示这个类在com.school.campusexpress.dto包中
// 第3-5行：import导入，导入需要的注解和类
// 第7行：@Data注解，自动生成getter和setter方法
// 第8行：@Schema注解，用于生成API文档，描述这个DTO是"快递入库DTO"
// 第9行：定义ExpressInboundDTO类
// 第11行：@NotBlank注解，验证快递单号不能为空
// 第12行：@Schema注解，用于生成API文档，描述这个字段是"快递单号（扫码/输入）"
// 第13行：定义trackingNumber字段，类型是String
// 第15行：@NotBlank注解，验证收件人手机号不能为空
// 第16行：@Schema注解，用于生成API文档，描述这个字段是"收件人手机号（用于自动匹配用户）"
// 第17行：定义receiverPhone字段，类型是String
// 第19行：@Schema注解，用于生成API文档，描述这个字段是"快递公司（可选）"
// 第20行：定义company字段，类型是String

// 为什么需要这个DTO？
// 1. 快递入库只需要快递单号、收件人手机号、快递公司
// 2. 其他字段（如status、pickupCode、inboundTime等）由后端自动设置
// 3. 用Express实体类的话，前端可以传入不应该设置的字段
```

---

## 6️⃣ 初学者常见误解

**误解1**：DTO和Entity是一样的
- ❌ 错误：DTO用于数据传输，Entity用于数据库映射
- ✅ 正确：DTO和Entity有不同的用途，不能混用

**误解2**：可以省略DTO，直接用Entity
- ❌ 错误：省略DTO会导致安全问题、验证困难、代码混乱
- ✅ 建议：每个接口都应该使用对应的DTO

**误解3**：DTO的字段和Entity的字段必须一样
- ❌ 错误：DTO只包含需要的字段，Entity包含所有字段
- ✅ 正确：DTO的字段应该根据场景来定，只包含需要的字段

**误解4**：DTO可以包含所有字段
- ❌ 错误：DTO应该只包含需要的字段，包含不必要字段会导致安全问题
- ✅ 建议：DTO应该尽量简洁，只包含需要的字段

---

## 7️⃣ 答辩时一句话怎么说

"DTO是数据传输对象，用来在不同层之间传输数据，只包含需要的字段；Entity是实体类，用来表示数据库中的表，包含所有字段。登录应该用LoginDTO而不是User，因为登录只需要用户名和密码，用LoginDTO更安全。"

---

## 8️⃣ 毕设答辩准备

**问题1**：请解释一下DTO和Entity的区别
**回答**：DTO是数据传输对象，用来在不同层之间传输数据，只包含需要的字段；Entity是实体类，用来表示数据库中的表，包含所有字段。

**问题2**：为什么登录用LoginDTO而不是User？
**回答**：登录只需要用户名和密码，用LoginDTO的话，前端只能传入用户名和密码，无法传入其他字段（如id、role、status等），更安全。

**问题3**：如果直接用User会有什么问题？
**回答**：直接用User的话，前端可以传入不应该修改的字段（比如修改别人的id、提升自己的role、绕过禁用检查等），导致安全问题。

**问题4**：DTO和Entity的验证规则有什么区别？
**回答**：DTO针对特定场景设置验证规则，比如LoginDTO只验证用户名和密码不能为空；Entity设置通用的验证规则，比如User验证手机号格式必须正确。

**问题5**：为什么需要ExpressInboundDTO？
**回答**：快递入库只需要快递单号、收件人手机号、快递公司，其他字段（如status、pickupCode、inboundTime等）由后端自动设置，用ExpressInboundDTO更安全、更清晰。

**问题6**：DTO的字段可以包含所有字段吗？
**回答**：不可以，DTO应该只包含需要的字段，包含不必要字段会导致安全问题，比如前端可以传入不应该设置的字段。

**问题7**：DTO和Entity的转换怎么做？
**回答**：DTO和Entity的转换通常在Service层做，比如登录时，用LoginDTO接收前端数据，然后在Service中查询数据库得到User实体类，最后返回数据时再把User转换成UserDTO返回给前端。
