package com.keshri.gateway;

//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import java.util.HashMap;
//import java.util.Map;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner commandLineRunner(ConfigurableEnvironment environment) {
//		return args -> {
//			System.out.println("---- Loaded Properties ----");
//			Map<String, Object> props = new HashMap<>();
//			for (PropertySource<?> propertySource : environment.getPropertySources()) {
//				if (propertySource instanceof EnumerablePropertySource) {
//					for (String key : ((EnumerablePropertySource<?>) propertySource).getPropertyNames()) {
//						props.put(key, environment.getProperty(key));
//					}
//				}
//			}
//			props.forEach((key, value) -> System.out.println(key + "=" + value));
//		};
//	}
//}
}