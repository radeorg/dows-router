-- PostgreSQL DDL for dows_router database

CREATE DATABASE dows_router WITH ENCODING 'UTF8';

\c dows_router;

-- 路由数据表
CREATE TABLE router_data (
    router_data_id BIGINT NOT NULL,
    operator_id BIGINT,
    router_account_id BIGINT,
    router_channel_id BIGINT,
    retry INTEGER DEFAULT 0,
    session_type INTEGER,
    priority INTEGER,
    day INTEGER,
    month INTEGER,
    year INTEGER,
    request_id VARCHAR(255),
    data TEXT,
    app_id VARCHAR(255),
    state SMALLINT,
    deleted SMALLINT DEFAULT 0,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ut TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (router_data_id)
);

COMMENT ON TABLE router_data IS '路由数据';
COMMENT ON COLUMN router_data.router_data_id IS '路由数据ID';
COMMENT ON COLUMN router_data.operator_id IS '操作者ID';
COMMENT ON COLUMN router_data.router_account_id IS '路由账户ID';
COMMENT ON COLUMN router_data.router_channel_id IS '路由通道ID';
COMMENT ON COLUMN router_data.retry IS '重试次数(默认0)';
COMMENT ON COLUMN router_data.session_type IS '会话类型';
COMMENT ON COLUMN router_data.priority IS '优先级';
COMMENT ON COLUMN router_data.day IS '日';
COMMENT ON COLUMN router_data.month IS '月';
COMMENT ON COLUMN router_data.year IS '年';
COMMENT ON COLUMN router_data.request_id IS '请求ID';
COMMENT ON COLUMN router_data.data IS '数据';
COMMENT ON COLUMN router_data.app_id IS '应用id';
COMMENT ON COLUMN router_data.state IS '状态';
COMMENT ON COLUMN router_data.deleted IS '是否删除:0未删除,1已删除';
COMMENT ON COLUMN router_data.ts IS '时间戳';
COMMENT ON COLUMN router_data.ut IS '更新时间';

-- 路由会话表
CREATE TABLE router_session (
    router_session_id BIGINT NOT NULL,
    router_data_id BIGINT,
    router_channel_id BIGINT,
    input_time TIMESTAMP,
    input_token INTEGER,
    input_fee DECIMAL(10,4),
    output_token INTEGER,
    output_time TIMESTAMP,
    output_fee DECIMAL(10,4),
    app_id VARCHAR(255),
    deleted SMALLINT DEFAULT 0,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ut TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (router_session_id)
);

COMMENT ON TABLE router_session IS '路由会话';
COMMENT ON COLUMN router_session.router_session_id IS '路由会话ID';
COMMENT ON COLUMN router_session.router_data_id IS '路由数据ID';
COMMENT ON COLUMN router_session.router_channel_id IS '路由通道ID';
COMMENT ON COLUMN router_session.input_time IS '输入时间';
COMMENT ON COLUMN router_session.input_token IS '输入token数';
COMMENT ON COLUMN router_session.input_fee IS '输入费用';
COMMENT ON COLUMN router_session.output_token IS '输出token数';
COMMENT ON COLUMN router_session.output_time IS '输出时间';
COMMENT ON COLUMN router_session.output_fee IS '输出费用';
COMMENT ON COLUMN router_session.app_id IS '应用id';
COMMENT ON COLUMN router_session.deleted IS '是否删除:0未删除,1已删除';
COMMENT ON COLUMN router_session.ts IS '时间戳';
COMMENT ON COLUMN router_session.ut IS '更新时间';

-- 路由通道表
CREATE TABLE router_channel (
    router_channel_id BIGINT NOT NULL,
    state INTEGER,
    channel_code VARCHAR(255),
    channel_type VARCHAR(255),
    description VARCHAR(500),
    app_id VARCHAR(255),
    deleted SMALLINT DEFAULT 0,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ut TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (router_channel_id),
    UNIQUE (channel_code)
);

COMMENT ON TABLE router_channel IS '路由通道';
COMMENT ON COLUMN router_channel.router_channel_id IS '路由通道ID';
COMMENT ON COLUMN router_channel.state IS '状态';
COMMENT ON COLUMN router_channel.channel_code IS '通道码';
COMMENT ON COLUMN router_channel.channel_type IS '通道类型';
COMMENT ON COLUMN router_channel.description IS '描述';
COMMENT ON COLUMN router_channel.app_id IS '应用id';
COMMENT ON COLUMN router_channel.deleted IS '是否删除:0未删除,1已删除';
COMMENT ON COLUMN router_channel.ts IS '时间戳';
COMMENT ON COLUMN router_channel.ut IS '更新时间';

