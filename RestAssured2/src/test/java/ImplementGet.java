import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ImplementGet {

    RequestSpecification reqspec;
    ResponseSpecification reponspec;
    @BeforeTest
    public void setup(){
        RequestSpecBuilder reqspecbuild=new RequestSpecBuilder();
        reqspecbuild.setBaseUri("https://jsonplaceholder.typicode.com/posts").
                addHeader("Content-Type","application/json");
        reqspec= RestAssured.with().spec(reqspecbuild.build());
        reponspec=RestAssured.expect().contentType(ContentType.JSON);

    }
    @Test
    public void GetCall(){
        given().spec(reqspec).
                when().get().
                then().spec(reponspec).statusCode(200).body("userId[39]",equalTo(4)).body("title[39]",equalTo("enim quo cumque"));
    }

}
