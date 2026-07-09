package br.com.medibridge.medi_bridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MediBridgeApplication {


	public static void main(String[] args) {
		SpringApplication.run(MediBridgeApplication.class, args);
	}

}
