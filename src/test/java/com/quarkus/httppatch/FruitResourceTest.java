package com.quarkus.httppatch;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(FruitResource.class)
public class FruitResourceTest {

    @Test
    public void testJsonMergePatch() {
        Response response = given().get();
        Fruit loadedFruit = (Fruit) response.body().jsonPath().getList("", Fruit.class).get(0); // Only a single
        // fruit available
        Assertions.assertEquals(loadedFruit.name, "apple");
        // Do first way of patching via JSON Merge Patch
        String patch = "{\"name\":\"banana\"}"; // Change fruit from apple to banana
        response = given().pathParam("id", loadedFruit.id).contentType("application/merge-patch+json")
                .body(patch)
                .patch("/{id}");
        Fruit patchedFruit = (Fruit) response.body().as(Fruit.class);
        Assertions.assertEquals(loadedFruit.name, "banana"); // ! Fails !
    }

    @Test
    public void testJsonPatch() {
        Response response = given().get();
        Fruit loadedFruit = (Fruit) response.body().jsonPath().getList("", Fruit.class).get(0); // Only a single
        // fruit available
        Assertions.assertEquals(loadedFruit.name, "apple");
        // Try patching via JSON Patch
        String patch = "[{\"op\":\"replace\", \"path\":\"/name\", \"value\":\"banana\"}]"; // Change fruit from apple to banana
        response = given().pathParam("id", loadedFruit.id).contentType(MediaType.APPLICATION_JSON_PATCH_JSON)
                .body(patch)
                .patch("/{id}");
        Fruit patchedFruit = (Fruit) response.body().as(Fruit.class);
        Assertions.assertEquals(loadedFruit.name, "banana"); // ! Fails !
    }

}