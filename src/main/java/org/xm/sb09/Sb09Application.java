package org.xm.sb09;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Sb09Application {

	public static void main(String[] args) {
		SpringApplication.run(Sb09Application.class, args);
	}

}
