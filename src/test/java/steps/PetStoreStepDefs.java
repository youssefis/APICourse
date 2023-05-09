package steps;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Assert;
import pojo.PetPojo;

import java.util.ArrayList;
import java.util.List;

public class PetStoreStepDefs {
    Response response;
    @Given("user has petStore endpoint")
    public void user_has_pet_store_endpoint() {

        RestAssured.baseURI="https://petstore.swagger.io";
        RestAssured.basePath="v2/pet";

    }
    @When("user sends GET request to list pets")
    public void user_sends_get_request_to_list_pets() {

        response = RestAssured.given().accept(ContentType.JSON)
                .queryParam("status","sold")
                .when().get("findByStatus");

    }
    @Then("status code is {int}")
    public void status_code_is(int expectedStatusCode) {
        response.prettyPrint(); //  Prints response body
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(expectedStatusCode,actualStatusCode);
    }
    @Then("response contains list of pets")
    public void response_contains_list_of_pets() {
        List<PetPojo> petPojoList=new ArrayList<>();
        petPojoList = response.as(petPojoList.getClass());
        Assert.assertTrue(petPojoList.size()>0);

    }

    @When("^user sends GET request to list (non-existing|invalid) pet by id$")
    public void user_sends_get_request_to_list_non_existing_pet_by_id(String petIdType) {
        String petId;
        if (petIdType.equalsIgnoreCase("non-existing")){
            petId="30901333";
        }else {
            petId="3090333f";
        }

        response=RestAssured.given().accept(ContentType.JSON)
                .when().get("30901333");
    }
    @Then("error message is {string}")
    public void error_message_is(String errorMsg) {

        response.then().body("message", Matchers.is(errorMsg));

    }

}
