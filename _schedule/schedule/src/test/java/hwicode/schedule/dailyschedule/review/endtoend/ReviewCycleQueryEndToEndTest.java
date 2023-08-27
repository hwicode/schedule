package hwicode.schedule.dailyschedule.review.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewCycleQueryEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 모든_복습_주기_조회_요청() {
        // given
        RequestSpecification requestSpecification = given().port(port);

        //when
        Response response = requestSpecification.when()
                .get("/dailyschedule/review-cycles");

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

}
