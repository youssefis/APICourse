package get;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import pojo.SwResultPojo;
import pojo.StarWarsPojo;

import java.util.List;
import java.util.Map;

public class StarWars {

    // 1- Defined/determined the endpoint
    // 2- Added query string params as needed
    // 3- Defined HTTP Method
    // 4- Send
    // 5- Validate status code

    @Test
    public void getSWChars(){

        //https://swapi.dev/api/people
        RestAssured.given()
                .when().get("https://swapi.dev/api/people")
                .then().statusCode(200).log().body();
    }

    @Test
    public void getSWCharsDeserialized(){
        Response response= RestAssured.given().header("Accept","application/json")
                .when().get("https://swapi.dev/api/people")
                .then().statusCode(200).extract().response();

        Map<String, Object> deserializedResponse=response.as(
                new TypeRef<Map<String, Object>>() {});

        int count= (int) deserializedResponse.get("count");
        Assert.assertEquals(82,count);

        //Array of Json objects [{},{},{},{}]
        List<Map<String, Object>> results= (List<Map<String, Object>>) deserializedResponse.get("results");


    }

    //Validate that SW API Count value is correct, we have total of 82 character
    @Test
    public void resultsCountsValidation(){

        String originalPageLink="https://swapi.dev/api/people/?page=";
        String dynamicPageLink="https://swapi.dev/api/people/?page=";
        int ActualSWApiCount=0;

        for (int i=1;i<=9;i++){
            dynamicPageLink=dynamicPageLink.concat(String.valueOf(i));

            Response response1= RestAssured.given().header("Accept","application/json")
                    .when().get(dynamicPageLink)
                    .then().statusCode(200).extract().response();

            Map<String,Object> deserializedResponse1=response1.as(new TypeRef<Map<String, Object>>() {
            });

            List<Map<String, Object>> results= (List<Map<String, Object>>) deserializedResponse1.get("results");

            ActualSWApiCount=ActualSWApiCount+results.size();

            dynamicPageLink=originalPageLink;
        }

        Assert.assertEquals(82,ActualSWApiCount);
    }

    //-get list of all SW characters name
    @Test
    public void allCharactersName(){
        String originalPageLink="https://swapi.dev/api/people/?page=";
        String dynamicPageLink="https://swapi.dev/api/people/?page=";
        for (int i=1;i<=9;i++) {
            dynamicPageLink=dynamicPageLink.concat(String.valueOf(i));
            Response response = RestAssured.given().header("Accept", "application/json")
                    .when().get(dynamicPageLink)
                    .then().statusCode(200).extract().response();

            Map<String, Object> deserializedResponse = response.as(new TypeRef<Map<String, Object>>() {
            });

            List<Map<String, Object>> result = (List<Map<String, Object>>) deserializedResponse.get("results");
            System.out.println("page "+i+"/");
            for (Map<String, Object> character : result) {

                System.out.println(character.get("name"));

            }
            dynamicPageLink=originalPageLink;
        }

    }
    //-get list all female characters name
    @Test
    public void allFemaleCharacters(){

        String originalPageLink="https://swapi.dev/api/people/?page=";
        String dynamicPageLink="https://swapi.dev/api/people/?page=";
        for (int i=1;i<=9;i++) {
            dynamicPageLink=dynamicPageLink.concat(String.valueOf(i));
            Response response = RestAssured.given().header("Accept", "application/json")
                    .when().get(dynamicPageLink)
                    .then().statusCode(200).extract().response();

            Map<String, Object> deserializedResponse = response.as(new TypeRef<Map<String, Object>>() {
            });

            List<Map<String, Object>> result = (List<Map<String, Object>>) deserializedResponse.get("results");
            System.out.println("page "+i+"/");
            for (Map<String, Object> character : result) {
                if (character.get("gender").equals("female")) {
                    System.out.println(character.get("name"));
                }
            }
            dynamicPageLink=originalPageLink;
        }


    }

    @Test
    public void swApiGetWithPojo(){

        Response response = RestAssured.given().header("Accept", "application/json")
                .when().get("https://swapi.dev/api/people")
                .then().statusCode(200).extract().response();

        StarWarsPojo deserializedResponse  = response.as(StarWarsPojo.class);
        int actualCount= deserializedResponse.getCount();
        int expectedCount =82;
        Assert.assertEquals(expectedCount,actualCount);

        List<SwResultPojo> results = deserializedResponse.getResults();

        for (SwResultPojo result:results){

            System.out.println(result.getName());

        }

    }

    @Test
    public void starWarsTestTemirlan(){
        //https://swapi.dev/api/people

        RestAssured.baseURI="https://swapi.dev/";
        RestAssured.basePath="api/people";
        Response response = RestAssured.given().accept(ContentType.JSON).log().all() //will print out request
                .when().get()
                .then().statusCode(200).log().body() //prints only response;
                .extract().response();

        StarWarsPojo parsedResponse= response.as(StarWarsPojo.class);
        int actualTotalCharactersCount = parsedResponse.getResults().size();
        String nextUrl=parsedResponse.getNext(); //gets value of "next" field from Json response
        while (nextUrl!=null){

            //1. Make a Get request to nextUrl
            //2. Count characters from next page
            //3. Get next page URL

            //1. Make a Get request to nextUrl
            response= RestAssured.given().accept(ContentType.JSON)
                    .when().get(nextUrl)
                    .then().statusCode(200)
                    .contentType(ContentType.JSON) //validating response format is json
                    .extract().response();
            parsedResponse =response.as(StarWarsPojo.class);

            //2. Count characters from next page
            actualTotalCharactersCount+=parsedResponse.getResults().size();

            //3. Get next page URL
            nextUrl=parsedResponse.getNext();
        }
        //validating count equals to total number of characters
    Assert.assertEquals(parsedResponse.getCount(),actualTotalCharactersCount);

    }




}
