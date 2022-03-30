package com.quarkus.httppatch;

import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

@Path("/fruits")
public class FruitResource {

    @Inject
    FruitService fruitService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Fruit> getById(@PathParam("id") Long id) {
        return fruitService.getFruits().onItem()
                .transform(fruitMap -> fruitMap.get(id));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Collection<Fruit>> getAll() {
        return fruitService.getFruits().onItem()
                .transform(fruitMap -> fruitMap.values());
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON_PATCH_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Fruit> updatePartially(@PathParam("id") Long id, Fruit fruit) {
        // Update the fruit
        fruitService.updateFruit(fruit);
        // Return the patched fruit
        return Uni.createFrom().item(fruit);
    }

    @PATCH
    @Path("/{id}")
    @Consumes("application/merge-patch+json")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Fruit> merge(@PathParam("id") Long id, Fruit fruit) {
        // Update the fruit
        fruitService.updateFruit(fruit);
        // Return the patched fruit
        return Uni.createFrom().item(fruit);
    }


}