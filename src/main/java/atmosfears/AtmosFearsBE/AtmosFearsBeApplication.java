package atmosfears.AtmosFearsBE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "classpath:config/db.properties")
public class AtmosFearsBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtmosFearsBeApplication.class, args);
	}
}
