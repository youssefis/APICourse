package post;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import pojo.SlackPojo;
import utils.PayloadUtils;

public class Slack {

    //Deserialize response
    //validate "ok" field
    //validate message.text field

    @Test
    public void sendSlackMessageTest(){

        RestAssured.baseURI="https://slack.com";
        RestAssured.basePath="api/chat.postMessage";
        String msg="Youssef, Hello channel, from Java code";

        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization",
                        "Bearer xoxb-4349924244708-5140059154064-33cWt1KR58QdZE0L0EwMs5O3")
                .body(PayloadUtils.getSlackMessagePayload(msg))
                .when().post()
                .then().statusCode(200).extract().response();

        SlackPojo deserializedResponse = response.as(SlackPojo.class);

//        System.out.println(deserializedResponse.getOk());
//        System.out.println(deserializedResponse.getMessage().getText());

        boolean isOk=true;
        String actualMsg=deserializedResponse.getMessage().getText();
        Assert.assertTrue("Failed to send Message to slack",isOk);
        Assert.assertEquals("Failed to validate slack Message",msg,actualMsg);

        JsonPath jsonPath= response.jsonPath();




    }

}
