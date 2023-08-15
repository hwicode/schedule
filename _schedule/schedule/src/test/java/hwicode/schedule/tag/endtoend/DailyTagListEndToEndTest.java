package hwicode.schedule.tag.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.tag.application.DailyTagListService;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.exception.domain.dailytaglist.DailyTagNotFoundException;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagListRepository;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagRepository;
import hwicode.schedule.tag.infra.jpa_repository.TagRepository;
import hwicode.schedule.tag.presentation.dailytaglist.dto.tag_add.DailyTagListTagAddRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static hwicode.schedule.tag.TagDataHelper.TAG_NAME;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DailyTagListEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DailyTagListService dailyTagListService;

    @Autowired
    DailyTagListRepository dailyTagListRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    DailyTagRepository dailyTagRepository;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 오늘의_태그_리스트에_태그_추가_요청() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Tag tag = new Tag(TAG_NAME);

        dailyTagListRepository.save(dailyTagList);
        tagRepository.save(tag);

        DailyTagListTagAddRequest dailyTagListTagAddRequest = new DailyTagListTagAddRequest(tag.getId());

        RequestSpecification requestSpecification = given().port(port)
                .contentType(ContentType.JSON)
                .body(dailyTagListTagAddRequest);

        Long dailyTagListId = dailyTagList.getId();

        // when
        Response response = requestSpecification.when()
                .post("/dailyschedule/daily-tag-lists/{dailyTagListId}/tags", dailyTagListId);

        // then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        assertThat(dailyTagRepository.existsById(dailyTagListId)).isTrue();
    }

    @Test
    void 오늘의_태그_리스트에_태그_삭제_요청() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Tag tag = new Tag(TAG_NAME);

        dailyTagListRepository.save(dailyTagList);
        tagRepository.save(tag);

        Long dailyTagListId = dailyTagList.getId();
        Long tagId = tag.getId();
        dailyTagListService.addTagToDailyTagList(dailyTagListId, tagId);

        RequestSpecification requestSpecification = given().port(port);

        // when
        Response response = requestSpecification.when()
                .delete("/dailyschedule/daily-tag-lists/{dailyTagListId}/tags/{tagId}", dailyTagListId, tagId);

        // then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThatThrownBy(() -> dailyTagListService.deleteTagToDailyTagList(dailyTagListId, tagId))
                .isInstanceOf(DailyTagNotFoundException.class);
        assertThat(dailyTagRepository.findAll()).isEmpty();
    }

}
