package com.ravi.microservice2;

import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EnableDiscoveryClient
@SpringBootApplication
public class ItemCatalogsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemCatalogsApplication.class, args);
	}

}

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
class Item{
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	public Item(String name) {
		this.name=name;
	}
	
}

@RepositoryRestResource
interface ItemRepository extends JpaRepository<Item, Long>{}

@Component
class ItemInitializer implements CommandLineRunner{

	private final ItemRepository itemRepository;
	
	
	public ItemInitializer(ItemRepository itemRepository) {
		this.itemRepository=itemRepository;
	}
	
	@Override
	public void run(String... args) throws Exception {
			Stream.of("science","maths","code","spring","hibernate","sql","javascript")
			.forEach(item-> itemRepository.save(new Item(item)));
			
			itemRepository.findAll().forEach(System.out::println);
	}
	
}