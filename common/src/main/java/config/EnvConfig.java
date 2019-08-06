package config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = { "config" })
@PropertySource("classpath:env.properties")
public class EnvConfig {

    @Value("${db.url}")
    private String dbUrl;

    @Value("${app.port}")
    private int appPort;

    @Value("${web.url}")
    private String webUrl;

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public int getAppPort() {
        return appPort;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
