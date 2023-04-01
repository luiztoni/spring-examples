package br.luiztoni.demo.webflux.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    public Mono<Product> show(String id) {
        return repository.findById(id);
    }

    public Flux<Product> index() {
        return repository.findAll();
    }

    public Mono<Product> store(Product product) {
        return repository.save(product);
    }

    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }
}
