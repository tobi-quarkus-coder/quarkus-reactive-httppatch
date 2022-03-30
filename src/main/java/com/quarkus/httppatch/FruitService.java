package com.quarkus.httppatch;

import io.smallrye.mutiny.Uni;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class FruitService {
    private Map<Long, Fruit> fruits = new HashMap<>();

    @PostConstruct
    private void init() {
        Fruit fruit = new Fruit();
        fruit.id = 1L;
        fruit.name = "apple";
        fruits.put(fruit.id, fruit);
    }

    public void updateFruit(Fruit fruit) {
        if (fruit.id != null && fruits.containsKey(fruit.id)) {
            fruits.put(fruit.id, fruit);
        }
    }

    public Uni<Map<Long, Fruit>> getFruits() {
        return Uni.createFrom().item(fruits);
    }
}
