package ru.job4j.accident.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@PropertySource("classpath:app.properties")
@EnableTransactionManagement
public class HbmConfig {

    @Bean
    public DataSource ds(@Value("${jdbc.driver}") String driver,
                         @Value("${jdbc.url}") String url,
                         @Value("${jdbc.username}") String username,
                         @Value("${jdbc.password}") String password) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(@Value("${hibernate.dialect}") String dialect,
                                                  @Value("${hibernate.show_sql}") String showSql,
                                                  @Value("${hibernate.show_sql}") String formatSql,
                                                  @Value("${hibernate.use_sql_comments}") String commentsSql,
                                                  @Value("${hibernate.hbm2ddl.auto}") String auto,
                                                  DataSource ds) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(ds);
        sessionFactory.setPackagesToScan("ru.job4j.accident.model");
        Properties cfg = new Properties();
        cfg.setProperty("hibernate.dialect", dialect);
        cfg.setProperty("hibernate.show_sql", showSql);
        cfg.setProperty("hibernate.format_sql", formatSql);
        cfg.setProperty("hibernate.use_sql_comments", commentsSql);
        cfg.setProperty("hibernate.hbm2ddl.auto", auto);
        sessionFactory.setHibernateProperties(cfg);
        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager htx(SessionFactory sf) {
        HibernateTransactionManager tx = new HibernateTransactionManager();
        tx.setSessionFactory(sf);
        return tx;
    }
}