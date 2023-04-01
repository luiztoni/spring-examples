package br.luiztoni.demo.webflux;

import com.mongodb.reactivestreams.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackages = "br.luiztoni.demo.webflux")
public class WebfluxApplication {

	@Value("${demo.webflux.mongodb.database}")
	private String database;
	public static void main(String[] args) {
		SpringApplication.run(WebfluxApplication.class, args);
	}

	@Autowired
	MongoClient mongoClient;

	@Bean
	public ReactiveMongoTemplate reactiveMongoTemplate() {
		return new ReactiveMongoTemplate(mongoClient, database);
	}

}
