package com.lolitaflamme.coffeesupplier.service;

import com.lolitaflamme.coffeesupplier.domain.Coffee;
import com.lolitaflamme.coffeesupplier.repository.CoffeeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CoffeeService {

    @NonNull
    private final CoffeeRepository coffeeRepository;

    public Flux<Coffee> getCoffees() {
        return coffeeRepository.findAll();
    }

    public Flux<Coffee> createCoffees(Iterable<Coffee> coffees) {
        return coffeeRepository.saveAll(coffees);
    }

    public Mono<Coffee> createCoffee(Coffee coffee) {
        return coffeeRepository.save(coffee);
    }

    public Mono<Coffee> getCoffeeById(Long id) {
        return coffeeRepository.findById(id);
    }

    public Mono<Coffee> updateCoffee(Coffee coffee) {
        return coffeeRepository.save(coffee);
    }

    public void deleteCoffeeById(Long id) {
        coffeeRepository.deleteById(id);
    }

    public Mono<Boolean> coffeeExistsById(Long id) {
        return coffeeRepository.existsById(id);
    }

    public void deleteAllCoffees() {
        coffeeRepository.deleteAll();
    }
}
