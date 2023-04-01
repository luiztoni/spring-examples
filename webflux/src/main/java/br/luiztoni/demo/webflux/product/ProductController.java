package br.luiztoni.demo.webflux.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;

@RestController
@RequestMapping({ "/products", "/" })
public class ProductController {

    @Autowired
    private ProductService service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> index() {
        return service.index();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> show(@PathVariable String id) {
        return service.show(id).map(power -> ResponseEntity.ok(power)).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping
    @PostMapping
    public Mono<ResponseEntity<Product>> store(@RequestBody Product product) {
        return service.store(product).map(updated -> new ResponseEntity<>(updated, HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return service.delete(id).map(deleted -> ResponseEntity.ok(deleted)).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tuple2<Long, Product>> stream() {
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(10));
        Flux<Product> products = service.index();
        return Flux.zip(interval, products);
    }
}
