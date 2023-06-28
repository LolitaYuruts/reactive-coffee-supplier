package com.lolitaflamme.coffeesupplier.repository;

import com.lolitaflamme.coffeesupplier.domain.Coffee;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CoffeeRepository extends ReactiveCrudRepository<Coffee, Long> {
}
