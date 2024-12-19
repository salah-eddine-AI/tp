package com.example.Client;

//import ch.qos.logback.core.net.server.Client;
import com.example.Client.entities.Voiture;
import com.example.Client.repositories.VoitureRepository;
import com.example.Client.tools.Client;
import com.example.Client.tools.ClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class VouitureApplication {

	@Autowired
	private VoitureRepository voitureRepository;

	public static void main(String[] args) {
		SpringApplication.run(VouitureApplication.class, args);
	}

	// Bean to insert VoituresVouitureApplication at startup
	/*@Bean
	public CommandLineRunner insertVoitures() {
		return (args) -> {
			Voiture voiture1 = new Voiture("Renault", "1234XYZ", "Clio", 1L, null);
			Voiture voiture2 = new Voiture("Peugeot", "5678ABC", "208", 2L, null);
			Voiture voiture3 = new Voiture("Tesla", "9123XYZ", "Model 3", 3L, null);

			// Saving voitures to the database
			voitureRepository.save(voiture1);
			voitureRepository.save(voiture2);
			voitureRepository.save(voiture3);
		};
	}*/

	@Bean
	public CommandLineRunner initialiserBasem(VoitureRepository voitureRepository, ClientService clientService) {
		return args -> {
			// Fetch clients from the ClientService
			Client cl = clientService.clientById(2L);
			Client c2 = clientService.clientById(1L);

			System.out.println("************************************");
			System.out.println("Client 1 ID: " + c2.getId());
			System.out.println("Client 1 Name: " + c2.getNom());
			System.out.println("************************************");
			System.out.println("Client 2 ID: " + cl.getId());
			System.out.println("Client 2 Name: " + cl.getNom());
			System.out.println("Client 2 Age: " + cl.getAge());
			System.out.println("************************************");


			voitureRepository.save(new Voiture(null, "Toyota", "A 25 333", "Corolla", 1L, c2));
			voitureRepository.save(new Voiture(null, "Renault", "B 6 3456", "Megane", 1L, c2));
			voitureRepository.save(new Voiture(null, "Peugeot", "A 55 4444", "301", 2L, cl));

			System.out.println("Voitures have been initialized in the database.");
		};
	}

}

