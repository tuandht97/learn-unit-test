package pl.com.coders.shop2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "pl.com.coders.shop2.domain")
public class

Shop2Application {
	public static void main(String[] args) {
		SpringApplication.run(Shop2Application.class, args);
	}
}
