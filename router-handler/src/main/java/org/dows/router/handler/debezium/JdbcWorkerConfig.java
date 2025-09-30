package org.dows.router.handler.debezium;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.runtime.WorkerConfig;

import java.util.Map;

public class JdbcWorkerConfig extends WorkerConfig {

    /**
     * The jdbc info of the offset storage jdbc.
     */
    public static final String OFFSET_STORAGE_JDBC_URL_CONFIG = "offset.storage.jdbc.connection.url";
    public static final String OFFSET_STORAGE_JDBC_USER_CONFIG = "offset.storage.jdbc.connection.user";
    public static final String OFFSET_STORAGE_JDBC_PASSWORD_CONFIG = "offset.storage.jdbc.connection.password";
    public static final String OFFSET_STORAGE_JDBC_TABLE_NAME_CONFIG = "offset.storage.jdbc.table.name";
    private static final ConfigDef CONFIG;
    private static final String OFFSET_STORAGE_JDBC_URL_DOC = "database to store source connector offsets";
    private static final String OFFSET_STORAGE_JDBC_USER_DOC = "database of user to store source connector offsets";
    private static final String OFFSET_STORAGE_JDBC_PASSWORD_DOC = "database of password to store source connector offsets";
    private static final String OFFSET_STORAGE_JDBC_TABLE_NAME_DOC = "table name to store source connector offsets";


    static {
        CONFIG = baseConfigDef()
                .define(OFFSET_STORAGE_JDBC_URL_CONFIG,
                        ConfigDef.Type.STRING,
                        ConfigDef.Importance.HIGH,
                        OFFSET_STORAGE_JDBC_URL_DOC)
                .define(OFFSET_STORAGE_JDBC_USER_CONFIG,
                        ConfigDef.Type.STRING,
                        ConfigDef.Importance.HIGH,
                        OFFSET_STORAGE_JDBC_USER_DOC)
                .define(OFFSET_STORAGE_JDBC_PASSWORD_CONFIG,
                        ConfigDef.Type.STRING,
                        ConfigDef.Importance.HIGH,
                        OFFSET_STORAGE_JDBC_PASSWORD_DOC)
                .define(OFFSET_STORAGE_JDBC_TABLE_NAME_CONFIG,
                        ConfigDef.Type.STRING,
                        ConfigDef.Importance.HIGH,
                        OFFSET_STORAGE_JDBC_TABLE_NAME_DOC);
    }

    public JdbcWorkerConfig(Map<String, String> props) {
        super(CONFIG, props);
    }
}

