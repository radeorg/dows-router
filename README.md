# Dows Router - 路由器系统

## 项目简介

Dows Router是一个基于Spring Boot和MyBatis-Flex的多模块路由器系统，提供完整的路由数据管理、会话管理、通道配置等功能。

## 技术栈

- **Java**: JDK 21
- **框架**: Spring Boot 3.2.0
- **数据库**: MySQL 8.0+ / PostgreSQL 12+
- **ORM**: MyBatis-Flex 1.7.8
- **API文档**: SpringDoc OpenAPI 3 (Swagger)
- **构建工具**: Maven 3.8+

## 模块结构

```
dows-router/
├── router-api/          # 接口层 - API接口定义
├── router-rpc/          # 暴露层 - Feign客户端
├── router-app/          # 启动模块 - Spring Boot应用入口
├── router-boot/         # 模块聚合 - 对外提供jar包
├── router-core/         # 核心模块 - 核心配置和工具类
├── router-rest/         # 控制层 - REST控制器实现
├── router-biz/          # 业务层 - 业务逻辑处理
├── router-handler/      # 处理层 - 数据处理和缓存
└── router-dao/          # 数据交互层 - 数据库操作
```

## 数据库表结构

### 核心表
- `router_data` - 路由数据表
- `router_session` - 路由会话表
- `router_channel` - 路由通道表
- `router_config` - 路由配置表
- `router_options` - 路由选项表
- `router_setting` - 路由设置表
- `router_account` - 路由账户表
- `router_metering` - 路由统计表

## 快速开始

### 1. 环境要求

- JDK 21+
- Maven 3.8+
- MySQL 8.0+ 或 PostgreSQL 12+

### 2. 数据库初始化

#### MySQL
```bash
mysql -u root -p < router-dao/src/main/resources/sql/mysql/schema.sql
```

#### PostgreSQL
```bash
psql -U postgres -f router-dao/src/main/resources/sql/postgresql/schema.sql
```

### 3. 配置数据库连接

修改 `router-app/src/main/resources/application.yml` 中的数据库连接配置：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/dows_router?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 4. 编译和运行

```bash
# 编译项目
mvn clean compile

# 运行应用
cd router-app
mvn spring-boot:run
```

### 5. 访问应用

- 应用地址: http://localhost:8080
- API文档: http://localhost:8080/swagger-ui.html
- API JSON: http://localhost:8080/v3/api-docs

## API接口

### 字段组管理 (/v1/admin/field/group)

- `POST /add` - 新增字段组
- `DELETE /remove/{id}` - 逻辑删除字段组
- `DELETE /delete/{id}` - 物理删除字段组
- `PUT /update` - 更新字段组
- `GET /page` - 分页查询字段组
- `GET /get/{id}` - 根据ID查询字段组
- `GET /list` - 列表查询字段组

## 开发指南

### 代码规范

- 使用Lombok简化代码
- 所有实体类使用Swagger注解
- 遵循RESTful API设计规范
- 使用MyBatis-Flex进行数据库操作

### 项目结构说明

1. **router-api**: 定义所有API接口和请求响应对象
2. **router-dao**: 数据访问层，包含实体类、Mapper和Service
3. **router-biz**: 业务逻辑层，处理具体业务逻辑
4. **router-rest**: 控制器层，实现API接口
5. **router-handler**: 处理层，聚合dao操作，提供缓存等功能
6. **router-core**: 核心配置和工具类
7. **router-app**: 应用启动入口

## 许可证

本项目采用 MIT 许可证。详情请参阅 [LICENSE](LICENSE) 文件。

## 联系方式

- 作者: lait.zhang
- 邮箱: lait.zhang@gmail.com