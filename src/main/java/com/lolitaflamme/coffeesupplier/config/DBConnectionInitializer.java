package com.lolitaflamme.coffeesupplier.config;

import com.lolitaflamme.coffeesupplier.domain.Coffee;
import com.lolitaflamme.coffeesupplier.service.CoffeeService;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import java.math.BigDecimal;
import java.util.List;

import static com.lolitaflamme.coffeesupplier.domain.Beans.ARABICA;
import static com.lolitaflamme.coffeesupplier.domain.Beans.ROBUSTA;
import static com.lolitaflamme.coffeesupplier.domain.Roast.DARK_ROAST;
import static com.lolitaflamme.coffeesupplier.domain.Roast.MEDIUM_ROAST;

@Configuration
public class DBConnectionInitializer {

    @Bean
    public ConnectionFactoryInitializer initializer(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        return initializer;
    }

    @Bean
    public CommandLineRunner init(CoffeeService coffeeService) {
        return args -> {
            coffeeService.createCoffees(List.of(
                            new Coffee("Tanzania Gombe", ARABICA, "citrus, walnuts and a hint of honey",
                                    MEDIUM_ROAST, new BigDecimal("7.90"), 1.5),
                            new Coffee("Colombia Pink Bourbon", ARABICA, "fruits", MEDIUM_ROAST,
                                    new BigDecimal(9.40), 1.5),
                            new Coffee("Espresso India Parchment Robusta", ROBUSTA,
                                    "dark chocolate, tobacco and cinnamon", DARK_ROAST, new BigDecimal(6.90), 2.7)))
                    .thenMany(coffeeService.getCoffees())
                    .subscribe(System.out::println);
        };
    }
}
