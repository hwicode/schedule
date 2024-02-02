package hwicode.schedule.tag.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.tag.TagDataHelper;
import hwicode.schedule.tag.application.MemoService;
import hwicode.schedule.tag.application.dto.memo.MemoSaveWithTagsCommand;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static hwicode.schedule.auth.AuthDataHelper.BEARER;
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

    @Autowired
    TokenProvider tokenProvider;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 메모_생성_요청() {
        // given
        Long userId = 1L;
        String accessToken = TagDataHelper.createAccessToken(tokenProvider, userId);

        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        dailyTagListRepository.save(dailyTagList);

        MemoSaveRequest memoSaveRequest = new MemoSaveRequest(dailyTagList.getId(), MEMO_TEXT);

        RequestSpecification requestSpecification = given().port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken)
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
        Long userId = 1L;
        String accessToken = TagDataHelper.createAccessToken(tokenProvider, userId);

        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        Memo memo = dailyTagList.createMemo(MEMO_TEXT);

        dailyTagListRepository.save(dailyTagList);
        memoRepository.save(memo);

        MemoTextModifyRequest memoTextModifyRequest = new MemoTextModifyRequest(NEW_MEMO_TEXT);

        RequestSpecification requestSpecification = given().port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken)
                .contentType(ContentType.JSON)
                .body(memoTextModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch("/dailyschedule/memos/{memoId}", memo.getId());

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        Memo savedMemo = memoRepository.findById(memo.getId()).orElseThrow();
        assertThat(savedMemo.changeText(NEW_MEMO_TEXT)).isFalse();
    }

    @Test
    void 메모에_여러_개의_태그_추가_요청() {
        // given
        Long userId = 1L;
        String accessToken = TagDataHelper.createAccessToken(tokenProvider, userId);

        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        Memo memo = dailyTagList.createMemo(MEMO_TEXT);

        dailyTagListRepository.save(dailyTagList);
        memoRepository.save(memo);


        List<Tag> tags = List.of(
                new Tag(TAG_NAME, userId), new Tag(TAG_NAME2, userId), new Tag(TAG_NAME3, userId)
        );
        tagRepository.saveAll(tags);

        Set<Long> tagIds = tags.stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());

        MemoTagsAddRequest memoTagsAddRequest = new MemoTagsAddRequest(tagIds);

        RequestSpecification requestSpecification = given().port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken)
                .contentType(ContentType.JSON)
                .body(memoTagsAddRequest);

        // when
        Response response = requestSpecification.when()
                .post("/dailyschedule/memos/{memoId}/tags", memo.getId());

        // then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        assertThat(memoTagRepository.findAll()).hasSize(tags.size());
    }

    @Test
    void 메모를_생성하며_여러_개의_태그_추가_요청() {
        // given
        Long userId = 1L;
        String accessToken = TagDataHelper.createAccessToken(tokenProvider, userId);

        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        dailyTagListRepository.save(dailyTagList);

        List<Tag> tags = List.of(
                new Tag(TAG_NAME, userId), new Tag(TAG_NAME2, userId), new Tag(TAG_NAME3, userId)
        );
        tagRepository.saveAll(tags);

        Set<Long> tagIds = tags.stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());

        MemoSaveWithTagsRequest memoSaveWithTagsRequest = new MemoSaveWithTagsRequest(dailyTagList.getId(), MEMO_TEXT, tagIds);

        RequestSpecification requestSpecification = given().port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken)
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
        Long userId = 1L;
        String accessToken = TagDataHelper.createAccessToken(tokenProvider, userId);

        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        dailyTagListRepository.save(dailyTagList);

        List<Tag> tags = List.of(
                new Tag(TAG_NAME, userId), new Tag(TAG_NAME2, userId), new Tag(TAG_NAME3, userId)
        );
        List<Long> tagIds = tagRepository.saveAll(tags)
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        Long memoId = memoService.saveMemoWithTags(
                new MemoSaveWithTagsCommand(userId, dailyTagList.getId(), tagIds, MEMO_TEXT)
        );

        RequestSpecification requestSpecification = given().port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken);

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
        Long userId = 1L;
        String accessToken = TagDataHelper.createAccessToken(tokenProvider, userId);

        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        dailyTagListRepository.save(dailyTagList);

        List<Tag> tags = List.of(
                new Tag(TAG_NAME, userId), new Tag(TAG_NAME2, userId), new Tag(TAG_NAME3, userId)
        );
        List<Long> tagIds = tagRepository.saveAll(tags)
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        Long memoId = memoService.saveMemoWithTags(
                new MemoSaveWithTagsCommand(userId, dailyTagList.getId(), tagIds, MEMO_TEXT)
        );

        RequestSpecification requestSpecification = given().port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken);

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
