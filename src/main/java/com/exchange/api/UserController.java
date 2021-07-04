package com.exchange.api;

import com.exchange.redis.User;
import com.exchange.redis.UserRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

    @Autowired
    private UserRedisService userRedisService;

    @PostMapping("/redis/{key}")
    public Mono<Boolean> put(@PathVariable("key") String key,
                             @RequestBody User user) {

        return userRedisService.put(key, user);
    }

    @GetMapping("/redis/{key}")
    public Mono<User> get(@PathVariable("key") String key) {

        return userRedisService.get(key);
    }

    @DeleteMapping("/redis/{key}")
    public Mono<Boolean> delete(@PathVariable("key") String key) {

        return userRedisService.delete(key);
    }
}