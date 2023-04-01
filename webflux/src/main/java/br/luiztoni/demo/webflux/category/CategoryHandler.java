package br.luiztoni.demo.webflux.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class CategoryHandler {
    @Autowired
    private CategoryService service;

    public Mono<ServerResponse> show(ServerRequest request) {
        String id = request.pathVariable("id");
        return ok().contentType(MediaType.APPLICATION_JSON).body(service.show(id), Category.class);
    }

    public Mono<ServerResponse> index(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(service.index(), Category.class);
    }

    public Mono<ServerResponse> store(ServerRequest request) {
        final Mono<Category> hero = request.bodyToMono(Category.class);
        return ok().contentType(MediaType.APPLICATION_JSON).body(fromPublisher(hero.flatMap(service::store), Category.class));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return ok().contentType(MediaType.APPLICATION_JSON).body(service.delete(id), Void.class);
    }
}
