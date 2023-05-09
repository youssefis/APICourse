package get;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import pojo.PokeApiPojo;
import pojo.PokeApiResultPojo;

import java.util.List;

public class PokeApi {

    @Test
    public void pokeApiWithPojo() {
        RestAssured.baseURI="https://pokeapi.co/api/v2/pokemon";
        Response response = RestAssured.given().header("Accept", "application/json")
                .queryParam("limit","100")
                .when()
                .get()
                .then().statusCode(200).extract().response();

        PokeApiPojo deserializedResponse = response.as(PokeApiPojo.class);
        List<PokeApiResultPojo> results= deserializedResponse.getResults();

        Assert.assertEquals(100,results.size());
        for (PokeApiResultPojo pokemon:results){

            if (pokemon.getName().equalsIgnoreCase("pikachu")){
                System.out.println(pokemon.getUrl());
            }

        }

        //        int actualCount=deserializedResponse.getCount();
//        int expectedCount=1279;
//        Assert.assertEquals(expectedCount,actualCount);


    }

}
