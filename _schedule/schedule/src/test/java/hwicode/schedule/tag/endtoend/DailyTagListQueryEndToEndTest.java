package hwicode.schedule.tag.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.tag.TagDataHelper;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagListRepository;
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
class DailyTagListQueryEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyTagListRepository dailyTagListRepository;

    @Autowired
    TokenProvider tokenProvider;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 계획표의_태그들_조회_요청() {
        //given
        Long userId = 1L;
        String accessToken = TagDataHelper.createAccessToken(tokenProvider, userId);

        LocalDate date = LocalDate.of(2023, 8, 24);

        RequestSpecification requestSpecification = given().port(port)
                .param("date", String.valueOf(date))
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken);

        //when
        Response response = requestSpecification.when()
                .get("/dailyschedule/daily-tag-lists");

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 계획표의_메모들_조회_요청() {
        //given
        Long userId = 1L;
        String accessToken = TagDataHelper.createAccessToken(tokenProvider, userId);

        LocalDate date = LocalDate.of(2023, 8, 24);

        DailyTagList dailyTagList = new DailyTagList(date, 1L);
        dailyTagListRepository.save(dailyTagList);

        RequestSpecification requestSpecification = given().port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken);

        //when
        Response response = requestSpecification.when()
                .get("/dailyschedule/daily-tag-lists/{dailyTagListId}/memos", dailyTagList.getId());

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

}
