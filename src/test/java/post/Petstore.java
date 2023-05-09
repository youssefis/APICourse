package post;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import pojo.PetPojo;
import utils.PayloadUtils;

public class Petstore {

    @Test
    public void createPetTest(){
        // Post: https://petstore.swagger.io/v2/pet
        // Get: https://petstore.swagger.io/v2/pet/123455

        /*
        1. created a pet
        2. validated post call response body and status code
        3. sent get request with newly created pet id
        4. validated get call response body and status code
         */

        int petId = 123455;
        String petName = "alex";
        String petStatus= "awake";


        RestAssured.baseURI = "https://petstore.swagger.io";
        RestAssured.basePath="v2/pet";
        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON) //bc it's post request
                .body(PayloadUtils.getPetPayload(petId,petName,petStatus)).when().post().then().statusCode(200).extract().response();
        PetPojo parsedResponse = response.as(PetPojo.class);
        Assert.assertEquals(petName,parsedResponse.getName());
        Assert.assertEquals(petId,parsedResponse.getId());
        Assert.assertEquals(petStatus,parsedResponse.getStatus());


        Response response1 = RestAssured.given().accept(ContentType.JSON)
                .when().get("https://petstore.swagger.io/v2/pet/"+petId)
                .then().statusCode(200).extract().response();

        PetPojo parsedGetResponse = response1.as(PetPojo.class);
        Assert.assertEquals(petId,parsedGetResponse.getId());
        Assert.assertEquals(petName,parsedResponse.getName());
        Assert.assertEquals(petStatus,parsedResponse.getStatus());


    }

}
