package Backend.m2_group_discount_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class M2GroupDiscountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(M2GroupDiscountServiceApplication.class, args);
	}

}
