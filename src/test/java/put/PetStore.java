package put;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Test;
import pojo.PetPojo;
import utils.PayloadUtils;

public class PetStore {

    /*

    1. Post call to create a pet
    2. Deserialize and validate post response
    3. PUT call to update an existing pet
    4. Deserialize and validate PUT response
    5. GET call to search for our pet
    6. Deserialize and validate GET Response

     */

    @Test
    public void updatePetTest(){
        RestAssured.baseURI="https://petstore.swagger.io";
        RestAssured.basePath="v2/pet";
        int petId=796899;
        String petName="Zeus";
        String petStatus="playing";

        RequestSpecification recSpec = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);


        RestAssured.given().accept(ContentType.JSON);
        //1. Post call to create a pet

        Response response = recSpec
                .body(PayloadUtils.getPetPayload(petId, petName, petStatus))
                .when().post()
                .then().statusCode(200).extract().response();

        //2. Deserialize and validate post response

        PetPojo parsedResponse = response.as(PetPojo.class);
        Assert.assertEquals(petId,parsedResponse.getId());
        Assert.assertEquals(petStatus,parsedResponse.getStatus());

        Assert.assertEquals(petName,parsedResponse.getName());
        //3. PUT call to update an existing pet

        String newStatus= "sleeping";
        response =recSpec
                .body(PayloadUtils.getPetPayload(petId,petName,newStatus))
                .when().put()
                .then().statusCode(200).extract().response();

        //4. Deserialize and validate PUT response

        parsedResponse=response.as((PetPojo.class));
        Assert.assertEquals(petName,parsedResponse.getName());
        Assert.assertEquals(petId,parsedResponse.getId());
        Assert.assertEquals(newStatus,parsedResponse.getStatus());
        //POST URL https://petstore.swagger.io/v2/pet
        //GET  URL https://petstore.swagger.io/v2/pet/{petId}

        //5. GET call to search for our pet
        RestAssured.given().accept(ContentType.JSON)
                .when().get(String.valueOf(petId))
                .then().statusCode(200).extract().response();

        //6. Deserialize and validate GET Response
        parsedResponse=response.as(PetPojo.class);
        Assert.assertEquals(petName,parsedResponse.getName());
        Assert.assertEquals(petId,parsedResponse.getId());
        Assert.assertEquals(newStatus,parsedResponse.getStatus());




    }



}
