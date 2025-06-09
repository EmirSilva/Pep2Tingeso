package Backend.m6_rack_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced // ¡Esta anotación es CRUCIAL!
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
