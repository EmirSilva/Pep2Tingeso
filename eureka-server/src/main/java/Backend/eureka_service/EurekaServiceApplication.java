package Backend.eureka_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment; // Importar Environment

@SpringBootApplication
@EnableEurekaServer
public class EurekaServiceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(EurekaServiceApplication.class, args);
		Environment env = context.getEnvironment();
		String serverPort = env.getProperty("server.port"); // <-- Añade esta línea
		String defaultZone = env.getProperty("eureka.client.service-url.default-zone");
		String instanceHostname = env.getProperty("eureka.instance.hostname");
		String appName = env.getProperty("spring.application.name");

		System.out.println("DEBUGGING --- Configured server.port: " + serverPort); // <-- Y esta
		System.out.println("DEBUGGING --- Configured default-zone: " + defaultZone);
		System.out.println("DEBUGGING --- Configured instance hostname: " + instanceHostname);
		System.out.println("DEBUGGING --- Configured app name: " + appName);

		// Cierra el contexto para evitar que la aplicación siga corriendo indefinidamente
		// (Puedes quitar esta línea después de depurar)
		// context.close();
	}

}