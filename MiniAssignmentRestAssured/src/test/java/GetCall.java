import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class GetCall {
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
