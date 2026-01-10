# 校园快递管理系统 - 前端项目

## 项目信息

- **项目名称**: 校园快递管理系统
- **项目类型**: 本科毕业设计
- **技术栈**: Vue3 + Vite + TypeScript + Element Plus + Pinia + Vue Router + ECharts
- **后端接口**: Spring Boot RESTful API

## 技术栈说明

- **Vue3**: 渐进式 JavaScript 框架
- **Vite**: 新一代前端构建工具
- **TypeScript**: JavaScript 的超集，提供类型安全
- **Element Plus**: Vue 3 组件库
- **Pinia**: Vue 状态管理库
- **Vue Router**: Vue 官方路由管理器
- **ECharts**: 图表库（用于数据统计展示）
- **Axios**: HTTP 客户端（用于接口调用）

## 项目结构

```
campus-express-frontend/
├── public/                    # 静态资源
├── src/
│   ├── api/                   # API 接口（按业务模块划分）
│   │   ├── auth.ts           # 认证接口
│   │   ├── express.ts        # 快递管理接口
│   │   ├── pickup.ts         # 取件记录接口
│   │   ├── station.ts        # 快递站接口
│   │   └── user.ts           # 用户管理接口
│   ├── assets/               # 资源文件
│   │   └── styles/           # 全局样式
│   │       └── index.css
│   ├── components/           # 业务组件（按业务模块划分）
│   │   ├── express/          # 快递相关组件
│   │   ├── pickup/           # 取件记录相关组件
│   │   ├── station/          # 快递站相关组件
│   │   └── user/             # 用户相关组件
│   ├── layouts/              # 布局组件
│   │   └── MainLayout.vue    # 主布局
│   ├── router/               # 路由配置
│   │   └── index.ts          # 路由定义
│   ├── store/                # 状态管理
│   │   ├── modules/
│   │   │   └── user.ts       # 用户状态
│   │   └── index.ts          # Store 入口
│   ├── types/                # 类型定义
│   │   ├── api.ts            # API 类型
│   │   └── index.ts          # 通用类型
│   ├── utils/                # 工具函数
│   │   ├── request.ts        # Axios 请求封装
│   │   └── validate.ts       # 表单验证
│   ├── views/                # 页面（按业务模块划分）
│   │   ├── login/            # 登录页
│   │   ├── dashboard/        # 首页
│   │   ├── express/          # 快递管理
│   │   ├── pickup/           # 取件记录
│   │   ├── station/          # 快递站管理
│   │   └── user/             # 用户管理
│   ├── App.vue
│   └── main.ts
├── .env.development          # 开发环境配置
├── .env.production           # 生产环境配置
├── index.html
├── package.json
├── tsconfig.json
└── vite.config.ts
```

## 开发指南

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

开发服务器将在 http://localhost:3000 启动

### 构建生产版本

```bash
npm run build
```

### 预览生产版本

```bash
npm run preview
```

## 功能模块

### 1. 登录页面
- 用户名/密码登录
- 登录校验
- 管理员/普通用户角色区分

### 2. Dashboard 首页
- 统计数据卡片
- 快递状态分布图表（饼图）
- 每日取件数量变化图表（柱状图）
- 最近取件记录列表

### 3. 快递管理页面
- 快递列表展示
- 添加快递
- 编辑快递
- 删除快递
- 查询快递

### 4. 取件记录页面
- 取件记录列表展示
- 添加取件记录
- 编辑取件记录
- 删除取件记录
- 查询取件记录

### 5. 快递站管理页面
- 快递站列表展示
- 添加快递站
- 编辑快递站
- 删除快递站
- 查询快递站

### 6. 用户管理页面
- 用户列表展示
- 添加用户
- 编辑用户
- 删除用户
- 查询用户

## 接口对接

使用 Axios 进行 HTTP 请求，通过统一的 request 工具类处理 token 添加和错误处理。

### 认证接口
- POST /user/login - 用户登录
- GET /user/info - 获取用户信息
- POST /user/logout - 用户登出

### 快递管理接口
- GET /admin/express/list - 获取快递列表
- GET /admin/express/{id} - 获取快递详情
- POST /admin/express/add - 添加快递
- PUT /admin/express/update - 更新快递
- DELETE /admin/express/delete/{id} - 删除快递
- GET /admin/express/statistics - 获取快递统计

### 取件记录接口
- GET /admin/pickup-record/list - 获取取件记录列表
- GET /admin/pickup-record/{id} - 获取取件记录详情
- POST /admin/pickup-record/add - 添加取件记录
- PUT /admin/pickup-record/update - 更新取件记录
- DELETE /admin/pickup-record/delete/{id} - 删除取件记录

### 快递站管理接口
- GET /admin/station/list - 获取快递站列表
- GET /admin/station/{id} - 获取快递站详情
- POST /admin/station/add - 添加快递站
- PUT /admin/station/update - 更新快递站
- DELETE /admin/station/delete/{id} - 删除快递站

### 用户管理接口
- GET /admin/user/list - 获取用户列表
- GET /admin/user/{id} - 获取用户详情
- POST /admin/user/add - 添加用户
- PUT /admin/user/update - 更新用户
- DELETE /admin/user/delete/{id} - 删除用户
- PUT /admin/user/status/{id} - 更新用户状态
- PUT /admin/user/reset-password/{id} - 重置用户密码
- GET /admin/user/search - 搜索用户

## 环境变量

### 开发环境（.env.development）
```
VITE_APP_TITLE=校园快递管理系统
VITE_API_BASE_URL=http://localhost:8080
```

### 生产环境（.env.production）
```
VITE_APP_TITLE=校园快递管理系统
VITE_API_BASE_URL=http://localhost:8080
```

## 注意事项

1. 所有组件仅服务对应业务模块，功能简单直观
2. 每个页面顶部添加功能说明和对应毕业设计模块
3. 每个函数添加必要注释，复杂逻辑说明用途
4. 错误提示保持简单（如 401 / 404 / 500），直接在组件中处理
5. 目录以业务模块为中心，不做通用组件库
6. 界面清晰简单，使用 Element Plus 默认样式
