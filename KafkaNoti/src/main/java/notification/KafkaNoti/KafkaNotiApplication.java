package notification.KafkaNoti;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class KafkaNotiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaNotiApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate) {
		return args -> {
			kafkaTemplate.send("martial_art", "snow saber");
			kafkaTemplate.send("martial_art", "shadow saber");
			kafkaTemplate.send("martial_art", "heavenly demon");
		};
	}
}
