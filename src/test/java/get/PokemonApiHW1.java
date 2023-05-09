package get;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import pojo.PokeApiPojo;
import pojo.PokeApiResultPojo;

import java.util.List;

public class PokemonApiHW1 {
    /*
    1. GET https://pokeapi.co/api/v2/pokemon
    2. Validate you got 20 Pokemons
    3. Get every Pokemons ability and store those in Map<String, List<String>>
     */

    @Test
    public void pokeNumberOfPokes(){
        Response response = RestAssured.given().header("Accept", "Application/json").log().all()
                .when()
                .get("https://pokeapi.co/api/v2/pokemon")
                .then().log().body()
                .statusCode(200).extract().response();

        PokeApiPojo deserializedResponse = response.as(PokeApiPojo.class);
        List<PokeApiResultPojo> results=deserializedResponse.getResults();




        Assert.assertEquals(20,results.size());

    }

    @Test
    public void pokesAbilities(){



    }




}
