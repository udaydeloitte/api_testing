import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
public class Solution1 {
    @Test
    public void get_call(){
        given().
                baseUri("https://jsonplaceholder.typicode.com/posts").header("Content-Type","application/json").
                when().
                get("https://jsonplaceholder.typicode.com/posts").
                then().
                 statusCode(200).body("userId[39]",equalTo(4)).body("title[39]",equalTo("enim quo cumque"));
    }
}
