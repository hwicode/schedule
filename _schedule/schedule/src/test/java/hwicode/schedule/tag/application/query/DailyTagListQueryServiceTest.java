package hwicode.schedule.tag.application.query;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.tag.application.query.dto.DailyTagListMemoQueryResponse;
import hwicode.schedule.tag.application.query.dto.DailyTagQueryResponse;
import hwicode.schedule.tag.domain.DailyTag;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.domain.Memo;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagListRepository;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagRepository;
import hwicode.schedule.tag.infra.jpa_repository.MemoRepository;
import hwicode.schedule.tag.infra.jpa_repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static hwicode.schedule.tag.TagDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DailyTagListQueryServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyTagListQueryService dailyTagListQueryService;

    @Autowired
    DailyTagListRepository dailyTagListRepository;

    @Autowired
    DailyTagRepository dailyTagRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    MemoRepository memoRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 특정_날짜의_태그들을_조회할_수_있다() {
        // given
        Long userId = 1L;
        LocalDate date = LocalDate.of(2023, 8, 24);

        DailyTagList dailyTagList = new DailyTagList(date, userId);
        dailyTagListRepository.save(dailyTagList);

        DailyTagQueryResponse dailyTagQueryResponse = createTagWithDailyTag(dailyTagList, TAG_NAME, userId);
        DailyTagQueryResponse dailyTagQueryResponse2 = createTagWithDailyTag(dailyTagList, TAG_NAME2, userId);

        // when
        List<DailyTagQueryResponse> result = dailyTagListQueryService.getDailyTagQueryResponses(userId, date);

        // then
        List<DailyTagQueryResponse> expectedResponses = List.of(dailyTagQueryResponse, dailyTagQueryResponse2);
        assertThat(result).isEqualTo(expectedResponses);
    }

    private DailyTagQueryResponse createTagWithDailyTag(DailyTagList dailyTagList, String tagName, Long userId) {
        Tag tag = new Tag(tagName, userId);
        tagRepository.save(tag);

        DailyTag dailyTag = new DailyTag(dailyTagList, tag);
        dailyTagRepository.save(dailyTag);

        return DailyTagQueryResponse.builder()
                .id(tag.getId())
                .name(tagName)
                .build();
    }

    @Test
    void 계획표에_존재하는_메모들을_조회할_수_있다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        dailyTagListRepository.save(dailyTagList);

        for (int i = 0; i < 3; i++) {
            Memo memo = dailyTagList.createMemo(MEMO_TEXT + i);
            memoRepository.save(memo);
        }

        // when
        List<DailyTagListMemoQueryResponse> dailyTagListMemoQueryResponses = dailyTagListQueryService.getDailyTagListMemoQueryResponses(userId, dailyTagList.getId());

        // then
        assertThat(dailyTagListMemoQueryResponses).hasSize(3);
        for (int i = 0; i < 3; i++) {
            assertThat(dailyTagListMemoQueryResponses.get(i).getText()).isEqualTo(MEMO_TEXT + i);
        }
    }

}
