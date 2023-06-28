package com.lolitaflamme.coffeesupplier.controller;

import com.lolitaflamme.coffeesupplier.domain.Coffee;
import com.lolitaflamme.coffeesupplier.service.CoffeeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Controller
@RequiredArgsConstructor
@RequestMapping("/coffees")
public class CoffeeController {

    @NonNull
    private final CoffeeService coffeeService;

    //REST endpoints
    @ResponseBody
    @GetMapping
    public ResponseEntity<Flux<Coffee>> getCoffees() {
        return new ResponseEntity<>(coffeeService.getCoffees(), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<Mono<Coffee>> createCoffee(@RequestBody Coffee coffee) {
        return new ResponseEntity<>(coffeeService.createCoffee(coffee), HttpStatus.CREATED);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<Mono<Coffee>> getCoffeeById(@PathVariable Long id) {
        return new ResponseEntity<>(coffeeService.getCoffeeById(id), HttpStatus.OK);
    }

    @ResponseBody
    @PutMapping("/{id}")
    public ResponseEntity<Mono<Coffee>> updateCoffee(@PathVariable Long id,
                                                     @RequestBody Coffee coffee) {
        return coffeeService.coffeeExistsById(id).equals(true)
                ? new ResponseEntity<>(coffeeService.updateCoffee(coffee), HttpStatus.OK)
                : new ResponseEntity<>(coffeeService.createCoffee(coffee), HttpStatus.CREATED);
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<Coffee> deleteCoffee(@PathVariable Long id) {
        coffeeService.deleteCoffeeById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //RSocket endpoints (Request - Stream interaction model)
    @MessageMapping("coffeesStream")
    public Flux<Coffee> getCoffeesStream() {
        return coffeeService.getCoffees().concatWith(
                Flux.interval(Duration.ofSeconds(1)).flatMap(c -> coffeeService.getCoffees()));
    }

    @MessageMapping("createCoffeeStream")
    public Flux<Coffee> createCoffeeStream(@Payload Coffee coffee) {
        Mono<Coffee> createdCoffee = coffeeService.createCoffee(coffee);
        return createdCoffee.concatWith(
                Flux.interval(Duration.ofSeconds(1)).flatMap(c -> coffeeService.getCoffeeById(coffee.getId())));
    }

    @MessageMapping("coffeeByIdStream/{id}")
    public Flux<Coffee> getCoffeeByIdStream(@DestinationVariable("id") Long id) {
        return coffeeService.getCoffeeById(id).concatWith(
                Flux.interval(Duration.ofSeconds(1)).flatMap(c -> coffeeService.getCoffeeById(id)));
    }

    @MessageMapping("updateCoffeeStream/{id}")
    public Flux<Coffee> updateCoffeeStream(@DestinationVariable("id") Long id,
                                           @Payload Coffee coffee) {

        Mono<Coffee> updatedCoffee = coffeeService.getCoffeeById(id)
                .map(c -> {
                    c.setTitle(coffee.getTitle());
                    c.setBeans(coffee.getBeans());
                    c.setAroma(coffee.getAroma());
                    c.setRoast(coffee.getRoast());
                    c.setCaffeineContent(coffee.getCaffeineContent());
                    return c;
                })
                .flatMap(coffeeService::updateCoffee);

        return updatedCoffee.concatWith(
                Flux.interval(Duration.ofSeconds(1)).flatMap(c -> coffeeService.getCoffeeById(id)));
    }
}
