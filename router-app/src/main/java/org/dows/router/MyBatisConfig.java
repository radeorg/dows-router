package org.dows.router;

import com.mybatisflex.core.audit.AuditManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@MapperScan("org.dows.router.mapper")
@Slf4j
public class MyBatisConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        // 设置其他必要配置
        return sessionFactory.getObject();
    }

    @Value("${rade.log.sql.printSql:false}")
    private boolean printSql;

    @PostConstruct
    public void init() {
        if (printSql) {
            //开启审计功能
            AuditManager.setAuditEnable(true);
            //设置 SQL 审计收集器
            AuditManager.setMessageCollector(auditMessage ->
                    log.info("{},{}ms", auditMessage.getFullSql(), auditMessage.getElapsedTime())
            );
        }
    }
}