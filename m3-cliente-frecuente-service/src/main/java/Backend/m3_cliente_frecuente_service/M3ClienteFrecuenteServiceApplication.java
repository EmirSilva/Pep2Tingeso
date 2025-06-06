package Backend.m3_cliente_frecuente_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class M3ClienteFrecuenteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(M3ClienteFrecuenteServiceApplication.class, args);
	}

}
