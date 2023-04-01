package br.luiztoni.demo.webflux.category;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class CategoryRouter {

    @Bean
    public RouterFunction<ServerResponse> route(CategoryHandler handler) {
        return RouterFunctions.route(GET("/categories").and(accept(MediaType.APPLICATION_JSON)), handler::index)
                .andRoute(GET("/categories/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::show)
                .andRoute(POST("/categories").and(accept(MediaType.APPLICATION_JSON)), handler::store)
                .andRoute(PUT("/categories").and(accept(MediaType.APPLICATION_JSON)), handler::store)
                .andRoute(DELETE("/categories/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::delete);
    }
}
