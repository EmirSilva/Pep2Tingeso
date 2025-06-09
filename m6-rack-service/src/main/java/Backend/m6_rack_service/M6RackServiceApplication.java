package Backend.m6_rack_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class M6RackServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(M6RackServiceApplication.class, args);
	}
}
