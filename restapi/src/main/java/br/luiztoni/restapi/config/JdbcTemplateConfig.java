package br.luiztoni.restapi.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateConfig {
    @Bean
    public JdbcTemplate customJdbcTemplate(@Qualifier("customDataSource") DataSource customDataSource) {
        return new JdbcTemplate(customDataSource);
    }
}
