package br.luiztoni.demo.webflux.user;
import br.luiztoni.demo.webflux.user.config.auth.AuthRequest;
import br.luiztoni.demo.webflux.user.config.auth.AuthResponse;
import br.luiztoni.demo.webflux.user.config.util.PasswordUtil;
import br.luiztoni.demo.webflux.user.config.util.JwtUtil;
import br.luiztoni.demo.webflux.user.config.auth.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import java.util.Arrays;

@RestController
public class UserController {
    @Autowired
    private JwtUtil jwt;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private UserRepository repository;

    private final User user = new User("user", passwordUtil.encode("user"), true, Arrays.asList(Role.ROLE_USER));

    private final User admin = new User("admin", passwordUtil.encode("admin"), true, Arrays.asList(Role.ROLE_ADMIN));

    private Mono<User> inMemoryAuth(String username) {
        if (username.equals(user.getUsername()))
            return Mono.just(user);
        if (username.equals(admin.getUsername()))
            return Mono.just(admin);
        return Mono.empty();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Mono<ResponseEntity<Object>> login(@RequestBody AuthRequest request) {
        return this.inMemoryAuth(request.getUsername()).map((user) -> {
            if (passwordUtil.encode(request.getPassword()).equals(user.getPassword())) {
                return ResponseEntity.status(HttpStatus.OK).header("Authorization", jwt.getToken(user)).build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Mono<ResponseEntity<?>> register(@RequestBody User user) {
        user.setPassword(passwordUtil.encode(user.getPassword()));
        return repository.save(user).thenReturn(ResponseEntity.ok(new AuthResponse(jwt.getToken(user)))) ;
    }
}