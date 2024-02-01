package hwicode.schedule.tag.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.infra.jpa_repository.TagRepository;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static hwicode.schedule.tag.TagDataHelper.*;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TagQueryEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TagRepository tagRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 특정_태그를_가지고_있는_계획표들_조회_요청() {
        // given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, userId);
        tagRepository.save(tag);

        RequestSpecification requestSpecification = given().port(port)
                .queryParam("tagId", tag.getId())
                .queryParam("lastDailyTagListId", DAILY_TAG_LIST_ID);

        // when
        Response response = requestSpecification.when()
                .get("/search/daily-tag-lists");

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 특정_태그를_가지고_있는_메모들_조회_요청() {
        // given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, userId);
        tagRepository.save(tag);

        RequestSpecification requestSpecification = given().port(port)
                .queryParam("tagId", tag.getId())
                .queryParam("lastMemoId", MEMO_ID);

        // when
        Response response = requestSpecification.when()
                .get("/search/memos");

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 모든_태그_조회_요청() {
        // given
        RequestSpecification requestSpecification = given().port(port);

        // when
        Response response = requestSpecification.when()
                .get("/tags");

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 특정_키워드를_가진_태그_조회_요청() {
        // given
        String nameKeyword = "a";

        RequestSpecification requestSpecification = given().port(port)
                .queryParam("nameKeyword", nameKeyword);

        // when
        Response response = requestSpecification.when()
                .get("/search/tags");

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

}
