package com.example.anywrpfe;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing
public class AnywrpfeApplication {

	@Autowired
	private JavaMailSenderImpl mailSender;
	public static void main(String[] args) {

		SpringApplication.run(AnywrpfeApplication.class, args);
	}

	@PostConstruct
	public void init() {
		System.out.println("Current mail configuration: ");
		System.out.println("Host: " + mailSender.getHost());
		System.out.println("Port: " + mailSender.getPort());
		System.out.println("Username: " + mailSender.getUsername());
	}

	@Value("${spring.mail.host}")
	private String mailHost;

	@Value("${spring.mail.port}")
	private Integer mailPort;

	@PostConstruct
	public void checkMailConfig() {
		System.out.println("Mail Host: " + mailHost);
		System.out.println("Mail Port: " + mailPort);
	}

//	@Bean
//	public CommandLineRunner commandLineRunner(
//			AuthenticationService service
//	) {
//		return args -> {
//			var admin = RegisterRequest.builder()
//					.firstname("Admin")
//					.lastname("Admin")
//					.email("admin@mail.com")
//					.password("password")
//					.role(Role.ADMIN)
//					.build();
//			System.out.println("Admin token: " + service.register(admin).getAccessToken());
//
//			var rh = RegisterRequest.builder()
//					.firstname("rh")
//					.lastname("rh")
//					.email("rh@mail.com")
//					.password("password")
//					.role(Role.GESTRH)
//					.build();
//			System.out.println("GEST RH token: " + service.register(rh).getAccessToken());
//
//			var manager = RegisterRequest.builder()
//					.firstname("Manager")
//					.lastname("Manager")
//					.email("manager@mail.com")
//					.password("password")
//					.role(Role.MANAGER)
//					.build();
//			System.out.println("Manager token: " + service.register(manager).getAccessToken());
//
//
//			var collaborateur = RegisterRequest.builder()
//					.firstname("collab")
//					.lastname("collab")
//					.email("collab@mail.com")
//					.password("password")
//					.role(Role.COLLABORATOR)
//					.build();
//			System.out.println("Collab token: " + service.register(collaborateur).getAccessToken());
//
//		};
//	}



}
