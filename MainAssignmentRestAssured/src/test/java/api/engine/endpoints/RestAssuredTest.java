package api.engine.endpoints;
import com.relevantcodes.extentreports.ExtentReports;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
@Listeners(ExtentReport.class)
public class RestAssuredTest {
    static Logger log = Logger.getLogger(String.valueOf(RestAssuredTest.class));
    //private String username;
    public String username;
    public String email;
    public String password;
    public static String tokenGenerated;
    private double age;
    @BeforeMethod
    public void registerUser() throws IOException {
        File file = new File("C:\\Users\\udayprsingh\\IdeaProjects\\restdata.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = wb.getSheet("restdata");
        XSSFRow row1 = sheet.getRow(1);
        XSSFCell cell1 = row1.getCell(0);
        XSSFCell cell2 = row1.getCell(1);
        XSSFCell cell3 = row1.getCell(2);
        XSSFCell cell4 = row1.getCell(3);
        username = cell1.getStringCellValue();
        System.out.println(username);
        email = cell2.getStringCellValue();
        System.out.println(email);
        password = cell3.getStringCellValue();
        System.out.println(password);
        age=cell4.getNumericCellValue();
        System.out.println(age);
    }

    @Test(priority = 1)
    public void RegisterUser() throws IOException {
        RestAssured.baseURI = "https://api-nodejs-todolist.herokuapp.com";
        RequestSpecification request = RestAssured.given();
        String payload = "{\n" +
                "  \"name\" : \""+username+"\",\n" +
                "  \"email\" : \""+email+"\",\n" +
                "  \"password\" : \""+password+"\"\n" +
                "}";
        request.header("Content-Type", "application/json");
        Response responsefromGeneratedToken = request.body(payload).post("/user/register");
        responsefromGeneratedToken.prettyPrint();
        String jsonString = responsefromGeneratedToken.getBody().asString();
        tokenGenerated = JsonPath.from(jsonString).get("token");
        request.header("Authorization", "Bearer" + tokenGenerated)
                .header("Content-Type", "application/json");
        String loginDetails = "{\n" +
                "  \"email\" : \""+email+"\",\n" +
                "  \"password\" : \""+password+"\"\n" +
                "}";
        Response responseLogin = request.body(loginDetails).post("/user/login");
        responseLogin.prettyPrint();
        Assert.assertEquals(responsefromGeneratedToken.statusCode(),201);
        log.info("User Registered Successfully");
    }

    @Test(priority = 2)
    public void addTask() throws IOException {
        RestAssured.baseURI = "https://api-nodejs-todolist.herokuapp.com/task";
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + tokenGenerated)
                .header("Content-Type", "application/json");
        FileInputStream inputStream = new FileInputStream("C:\\Users\\udayprsingh\\IdeaProjects\\MainAssignmentRestAssured\\src\\test\\testdata\\taskdata.xlsx");
        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = wb.getSheet("taskdata");
        int rows = sheet.getPhysicalNumberOfRows();
        int cols = sheet.getRow(0).getLastCellNum();
        String description = null;
        String task = null;
        for (int i = 1; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (j == 0) {
                    description = sheet.getRow(i).getCell(j).getStringCellValue();
                }
                try{if(j == 1)
                    task = sheet.getRow(i).getCell(j).getStringCellValue();
                }
                catch(Exception e){
                    System.out.println("Discription can not be empty");
                    continue;
                }

            }

            String addTaskJson = "{\n" +
                    "\t\""+description+"\": \""+task+"\"\n" +
                    "}";
            Response responseaddTask = request.body(addTaskJson).post();
            responseaddTask.prettyPrint();
            String jsonString1 = responseaddTask.getBody().asString();
            String task1 = JsonPath.from(jsonString1).get("data.description");
            System.out.println(task1);
            Assert.assertEquals(task1,task);
            Assert.assertEquals(responseaddTask.statusCode(),201);
            wb.close();
            inputStream.close();
        }
        log.info("task added and validated successfully");
    }
    @Test(priority = 3)
    public void LoginUser(){
        RestAssured.baseURI = "https://api-nodejs-todolist.herokuapp.com/user/me";
        RequestSpecification request = RestAssured.given();
        request.header("Authorization","Bearer "+ tokenGenerated)
                .header("Content-Type","application/json");
        Response responsevalidateUser = request.get();
        responsevalidateUser.prettyPrint();
        Assert.assertEquals(responsevalidateUser.statusCode(),200);
    }
    @Test(priority = 4)
    public void getTask(){
        RestAssured.baseURI = "https://api-nodejs-todolist.herokuapp.com/task";
        RequestSpecification request = RestAssured.given();
        request.header("Authorization","Bearer "+ tokenGenerated)
                .header("Content-Type","application/json");
        Response responsegetTask = request.get();
        responsegetTask.prettyPrint();
        Assert.assertEquals(responsegetTask.statusCode(),200);

    }
    @Test(priority = 5)
    public void paginationFor2(){
        RestAssured.baseURI = "https://api-nodejs-todolist.herokuapp.com";
        RequestSpecification request2 = RestAssured.given();
        request2.header("Authorization","Bearer "+ tokenGenerated)
                .header("Content-Type","application/json");
        Response response2 = request2.get("/task?limit=2");
        response2.prettyPrint();
        Assert.assertEquals(response2.statusCode(),200);
    }
    @Test(priority = 6)
    public void paginationFor5(){
        RestAssured.baseURI = "https://api-nodejs-todolist.herokuapp.com";
        RequestSpecification request5 = RestAssured.given();
        request5.header("Authorization","Bearer "+ tokenGenerated)
                .header("Content-Type","application/json");
        Response response5 = request5.get("/task?limit=5");
        response5.prettyPrint();
        Assert.assertEquals(response5.statusCode(),200);
    }
    @Test(priority = 7)
    public void paginationFor10(){
        RestAssured.baseURI = "https://api-nodejs-todolist.herokuapp.com";
        RequestSpecification request10 = RestAssured.given();
        request10.header("Authorization","Bearer "+ tokenGenerated)
                .header("Content-Type","application/json");
        Response response10 = request10.get("/task?limit=10");
        response10.prettyPrint();
        Assert.assertEquals(response10.statusCode(),200);
    }
    @Test(priority = 8)
    public void RegisterWithSameDetailTest() throws IOException {
        RestAssured.baseURI = "https://api-nodejs-todolist.herokuapp.com";
        RequestSpecification request = RestAssured.given();
        String payload = "{\n" +
                "  \"name\" : \""+username+"\",\n" +
                "  \"email\" : \""+email+"\",\n" +
                "  \"password\" : \""+password+"\"\n" +
                "}";
        request.header("Content-Type", "application/json");
        Response responsefromGeneratedToken = request.body(payload).post("/user/register");
        responsefromGeneratedToken.prettyPrint();

        Assert.assertEquals(responsefromGeneratedToken.statusCode(),400);
        log.info("User is already registered: Duplicate entry");

    }
    @Test(priority = 9)
    public void loginNotRegisterdUser()
    {
        RestAssured.baseURI = "https://api-nodejs-todolist.herokuapp.com";
        RequestSpecification request = RestAssured.given();
        String loginDetails = "{\n" +
                "  \"email\" : \""+"mogits340@gmail.com"+"\",\n" +
                "  \"password\" : \""+"1234@123h"+"\"\n" +
                "}";
        Response responseLogin = request.body(loginDetails).post("/user/login");
        responseLogin.prettyPrint();
        String actual = responseLogin.getBody().asString();
        System.out.println(actual);
        String expected ="Unable to login";
        Assert.assertNotEquals(actual,expected);
        log.info("User is not registered");

    }


}
