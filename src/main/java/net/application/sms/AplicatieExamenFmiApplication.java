package net.application.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("net.application.sms.*")
@ComponentScan(basePackages = { "net.application.sms.*" })
@EntityScan("net.application.sms.*")   
public class AplicatieExamenFmiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AplicatieExamenFmiApplication.class, args);
	}

}
