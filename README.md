# AdGrowth Platform

一个面向广告获客、用户增长和客服运营的开源项目骨架。前端提供可交互的管理端与用户端页面，后端提供 Spring Boot + MySQL 的基础接口与用户模型。

## 功能范围

- 广告投放、注册转化与增长漏斗
- 新人优惠券、签到积分、邀请裂变、拼团秒杀、限时券与会员召回
- 用户分群、自动化运营、营销规则、奖励流水和反作弊审核
- RAG 客服运营、知识库、服务评价闭环与人工工单

> 页面中的活动数据为本地演示数据。RAG 检索、营销自动化和风控策略目前展示交互流程，后端业务实现可按模块逐步接入。

## 技术栈

- Java 17、Spring Boot、Spring Web、Spring Data JPA、MySQL
- Vue 3、Vite

## 本地启动

### 1. 启动 MySQL

创建数据库：

```sql
CREATE DATABASE ad_growth DEFAULT CHARACTER SET utf8mb4;
```

设置数据库连接信息。未设置时默认连接 `127.0.0.1:3306/ad_growth`：

```powershell
$env:DB_URL = 'jdbc:mysql://127.0.0.1:3306/ad_growth?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true'
$env:DB_USERNAME = 'root'
$env:DB_PASSWORD = 'your-password'
```

### 2. 启动后端

```bash
mvn spring-boot:run
```

服务默认运行在 `http://127.0.0.1:8080`。

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

Vite 会在终端输出实际访问地址；默认端口为 `5173`，若已被占用会自动选择下一个可用端口。`/api` 请求会代理到后端 `8080` 端口。

## 当前接口

| 方法 | 路径 | 用途 |
| --- | --- | --- |
| GET | `/api/health` | 服务健康检查 |
| POST | `/api/users` | 创建用户 |
| GET | `/api/users` | 查询用户列表 |
| GET | `/api/users/{id}` | 查询用户详情 |

## 目录结构

```text
├── frontend/                 # Vue 3 运营台
├── src/main/java/            # Spring Boot 后端
├── src/main/resources/       # 应用配置
└── pom.xml
```

## 计划

- 接入认证、权限与前后端真实数据
- 实现活动、积分、优惠券和奖励审核领域模型
- 接入文档上传、向量检索和人工工单流转
- 补充接口测试与容器化部署

## 贡献

欢迎提交 Issue 和 Pull Request。提交前请执行前端构建与后端测试。
