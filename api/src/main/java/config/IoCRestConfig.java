package config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.context.annotation.Bean;
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

    @Lazy
    @Bean
    public WireMockServer wireMockServer() { return new WireMockServer(new WireMockConfiguration().dynamicPort()); }

}
