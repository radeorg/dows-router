# Dows Router 项目结构说明

## 项目概述

基于您提供的元数据，我已成功创建了一个完整的Maven多模块路由器系统项目。项目采用分层架构设计，包含9个子模块，完全符合您的需求规范。

## 项目结构

```
dows-router/                           # 根项目
├── pom.xml                           # 父POM文件
├── README.md                         # 项目说明文档
├── .gitignore                        # Git忽略文件
├── PROJECT_STRUCTURE.md              # 项目结构说明
│
├── router-api/                       # 接口层模块
│   ├── pom.xml
│   └── src/main/java/org/dows/router/
│       ├── common/
│       │   └── Page.java            # 通用分页对象
│       └── config/admin/
│           ├── FieldGroupApi.java   # 字段组API接口
│           ├── FieldGroup*Request.java   # 请求对象
│           └── FieldGroup*Response.java  # 响应对象
│
├── router-rpc/                       # 暴露层模块
│   ├── pom.xml
│   └── src/main/java/org/dows/router/config/admin/
│       └── AdminFieldGroupFeign.java # Feign客户端
│
├── router-app/                       # 启动模块
│   ├── pom.xml
│   ├── src/main/java/org/dows/router/
│   │   └── RouterApplication.java   # Spring Boot启动类
│   └── src/main/resources/
│       ├── application.yml          # 应用配置
│       └── application-example.yml  # 配置示例
│
├── router-boot/                      # 模块聚合
│   └── pom.xml                      # 聚合core和rest模块
│
├── router-core/                      # 核心模块
│   ├── pom.xml
│   └── src/main/java/org/dows/router/core/config/
│       └── RouterConfig.java        # 核心配置类
│
├── router-rest/                      # 控制层模块
│   ├── pom.xml
│   └── src/main/java/org/dows/router/config/admin/
│       └── AdminFieldGroupRest.java # REST控制器
│
├── router-biz/                       # 业务层模块
│   ├── pom.xml
│   └── src/main/java/org/dows/router/config/admin/
│       └── FieldGroupBiz.java       # 业务逻辑类
│
├── router-handler/                   # 处理层模块
│   ├── pom.xml
│   └── src/main/java/org/dows/router/handler/
│       └── RouterDataHandler.java   # 数据处理器
│
└── router-dao/                       # 数据交互层模块
    ├── pom.xml
    ├── src/main/java/org/dows/router/dao/
    │   ├── entity/                  # 实体类
    │   │   ├── RouterDataEntity.java
    │   │   ├── RouterSessionEntity.java
    │   │   ├── RouterChannelEntity.java
    │   │   ├── RouterConfigEntity.java
    │   │   ├── RouterOptionsEntity.java
    │   │   ├── RouterSettingEntity.java
    │   │   ├── RouterAccountEntity.java
    │   │   └── RouterMeteringEntity.java
    │   ├── mapper/                  # Mapper接口
    │   │   ├── RouterDataMapper.java
    │   │   ├── RouterSessionMapper.java
    │   │   ├── RouterChannelMapper.java
    │   │   ├── RouterConfigMapper.java
    │   │   ├── RouterOptionsMapper.java
    │   │   ├── RouterSettingMapper.java
    │   │   ├── RouterAccountMapper.java
    │   │   └── RouterMeteringMapper.java
    │   └── service/                 # Service接口和实现
    │       ├── RouterDataService.java
    │       ├── RouterSessionService.java
    │       ├── RouterChannelService.java
    │       ├── RouterConfigService.java
    │       ├── RouterOptionsService.java
    │       ├── RouterSettingService.java
    │       ├── RouterAccountService.java
    │       ├── RouterMeteringService.java
    │       └── impl/                # Service实现类
    │           ├── RouterDataServiceImpl.java
    │           ├── RouterSessionServiceImpl.java
    │           ├── RouterChannelServiceImpl.java
    │           ├── RouterConfigServiceImpl.java
    │           ├── RouterOptionsServiceImpl.java
    │           ├── RouterSettingServiceImpl.java
    │           ├── RouterAccountServiceImpl.java
    │           └── RouterMeteringServiceImpl.java
    └── src/main/resources/sql/
        ├── mysql/
        │   └── schema.sql           # MySQL DDL脚本
        └── postgresql/
            └── schema.sql           # PostgreSQL DDL脚本
```

## 技术栈

- **Java**: JDK 21
- **框架**: Spring Boot 3.2.0
- **ORM**: MyBatis-Flex 1.7.8
- **数据库**: MySQL 8.0+ / PostgreSQL 12+
- **API文档**: SpringDoc OpenAPI 3 (Swagger)
- **构建工具**: Maven 3.8+
- **工具库**: Lombok, Jakarta Validation

## 数据库表结构

已根据您的元数据创建了8张核心表：

1. **router_data** - 路由数据表（主表）
2. **router_session** - 路由会话表
3. **router_channel** - 路由通道表
4. **router_config** - 路由配置表
5. **router_options** - 路由选项表
6. **router_setting** - 路由设置表
7. **router_account** - 路由账户表
8. **router_metering** - 路由统计表

所有表都包含完整的字段定义、索引和外键约束，支持MySQL和PostgreSQL两种数据库。

## API接口

已实现字段组管理的完整CRUD接口：

- `POST /v1/admin/field/group/add` - 新增
- `DELETE /v1/admin/field/group/remove/{id}` - 逻辑删除
- `DELETE /v1/admin/field/group/delete/{id}` - 物理删除
- `PUT /v1/admin/field/group/update` - 更新
- `GET /v1/admin/field/group/page` - 分页查询
- `GET /v1/admin/field/group/get/{id}` - 详情查询
- `GET /v1/admin/field/group/list` - 列表查询

## 模块依赖关系

```
router-app
├── router-rest
│   ├── router-api
│   └── router-biz
│       └── router-handler
│           ├── router-api
│           └── router-dao
└── router-core

router-boot
├── router-core
└── router-rest

router-rpc
└── router-api
```

## 已实现的功能

✅ **项目骨架**: 完整的Maven多模块结构
✅ **数据库设计**: MySQL和PostgreSQL DDL脚本
✅ **实体映射**: 8个实体类，使用MyBatis-Flex注解
✅ **数据访问**: Mapper和Service层完整CRUD模板
✅ **API接口**: 标准RESTful接口定义
✅ **业务逻辑**: 分层架构，职责清晰
✅ **配置文件**: Spring Boot配置和Swagger文档
✅ **文档**: README和项目结构说明

## 下一步开发建议

1. **数据库初始化**: 运行DDL脚本创建数据库表
2. **配置数据源**: 修改application.yml中的数据库连接
3. **业务逻辑实现**: 完善FieldGroupBiz中的具体业务逻辑
4. **数据处理**: 实现RouterDataHandler中的路由数据处理逻辑
5. **测试**: 添加单元测试和集成测试
6. **扩展API**: 根据需要添加更多业务接口

## 启动项目

```bash
# 1. 初始化数据库
mysql -u root -p < router-dao/src/main/resources/sql/mysql/schema.sql

# 2. 配置数据库连接
# 编辑 router-app/src/main/resources/application.yml

# 3. 编译项目
mvn clean compile

# 4. 启动应用
cd router-app
mvn spring-boot:run

# 5. 访问Swagger文档
# http://localhost:8080/swagger-ui.html
```

项目已完全按照您的元数据规范创建，具备完整的分层架构和标准的CRUD功能模板。