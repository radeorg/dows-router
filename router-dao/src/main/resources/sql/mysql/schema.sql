-- MySQL DDL for dows_router database

CREATE
DATABASE IF NOT EXISTS dows_router DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE
dows_router;

-- 路由数据表
CREATE TABLE router_data
(
    router_data_id    BIGINT NOT NULL COMMENT '路由数据ID',
    operator_id       BIGINT COMMENT '操作者ID',
    router_account_id BIGINT COMMENT '路由账户ID',
    router_channel_id BIGINT COMMENT '路由通道ID',
    retry             INT      DEFAULT 0 COMMENT '重试次数(默认0)',
    session_type      INT COMMENT '会话类型',
    priority          INT COMMENT '优先级',
    day               INT COMMENT '日',
    month             INT COMMENT '月',
    year              INT COMMENT '年',
    request_id        VARCHAR(255) COMMENT '请求ID',
    data              TEXT COMMENT '数据',
    app_id            VARCHAR(255) COMMENT '应用id',
    state             TINYINT COMMENT '状态',
    deleted           TINYINT  DEFAULT 0 COMMENT '是否删除:0未删除,1已删除',
    ts                DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
    ut                DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (router_data_id),
    INDEX             idx_router_channel_id (router_channel_id),
    INDEX             idx_router_account_id (router_account_id),
    INDEX             idx_state (state),
    INDEX             idx_app_id (app_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='路由数据';

-- 路由会话表
CREATE TABLE router_session
(
    router_session_id BIGINT NOT NULL COMMENT '路由会话ID',
    router_data_id    BIGINT COMMENT '路由数据ID',
    router_channel_id BIGINT COMMENT '路由通道ID',
    input_time        DATETIME COMMENT '输入时间',
    input_token       INT COMMENT '输入token数',
    input_fee         DECIMAL(10, 4) COMMENT '输入费用',
    output_token      INT COMMENT '输出token数',
    output_time       DATETIME COMMENT '输出时间',
    output_fee        DECIMAL(10, 4) COMMENT '输出费用',
    app_id            VARCHAR(255) COMMENT '应用id',
    deleted           TINYINT  DEFAULT 0 COMMENT '是否删除:0未删除,1已删除',
    ts                DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
    ut                DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (router_session_id),
    INDEX             idx_router_data_id (router_data_id),
    INDEX             idx_router_channel_id (router_channel_id),
    INDEX             idx_app_id (app_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='路由会话';

-- 路由通道表
CREATE TABLE router_channel
(
    router_channel_id BIGINT NOT NULL COMMENT '路由通道ID',
    state             INT COMMENT '状态',
    channel_code      VARCHAR(255) COMMENT '通道码',
    channel_type      VARCHAR(255) COMMENT '通道类型',
    description       VARCHAR(500) COMMENT '描述',
    app_id            VARCHAR(255) COMMENT '应用id',
    deleted           TINYINT  DEFAULT 0 COMMENT '是否删除:0未删除,1已删除',
    ts                DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
    ut                DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (router_channel_id),
    UNIQUE KEY uk_channel_code (channel_code),
    INDEX             idx_state (state),
    INDEX             idx_app_id (app_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='路由通道';

-- 路由配置表
CREATE TABLE router_config
(
    router_config_id  BIGINT NOT NULL COMMENT '路由配置ID',
    router_channel_id BIGINT COMMENT '路由通道ID',
    key               VARCHAR(255) COMMENT '配置键名',
    label             VARCHAR(255) COMMENT '标签',
    tag               VARCHAR(50) COMMENT '元素标签[input,select,checkbox...]',
    datatype          VARCHAR(50) COMMENT '数据类型',
    description       VARCHAR(500) COMMENT '描述',
    definition        BOOLEAN COMMENT '自定义标识',
    app_id            VARCHAR(255) COMMENT '应用id',
    deleted           TINYINT  DEFAULT 0 COMMENT '是否删除:0未删除,1已删除',
    ts                DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
    ut                DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (router_config_id),
    INDEX             idx_router_channel_id (router_channel_id),
    INDEX             idx_key (key),
    INDEX             idx_app_id (app_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='路由配置';

-- 路由选项表
CREATE TABLE router_options
(
    router_options_id BIGINT NOT NULL COMMENT '路由选项ID',
    router_channel_id BIGINT COMMENT '路由通道ID',
    router_config_id  BIGINT COMMENT '路由配置ID',
    item              VARCHAR(255) COMMENT '选项item',
    app_id            VARCHAR(255) COMMENT '应用id',
    deleted           TINYINT  DEFAULT 0 COMMENT '是否删除:0未删除,1已删除',
    ts                DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
    ut                DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (router_options_id),
    INDEX             idx_router_channel_id (router_channel_id),
    INDEX             idx_router_config_id (router_config_id),
    INDEX             idx_app_id (app_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='路由选项';

-- 路由设置表
CREATE TABLE router_setting
(
    router_setting_id BIGINT NOT NULL COMMENT '路由设置ID',
    router_config_id  BIGINT COMMENT '路由配置ID',
    identifier_id     BIGINT COMMENT '主体标识ID[账号ID,组织ID...]',
    identifier_type   INT COMMENT '主体标识类型[0:账号,1:组织]',
    val               VARCHAR(1000) COMMENT '键名对应的值',
    app_id            VARCHAR(255) COMMENT '应用id',
    deleted           TINYINT  DEFAULT 0 COMMENT '是否删除:0未删除,1已删除',
    ts                DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
    ut                DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (router_setting_id),
    INDEX             idx_router_config_id (router_config_id),
    INDEX             idx_identifier (identifier_id, identifier_type),
    INDEX             idx_app_id (app_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='路由设置';

-- 路由账户表
CREATE TABLE router_account
(
    router_account_id   BIGINT NOT NULL COMMENT '路由账户ID',
    account_instance_id BIGINT COMMENT '账号实例ID',
    amount              DECIMAL(15, 4) COMMENT '总额',
    balance             DECIMAL(15, 4) COMMENT '余额',
    app_id              VARCHAR(255) COMMENT '应用id',
    deleted             TINYINT  DEFAULT 0 COMMENT '是否删除:0未删除,1已删除',
    ts                  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
    ut                  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (router_account_id),
    INDEX               idx_account_instance_id (account_instance_id),
    INDEX               idx_app_id (app_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='路由账户';

-- 路由统计表
CREATE TABLE router_metering
(
    router_metering_id  BIGINT NOT NULL COMMENT '路由计量ID',
    router_channel_id   BIGINT COMMENT '路由通道ID',
    account_instance_id BIGINT COMMENT '账号实例ID',
    counter             BIGINT COMMENT '计数器(总次数)',
    success_counter     BIGINT COMMENT '成功次数',
    fail_counter        BIGINT COMMENT '失败次数',
    success_rate        DECIMAL(5, 4) COMMENT '成功率',
    fail_rate           DECIMAL(5, 4) COMMENT '失败率',
    day_counter         INT COMMENT '日计数',
    month_counter       INT COMMENT '月计数',
    year_counter        INT COMMENT '年计数',
    app_id              VARCHAR(255) COMMENT '应用id',
    deleted             TINYINT  DEFAULT 0 COMMENT '是否删除:0未删除,1已删除',
    ts                  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
    ut                  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (router_metering_id),
    INDEX               idx_router_channel_id (router_channel_id),
    INDEX               idx_account_instance_id (account_instance_id),
    INDEX               idx_app_id (app_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='路由统计';

-- 外键约束
ALTER TABLE router_data
    ADD CONSTRAINT fk_router_data_channel FOREIGN KEY (router_channel_id) REFERENCES router_channel (router_channel_id);
ALTER TABLE router_data
    ADD CONSTRAINT fk_router_data_account FOREIGN KEY (router_account_id) REFERENCES router_account (router_account_id);
ALTER TABLE router_session
    ADD CONSTRAINT fk_router_session_data FOREIGN KEY (router_data_id) REFERENCES router_data (router_data_id);
ALTER TABLE router_session
    ADD CONSTRAINT fk_router_session_channel FOREIGN KEY (router_channel_id) REFERENCES router_channel (router_channel_id);
ALTER TABLE router_config
    ADD CONSTRAINT fk_router_config_channel FOREIGN KEY (router_channel_id) REFERENCES router_channel (router_channel_id);
ALTER TABLE router_options
    ADD CONSTRAINT fk_router_options_channel FOREIGN KEY (router_channel_id) REFERENCES router_channel (router_channel_id);
ALTER TABLE router_options
    ADD CONSTRAINT fk_router_options_config FOREIGN KEY (router_config_id) REFERENCES router_config (router_config_id);
ALTER TABLE router_setting
    ADD CONSTRAINT fk_router_setting_config FOREIGN KEY (router_config_id) REFERENCES router_config (router_config_id);
ALTER TABLE router_metering
    ADD CONSTRAINT fk_router_metering_channel FOREIGN KEY (router_channel_id) REFERENCES router_channel (router_channel_id);