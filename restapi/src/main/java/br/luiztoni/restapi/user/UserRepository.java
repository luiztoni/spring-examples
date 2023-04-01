package br.luiztoni.restapi.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepository {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    @Autowired
    @Qualifier("customJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Cacheable(cacheNames = "Users", key="#root.method.name")
    public List<User> index() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Cacheable(cacheNames = "Users", key = "#id")
    public User read(int id) {
        LOG.info("Getting user with ID {}.", id);
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", new Object[] { id }, new UserMapper());
    }

    @CacheEvict(cacheNames = "Users", allEntries=true)
    public boolean delete(User user) {
        return jdbcTemplate.update("DELETE FROM users WHERE id = ?", user.getId()) > 0;
    }

    @CachePut(cacheNames = "Users", key = "#user.id")
    public boolean update(User user) {
        return jdbcTemplate.update("UPDATE users SET name = ? , age = ? WHERE id = ?", user.getName(), user.getAge(), user.getId()) > 0;
    }

    @CacheEvict(cacheNames = "Users", allEntries=true)
    public boolean create(User user) {
        return jdbcTemplate.update("INSERT INTO users(name, age) values(?, ?)", user.getName(), user.getAge()) > 0;
    }
}
