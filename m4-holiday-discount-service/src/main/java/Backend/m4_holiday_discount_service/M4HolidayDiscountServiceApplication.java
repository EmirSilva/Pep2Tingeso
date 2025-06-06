package Backend.m4_holiday_discount_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class M4HolidayDiscountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(M4HolidayDiscountServiceApplication.class, args);
	}

}
