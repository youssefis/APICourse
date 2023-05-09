package post;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Test;
import pojo.PetPojo;

import java.io.File;
import java.io.IOException;

public class Serialization {

    @Test
    public void serializationTest() throws IOException {

        PetPojo pet=new PetPojo();
        pet.setName("Hutch");
        pet.setStatus("Serving");
        pet.setId(781337);

        File jasonFile=new File("src/test/resources/pet.json");
        ObjectMapper objectMapper= new ObjectMapper();
        objectMapper.writeValue(jasonFile,pet);

    }

    @Test
    public void serializationTest2(){

        RestAssured.baseURI="https://petstore.swagger.io";
        RestAssured.basePath="v2/pet";

        File jsonFile = new File("src/test/resources/pet.json");
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(jsonFile)                                                           // serialization using a file
                .when().post()
                .then().statusCode(200)
                .and()
                .body("name", Matchers.is("Hutch"))
                .body("status", Matchers.equalTo("Serving")).extract().response();

    }

    @Test
    public void serializationTest3(){

        RestAssured.baseURI="https://petstore.swagger.io";
        RestAssured.basePath="v2/pet";

        PetPojo pet = new PetPojo();
        pet.setId(1244);
        pet.setName("Zeus");
        pet.setStatus("playing");

        RestAssured.given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(pet)                                                           // serialization using java object
                .when().post()
                .then().statusCode(200)
                .body("name",Matchers.is("Zeus"))
                .and()
                .body("status",Matchers.is("playing")).extract().response();


    }












}
