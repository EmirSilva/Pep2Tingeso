package Backend.m7_report_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class M7ReportServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(M7ReportServiceApplication.class, args);
	}
}
