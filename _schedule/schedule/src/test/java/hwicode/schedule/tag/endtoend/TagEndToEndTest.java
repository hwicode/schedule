package hwicode.schedule.tag.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.tag.TagDataHelper;
import hwicode.schedule.tag.application.TagService;
import hwicode.schedule.tag.application.dto.tag.TagSaveCommand;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.infra.jpa_repository.TagRepository;
import hwicode.schedule.tag.presentation.tag.dto.name_modify.TagNameModifyRequest;
import hwicode.schedule.tag.presentation.tag.dto.save.TagSaveRequest;
import io.restassured.http.ContentType;
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
import static hwicode.schedule.tag.TagDataHelper.NEW_TAG_NAME;
import static hwicode.schedule.tag.TagDataHelper.TAG_NAME;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TagEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    TagService tagService;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 태그_생성_요청() {
        // given
        Long userId = 1L;
        String accessToken = TagDataHelper.createAccessToken(tokenProvider, userId);

        TagSaveRequest tagSaveRequest = new TagSaveRequest(TAG_NAME);

        RequestSpecification requestSpecification = given().port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken)
                .contentType(ContentType.JSON)
                .body(tagSaveRequest);

        // when
        Response response = requestSpecification.when()
                .post("/dailyschedule/tags");

        // then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        assertThat(tagRepository.findAll()).hasSize(1);
    }

    @Test
    void 태그_이름_변경_요청() {
        // given
        Long userId = 1L;
        String accessToken = TagDataHelper.createAccessToken(tokenProvider, userId);

        TagSaveCommand command = new TagSaveCommand(userId, TAG_NAME);
        Long tagId = tagService.saveTag(command);

        TagNameModifyRequest tagNameModifyRequest = new TagNameModifyRequest(NEW_TAG_NAME);

        RequestSpecification requestSpecification = given().port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken)
                .contentType(ContentType.JSON)
                .body(tagNameModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch("/dailyschedule/tags/{tagId}", tagId);

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        Tag savedTag = tagRepository.findById(tagId).orElseThrow();
        assertThat(savedTag.changeName(NEW_TAG_NAME)).isFalse();
    }

    @Test
    void 태그_삭제_요청() {
        // given
        Long userId = 1L;
        String accessToken = TagDataHelper.createAccessToken(tokenProvider, userId);

        TagSaveCommand command = new TagSaveCommand(userId, TAG_NAME);
        Long tagId = tagService.saveTag(command);

        RequestSpecification requestSpecification = given().port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken);

        // when
        Response response = requestSpecification.when()
                .delete("/dailyschedule/tags/{tagId}", tagId);

        // then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(tagRepository.existsById(tagId)).isFalse();
    }

}
