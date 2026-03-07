package canteen_bite.example.Canteen_bite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CanteenBiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanteenBiteApplication.class, args);
	}

}
