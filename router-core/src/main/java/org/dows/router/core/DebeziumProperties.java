
// DebeziumConfigProperties.java
package org.dows.router.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Data
@Component
@ConfigurationProperties(prefix = "debezium")
public class DebeziumProperties {

    private Properties properties = new Properties();

    // 为了方便使用，也可以保留一些常用的直接属性
    private String name = "mysql-connector";
    private String topicPrefix = "router-";

    // MySQL 配置
    private Mysql mysql = new Mysql();

    // 偏移量配置
    private Offset offset = new Offset();

    // Schema 配置
    private Schema schema = new Schema();

    // 转换器配置
    private Converter converter = new Converter();

    @Data
    public static class Mysql {
        private String hostname = "localhost";
        private String user = "debezium";
        private String password = "123456";
        private String port = "13306";
        private String serverId = "184055";
        private String serverName = "mysql-server";
        private String databaseInclude = "dev_bole";
        private String tableInclude = "dev_bole.router_data";
    }

    @Data
    public static class Offset {
        private String flushIntervalMs = "5000";
        private String storage = "org.apache.kafka.connect.storage.FileOffsetBackingStore";
        private String file = "mysql-offset.dat";
    }

    @Data
    public static class Schema {
        private String includeChanges = "false";
        private String storeOnlyCapturedTablesDdl = "true";
        private String historyInternal = "io.debezium.storage.file.history.FileSchemaHistory";
        private String historyFile = "schema-history.dat";
    }

    @Data
    public static class Converter {
        private String value = "org.apache.kafka.connect.json.JsonConverter";
        private String schemasEnable = "false";
    }
}