-- 路由配置表
CREATE TABLE router_config (
    router_config_id BIGINT NOT NULL,
    router_channel_id BIGINT,
    key VARCHAR(255),
    label VARCHAR(255),
    tag VARCHAR(50),
    datatype VARCHAR(50),
    description VARCHAR(500),
    definition BOOLEAN,
    app_id VARCHAR(255),
    deleted SMALLINT DEFAULT 0,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ut TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (router_config_id)
);

COMMENT ON TABLE router_config IS '路由配置';
COMMENT ON COLUMN router_config.router_config_id IS '路由配置ID';
COMMENT ON COLUMN router_config.router_channel_id IS '路由通道ID';
COMMENT ON COLUMN router_config.key IS '配置键名';
COMMENT ON COLUMN router_config.label IS '标签';
COMMENT ON COLUMN router_config.tag IS '元素标签[input,select,checkbox...]';
COMMENT ON COLUMN router_config.datatype IS '数据类型';
COMMENT ON COLUMN router_config.description IS '描述';
COMMENT ON COLUMN router_config.definition IS '自定义标识';
COMMENT ON COLUMN router_config.app_id IS '应用id';
COMMENT ON COLUMN router_config.deleted IS '是否删除:0未删除,1已删除';
COMMENT ON COLUMN router_config.ts IS '时间戳';
COMMENT ON COLUMN router_config.ut IS '更新时间';

-- 路由选项表
CREATE TABLE router_options (
    router_options_id BIGINT NOT NULL,
    router_channel_id BIGINT,
    router_config_id BIGINT,
    item VARCHAR(255),
    app_id VARCHAR(255),
    deleted SMALLINT DEFAULT 0,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ut TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (router_options_id)
);

COMMENT ON TABLE router_options IS '路由选项';
COMMENT ON COLUMN router_options.router_options_id IS '路由选项ID';
COMMENT ON COLUMN router_options.router_channel_id IS '路由通道ID';
COMMENT ON COLUMN router_options.router_config_id IS '路由配置ID';
COMMENT ON COLUMN router_options.item IS '选项item';
COMMENT ON COLUMN router_options.app_id IS '应用id';
COMMENT ON COLUMN router_options.deleted IS '是否删除:0未删除,1已删除';
COMMENT ON COLUMN router_options.ts IS '时间戳';
COMMENT ON COLUMN router_options.ut IS '更新时间';

-- 路由设置表
CREATE TABLE router_setting (
    router_setting_id BIGINT NOT NULL,
    router_config_id BIGINT,
    identifier_id BIGINT,
    identifier_type INTEGER,
    val VARCHAR(1000),
    app_id VARCHAR(255),
    deleted SMALLINT DEFAULT 0,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ut TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (router_setting_id)
);

COMMENT ON TABLE router_setting IS '路由设置';
COMMENT ON COLUMN router_setting.router_setting_id IS '路由设置ID';
COMMENT ON COLUMN router_setting.router_config_id IS '路由配置ID';
COMMENT ON COLUMN router_setting.identifier_id IS '主体标识ID[账号ID,组织ID...]';
COMMENT ON COLUMN router_setting.identifier_type IS '主体标识类型[0:账号,1:组织]';
COMMENT ON COLUMN router_setting.val IS '键名对应的值';
COMMENT ON COLUMN router_setting.app_id IS '应用id';
COMMENT ON COLUMN router_setting.deleted IS '是否删除:0未删除,1已删除';
COMMENT ON COLUMN router_setting.ts IS '时间戳';
COMMENT ON COLUMN router_setting.ut IS '更新时间';

-- 路由账户表
CREATE TABLE router_account (
    router_account_id BIGINT NOT NULL,
    account_instance_id BIGINT,
    amount DECIMAL(15,4),
    balance DECIMAL(15,4),
    app_id VARCHAR(255),
    deleted SMALLINT DEFAULT 0,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ut TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (router_account_id)
);

COMMENT ON TABLE router_account IS '路由账户';
COMMENT ON COLUMN router_account.router_account_id IS '路由账户ID';
COMMENT ON COLUMN router_account.account_instance_id IS '账号实例ID';
COMMENT ON COLUMN router_account.amount IS '总额';
COMMENT ON COLUMN router_account.balance IS '余额';
COMMENT ON COLUMN router_account.app_id IS '应用id';
COMMENT ON COLUMN router_account.deleted IS '是否删除:0未删除,1已删除';
COMMENT ON COLUMN router_account.ts IS '时间戳';
COMMENT ON COLUMN router_account.ut IS '更新时间';

-- 路由统计表
CREATE TABLE router_metering (
    router_metering_id BIGINT NOT NULL,
    router_channel_id BIGINT,
    account_instance_id BIGINT,
    counter BIGINT,
    success_counter BIGINT,
    fail_counter BIGINT,
    success_rate DECIMAL(5,4),
    fail_rate DECIMAL(5,4),
    day_counter INTEGER,
    month_counter INTEGER,
    year_counter INTEGER,
    app_id VARCHAR(255),
    deleted SMALLINT DEFAULT 0,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ut TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (router_metering_id)
);

