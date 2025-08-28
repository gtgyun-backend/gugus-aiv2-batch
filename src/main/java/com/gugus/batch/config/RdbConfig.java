package com.gugus.batch.config;

import com.zaxxer.hikari.HikariDataSource;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = "com.gugus.batch.database.repositories")
public class RdbConfig {

    @Value("${spring.datasource.url}")
    public String jdbcUrl;

    @Value("${spring.datasource.driver-class-name}")
    public String driverClassName;

    @Value("${spring.datasource.hikari.pool-name}")
    public String poolName;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    public Integer maximumPoolSize;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    public String ddlAuto;

    @Value("${spring.jpa.show-sql}")
    public String showSql;

    @Value("${spring.jpa.properties.hibernate.default_batch_fetch_size}")
    public Integer batchSize;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public HikariDataSource masterDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(this.jdbcUrl);
        hikariDataSource.setDriverClassName(this.driverClassName);
        hikariDataSource.setPoolName(this.poolName);
        hikariDataSource.setMaximumPoolSize(this.maximumPoolSize);
        hikariDataSource.setConnectionTestQuery("select 1");
        return hikariDataSource;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource masterDataSource) {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(masterDataSource);
        entityManager.setPackagesToScan("com.gugus.batch.database.entities");
        entityManager.setPersistenceUnitName("entityManager");
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties properties = additionalProperties();
        // Hibernate Interceptor 등록
        properties.setProperty("hibernate.session_factory.interceptor",
                "com.gugus.batch.auditlog.service.AuditInterceptor");

        entityManager.setJpaProperties(properties);

        String ddlAuto = (String) entityManager.getJpaPropertyMap().get("hibernate.hbm2ddl.auto");
        if (!"validate".equals(ddlAuto) && !"none".equals(ddlAuto)) {
            throw new RuntimeException("Only Validate and None types can run this projects");
        }
        return entityManager;
    }

    @Bean
    public JpaTransactionManager transactionManager(DataSource dataSource) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource); // 의존성 주입으로 변경
        return transactionManager;
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
        properties.setProperty("hibernate.show_sql", showSql);
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.default_batch_fetch_size", String.valueOf(batchSize));
        return properties;
    }
}
