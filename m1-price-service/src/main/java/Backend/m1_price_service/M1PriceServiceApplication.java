package Backend.m1_price_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class M1PriceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(M1PriceServiceApplication.class, args);
	}

}
