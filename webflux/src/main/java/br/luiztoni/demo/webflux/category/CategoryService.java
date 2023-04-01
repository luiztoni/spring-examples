package br.luiztoni.demo.webflux.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    public Mono<Category> show(String id) {
        return repository.findById(id);
    }

    public Flux<Category> index() {
        return repository.findAll();
    }

    public Mono<Category> store(Category category) {
        return repository.save(category);
    }

    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }
}
