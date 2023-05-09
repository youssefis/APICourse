package get;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pojo.CompetitionsPojo;
import pojo.FootballPojo;

import java.util.List;

public class Football {

    @Before
    public void setup() {

        RestAssured.baseURI = "http://api.football-data.org/";
        RestAssured.basePath = "v2/competitions";
    }

    @Test
    public void competitionsTest() {

        RestAssured.given().accept(ContentType.JSON)
                .when().get()
                .then().statusCode(200)
                .contentType(ContentType.JSON)
                .body("count", Matchers.equalTo(168)) //assertion from Matchers library
                .and()
                .body("competitions[0].name", Matchers.equalTo("WC Qualification CAF"));

    }

    @Test
    public void competitionSearchTest() {

        // GET http://api.football-data.org/v2/competitions
        // Parse the response
        // Search for MLS competition
        // validation that MLS competition id=2145

        Response response = RestAssured.given().accept(ContentType.JSON)
                .when().get()
                .then().statusCode(200).extract().response();

        FootballPojo parsedResponse = response.as(FootballPojo.class);
        List<CompetitionsPojo> competitions = parsedResponse.getCompetitions();
        for (CompetitionsPojo competition : competitions) {


            if (competition.getName().equals("MLS")) {
                Assert.assertEquals(2145, competition.getId());
            }
        }


    }

    @Test
    public void AdvancedRESTAssuredTest(){

        // Advanced REST Assured way
        Response response = RestAssured.given().accept(ContentType.JSON)
                .when().get()
                .then().statusCode(200)
                .body("competitions.find { it.name == 'MLS' }.id", Matchers.equalTo(2145))
                .extract().response();

    }

    @Test
    public void AdvancedRESTAssuredTest2(){

        Response response = RestAssured.given().accept(ContentType.JSON)
                .when().get()
                .then().statusCode(200)
                .body("competitions.collect{it.name}",Matchers.containsInRelativeOrder("Supercopa Argentina"))
                .extract().response();

        List<String> result=response.path("competitions.collect{it.name}");
        System.out.println(result);

        // get all country names where competition id is greater than 2006
        List<String> countryList=response.path("competitions.findAll{it.id>2006}.area.name");
        System.out.println(countryList.size());
        System.out.println(countryList);

        //sum all id values for all competitions
        int sum=response.path("competitions.collect { it.id }.sum()");
        System.out.println("Sum of all ids is: "+sum);





    }


}