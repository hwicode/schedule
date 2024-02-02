package hwicode.schedule.dailyschedule.review.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.dailyschedule.review.ReviewDataHelper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static hwicode.schedule.auth.AuthDataHelper.BEARER;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewCycleQueryEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TokenProvider tokenProvider;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 모든_복습_주기_조회_요청() {
        // given
        Long userId = 1L;
        String accessToken = ReviewDataHelper.createAccessToken(tokenProvider, userId);

        RequestSpecification requestSpecification = given().port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken);

        //when
        Response response = requestSpecification.when()
                .get("/dailyschedule/review-cycles");

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

}
