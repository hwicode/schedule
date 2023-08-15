package hwicode.schedule.tag.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.tag.application.MemoService;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.domain.Memo;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagListRepository;
import hwicode.schedule.tag.infra.jpa_repository.MemoRepository;
import hwicode.schedule.tag.infra.jpa_repository.MemoTagRepository;
import hwicode.schedule.tag.infra.jpa_repository.TagRepository;
import hwicode.schedule.tag.presentation.memo.dto.save.MemoSaveRequest;
import hwicode.schedule.tag.presentation.memo.dto.save_with_tags.MemoSaveWithTagsRequest;
import hwicode.schedule.tag.presentation.memo.dto.tags_add.MemoTagsAddRequest;
import hwicode.schedule.tag.presentation.memo.dto.text_modify.MemoTextModifyRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static hwicode.schedule.tag.TagDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemoEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    MemoService memoService;

    @Autowired
    DailyTagListRepository dailyTagListRepository;

    @Autowired
    MemoRepository memoRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    MemoTagRepository memoTagRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 메모_생성_요청() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);

        MemoSaveRequest memoSaveRequest = new MemoSaveRequest(dailyTagList.getId(), MEMO_TEXT);

        RequestSpecification requestSpecification = given().port(port)
                .contentType(ContentType.JSON)
                .body(memoSaveRequest);

        // when
        Response response = requestSpecification.when()
                .post("/dailyschedule/memos");

        // then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        assertThat(memoRepository.findAll()).hasSize(1);
    }

    @Test
    void 메모의_내용_변경_요청() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);

        Long memoId = memoService.saveMemo(dailyTagList.getId(), MEMO_TEXT);

        MemoTextModifyRequest memoTextModifyRequest = new MemoTextModifyRequest(NEW_MEMO_TEXT);

        RequestSpecification requestSpecification = given().port(port)
                .contentType(ContentType.JSON)
                .body(memoTextModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch("/dailyschedule/memos/{memoId}", memoId);

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        Memo savedMemo = memoRepository.findById(memoId).orElseThrow();
        assertThat(savedMemo.changeText(NEW_MEMO_TEXT)).isFalse();
    }

    @Test
    void 메모에_여러_개의_태그_추가_요청() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);

        List<Tag> tags = List.of(
                new Tag(TAG_NAME), new Tag(TAG_NAME2), new Tag(TAG_NAME3)
        );
        tagRepository.saveAll(tags);

        Set<Long> tagIds = tags.stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());

        Long memoId = memoService.saveMemo(dailyTagList.getId(), MEMO_TEXT);

        MemoTagsAddRequest memoTagsAddRequest = new MemoTagsAddRequest(tagIds);

        RequestSpecification requestSpecification = given().port(port)
                .contentType(ContentType.JSON)
                .body(memoTagsAddRequest);

        // when
        Response response = requestSpecification.when()
                .post("/dailyschedule/memos/{memoId}/tags", memoId);

        // then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        assertThat(memoTagRepository.findAll()).hasSize(tags.size());
    }

    @Test
    void 메모를_생성하며_여러_개의_태그_추가_요청() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);

        List<Tag> tags = List.of(
                new Tag(TAG_NAME), new Tag(TAG_NAME2), new Tag(TAG_NAME3)
        );
        tagRepository.saveAll(tags);

        Set<Long> tagIds = tags.stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());

        MemoSaveWithTagsRequest memoSaveWithTagsRequest = new MemoSaveWithTagsRequest(dailyTagList.getId(), MEMO_TEXT, tagIds);

        RequestSpecification requestSpecification = given().port(port)
                .contentType(ContentType.JSON)
                .body(memoSaveWithTagsRequest);

        // when
        Response response = requestSpecification.when()
                .post("/dailyschedule/memos/tags");

        // then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        assertThat(memoRepository.findAll()).hasSize(1);
        assertThat(memoTagRepository.findAll()).hasSize(tags.size());
    }

    @Test
    void 메모에_태그_삭제_요청() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);

        List<Tag> tags = List.of(
                new Tag(TAG_NAME), new Tag(TAG_NAME2), new Tag(TAG_NAME3)
        );
        List<Long> tagIds = tagRepository.saveAll(tags)
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        Long memoId = memoService.saveMemoWithTags(dailyTagList.getId(), MEMO_TEXT, tagIds);

        RequestSpecification requestSpecification = given().port(port);

        // when
        Long deletedTargetTagId = tags.get(0).getId();
        Response response = requestSpecification.when()
                .delete("/dailyschedule/memos/{memoId}/tags/{tagId}", memoId, deletedTargetTagId);

        // then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        int numberOfMemoTags = tags.size() - 1;
        assertThat(memoTagRepository.findAll()).hasSize(numberOfMemoTags);
        assertThat(memoRepository.existsById(memoId)).isTrue();
    }

    @Test
    void 메모_삭제_요청() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);

        List<Tag> tags = List.of(
                new Tag(TAG_NAME), new Tag(TAG_NAME2), new Tag(TAG_NAME3)
        );
        List<Long> tagIds = tagRepository.saveAll(tags)
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        Long memoId = memoService.saveMemoWithTags(dailyTagList.getId(), MEMO_TEXT, tagIds);

        RequestSpecification requestSpecification = given().port(port);

        // when
        Response response = requestSpecification.when()
                .delete("/dailyschedule/memos/{memoId}", memoId);

        // then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(memoRepository.existsById(memoId)).isFalse();
        assertThat(memoTagRepository.findAll()).isEmpty();
    }

}