COMMENT ON TABLE router_metering IS '路由统计';
COMMENT ON COLUMN router_metering.router_metering_id IS '路由计量ID';
COMMENT ON COLUMN router_metering.router_channel_id IS '路由通道ID';
COMMENT ON COLUMN router_metering.account_instance_id IS '账号实例ID';
COMMENT ON COLUMN router_metering.counter IS '计数器(总次数)';
COMMENT ON COLUMN router_metering.success_counter IS '成功次数';
COMMENT ON COLUMN router_metering.fail_counter IS '失败次数';
COMMENT ON COLUMN router_metering.success_rate IS '成功率';
COMMENT ON COLUMN router_metering.fail_rate IS '失败率';
COMMENT ON COLUMN router_metering.day_counter IS '日计数';
COMMENT ON COLUMN router_metering.month_counter IS '月计数';
COMMENT ON COLUMN router_metering.year_counter IS '年计数';
COMMENT ON COLUMN router_metering.app_id IS '应用id';
COMMENT ON COLUMN router_metering.deleted IS '是否删除:0未删除,1已删除';
COMMENT ON COLUMN router_metering.ts IS '时间戳';
COMMENT ON COLUMN router_metering.ut IS '更新时间';

-- 创建索引
CREATE INDEX idx_router_data_channel_id ON router_data(router_channel_id);
CREATE INDEX idx_router_data_account_id ON router_data(router_account_id);
CREATE INDEX idx_router_data_state ON router_data(state);
CREATE INDEX idx_router_data_app_id ON router_data(app_id);

CREATE INDEX idx_router_session_data_id ON router_session(router_data_id);
CREATE INDEX idx_router_session_channel_id ON router_session(router_channel_id);
CREATE INDEX idx_router_session_app_id ON router_session(app_id);

CREATE INDEX idx_router_channel_state ON router_channel(state);
CREATE INDEX idx_router_channel_app_id ON router_channel(app_id);

CREATE INDEX idx_router_config_channel_id ON router_config(router_channel_id);
CREATE INDEX idx_router_config_key ON router_config(key);
CREATE INDEX idx_router_config_app_id ON router_config(app_id);

CREATE INDEX idx_router_options_channel_id ON router_options(router_channel_id);
CREATE INDEX idx_router_options_config_id ON router_options(router_config_id);
CREATE INDEX idx_router_options_app_id ON router_options(app_id);

CREATE INDEX idx_router_setting_config_id ON router_setting(router_config_id);
CREATE INDEX idx_router_setting_identifier ON router_setting(identifier_id, identifier_type);
CREATE INDEX idx_router_setting_app_id ON router_setting(app_id);

CREATE INDEX idx_router_account_instance_id ON router_account(account_instance_id);
CREATE INDEX idx_router_account_app_id ON router_account(app_id);

CREATE INDEX idx_router_metering_channel_id ON router_metering(router_channel_id);
CREATE INDEX idx_router_metering_account_id ON router_metering(account_instance_id);
CREATE INDEX idx_router_metering_app_id ON router_metering(app_id);

-- 外键约束
ALTER TABLE router_data ADD CONSTRAINT fk_router_data_channel FOREIGN KEY (router_channel_id) REFERENCES router_channel(router_channel_id);
ALTER TABLE router_data ADD CONSTRAINT fk_router_data_account FOREIGN KEY (router_account_id) REFERENCES router_account(router_account_id);
ALTER TABLE router_session ADD CONSTRAINT fk_router_session_data FOREIGN KEY (router_data_id) REFERENCES router_data(router_data_id);
ALTER TABLE router_session ADD CONSTRAINT fk_router_session_channel FOREIGN KEY (router_channel_id) REFERENCES router_channel(router_channel_id);
ALTER TABLE router_config ADD CONSTRAINT fk_router_config_channel FOREIGN KEY (router_channel_id) REFERENCES router_channel(router_channel_id);
ALTER TABLE router_options ADD CONSTRAINT fk_router_options_channel FOREIGN KEY (router_channel_id) REFERENCES router_channel(router_channel_id);
ALTER TABLE router_options ADD CONSTRAINT fk_router_options_config FOREIGN KEY (router_config_id) REFERENCES router_config(router_config_id);
ALTER TABLE router_setting ADD CONSTRAINT fk_router_setting_config FOREIGN KEY (router_config_id) REFERENCES router_config(router_config_id);
ALTER TABLE router_metering ADD CONSTRAINT fk_router_metering_channel FOREIGN KEY (router_channel_id) REFERENCES router_channel(router_channel_id);