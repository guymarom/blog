package com.outbrain.blog.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * User: maromg
 * Date: 01/07/2014
 */
public class BlogApplicationLoader {

    public void load() {
        SpringApplication.run(BlogApplicationContext.class);
    }

    @Configuration
    @EnableJpaRepositories(basePackages = "com.outbrain.blog.repositories")
    @EnableAutoConfiguration
    @Import(RepositoryRestMvcConfiguration.class)
    static class BlogApplicationContext {

        @Bean
        DataSource dataSource() {
            return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
        }

        @Bean
        JpaVendorAdapter jpaVendorAdapter() {
            HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
            adapter.setGenerateDdl(true);
            adapter.setDatabase(Database.H2);
            return adapter;
        }

        @Bean
        LocalContainerEntityManagerFactoryBean entityManagerFactory() {
            LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
            emf.setJpaVendorAdapter(jpaVendorAdapter());
            emf.setDataSource(dataSource());
            emf.setPackagesToScan("com.outbrain.blog.entities");
            emf.setJpaProperties(jpaProperties());
            return emf;
        }

        @Bean
        Properties jpaProperties() {
            Properties props = new Properties();
            props.setProperty("hibernate.hbm2ddl.auto", "create");
            return props;
        }

        @Bean
        Validator beforeCreateBlogPostValidator() {
            return new LocalValidatorFactoryBean();
        }

    }
}
