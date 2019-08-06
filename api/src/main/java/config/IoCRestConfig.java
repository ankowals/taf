package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import service.ApiClientService;

@Configuration
public class IoCRestConfig {

    @Lazy
    @Bean
    public ApiClientService apiClientService() {
        return new ApiClientService();
    }

}
