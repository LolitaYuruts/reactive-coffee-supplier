package com.lolitaflamme.coffeesupplier.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coffee {
    @Id
    private Long id;
    private String title;
    private Beans beans;
    private String aroma;
    private Roast roast;
    private BigDecimal price;
    @JsonProperty("caffeine_content")
    private double caffeineContent;

    public Coffee(String title, Beans beans, String aroma, Roast roast, BigDecimal price, double caffeineContent) {
        this(null, title, beans, aroma, roast, price, caffeineContent);
    }
}
