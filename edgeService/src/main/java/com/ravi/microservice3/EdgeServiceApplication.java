package com.ravi.microservice3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.Data;

@EnableFeignClients
@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class EdgeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdgeServiceApplication.class, args);
	}

}

@Data
class Item{
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name=name;
	}
}
@FeignClient("item-catalog-service")
interface ItemClient{
	
	@GetMapping("/items")
	static
	Collection<Item> readItems() {
		// TODO Auto-generated method stub
		return null;
	}
}

@RestController
class GoodItemAdapterRestController{
	
	private final ItemClient itemClient;
	
	GoodItemAdapterRestController(ItemClient itemClient){
		this.itemClient=itemClient;
	}
	public Collection<Item> fallback(){
		return new ArrayList<>();
	}
	
	@HystrixCommand(fallbackMethod="fallback")
	@GetMapping("/top-brands")
	@CrossOrigin(origins = "*")
	public Collection<Item> goodItem(){
		return ItemClient.readItems().stream().
				filter(this::isGreat).collect(Collectors.toList());
	}
	
	private boolean isGreat(Item item) {
		return !item.getName().equals("code") &&
				!item.getName().equals("science");
	}
}


