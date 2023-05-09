package get;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReqRes {

    //1. Defined/determined the endpoint
    //2. Added query string params as needed
    //3.Defined HTTP Method
    //4.Send
    //5.Validate status code

    @Test
    public void ReqRes(){
        Response response= RestAssured
                .given().header("Accept","application/json")
                .when().get("https://reqres.in/api/users?page=2")
                .then().statusCode(200).extract().response();

        Map<String, Object> deserializationResponse=response.as(new TypeRef<Map<String, Object>>() {
        });
        System.out.println(deserializationResponse.get("per_page"));
        System.out.println(deserializationResponse.get("total_pages"));
        System.out.println(deserializationResponse.get("support"));

        List<Map<String,Object>> dataList= (List<Map<String, Object>>) deserializationResponse.get("data");
        System.out.println(dataList);

        for (int i=0; i<dataList.size();i++){
            System.out.println(dataList.get(i).get("email"));
        }

    }

}
