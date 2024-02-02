package hwicode.schedule.timetable.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.timetable.TimeTableDataHelper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static hwicode.schedule.auth.AuthDataHelper.BEARER;
import static io.restassured.RestAssured.given;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TimeTableQueryEndToEndTest {

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
    void 학습_시간들_조회_요청() {
        //given
        Long userId = 1L;
        String accessToken = TimeTableDataHelper.createAccessToken(tokenProvider, userId);

        LocalDate date = LocalDate.of(2023, 8, 24);

        RequestSpecification requestSpecification = given().port(port)
                .param("date", String.valueOf(date))
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken);

        //when
        Response response = requestSpecification.when()
                .get("/dailyschedule/timetables");

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

}
