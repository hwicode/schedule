package hwicode.schedule.tag.application.query;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.tag.application.query.dto.DailyTagListSearchQueryResponse;
import hwicode.schedule.tag.application.query.dto.MemoSearchQueryResponse;
import hwicode.schedule.tag.application.query.dto.TagQueryResponse;
import hwicode.schedule.tag.application.query.dto.TagSearchQueryResponse;
import hwicode.schedule.tag.domain.*;
import hwicode.schedule.tag.infra.jpa_repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static hwicode.schedule.tag.TagDataHelper.TAG_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TagQueryServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TagQueryService tagQueryService;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    DailyTagListRepository dailyTagListRepository;

    @Autowired
    DailyTagRepository dailyTagRepository;

    @Autowired
    MemoTagRepository memoTagRepository;

    @Autowired
    MemoRepository memoRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 데일리_태그_리스트의_첫_번째_페이지_조회를_요청할_수_있다() {
        //given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, userId);
        tagRepository.save(tag);

        LocalDate date = LocalDate.of(2023, 8, 27);
        int size = 10;
        for (int i = 1; i <= size; i++) {
            DailyTagList dailyTagList = new DailyTagList(date.plusDays(i), userId);
            DailyTag dailyTag = new DailyTag(dailyTagList, tag);

            dailyTagListRepository.save(dailyTagList);
            dailyTagRepository.save(dailyTag);
        }

        //when
        List<DailyTagListSearchQueryResponse> dailyTagListSearchQueryResponsePage = tagQueryService.getDailyTagListSearchQueryResponsePage(tag.getId(), null);

        //then
        assertThat(dailyTagListSearchQueryResponsePage).hasSize(10);
        assertThat(dailyTagListSearchQueryResponsePage.get(0).getYearAndMonthAndDay()).isEqualTo(date.plusDays(size));
        assertThat(dailyTagListSearchQueryResponsePage.get(9).getYearAndMonthAndDay()).isEqualTo(date.plusDays(size - 9));
    }

    @Test
    void 데일리_태그_리스트의_두_번째_페이지_조회를_요청할_수_있다() {
        // given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, userId);
        tagRepository.save(tag);

        LocalDate date = LocalDate.of(2023, 8, 27);

        for (int i = 1; i <= 20; i++) {
            DailyTagList dailyTagList = new DailyTagList(date.plusDays(i), userId);
            DailyTag dailyTag = new DailyTag(dailyTagList, tag);

            dailyTagListRepository.save(dailyTagList);
            dailyTagRepository.save(dailyTag);
        }

        // when
        List<DailyTagListSearchQueryResponse> dailyTagListSearchQueryResponsePage = tagQueryService.getDailyTagListSearchQueryResponsePage(tag.getId(), 11L);

        // then
        assertThat(dailyTagListSearchQueryResponsePage).hasSize(10);
        assertThat(dailyTagListSearchQueryResponsePage.get(0).getYearAndMonthAndDay()).isEqualTo(date.plusDays(10));
        assertThat(dailyTagListSearchQueryResponsePage.get(9).getYearAndMonthAndDay()).isEqualTo(date.plusDays(1));
    }

    @Test
    void 메모의_첫_번째_페이지_조회를_요청할_수_있다() {
        //given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, userId);
        tagRepository.save(tag);

        String text = "a";
        for (int i = 1; i <= 10; i++) {
            DailyTagList dailyTagList = new DailyTagList();
            dailyTagListRepository.save(dailyTagList);

            Memo memo = new Memo(text + i, dailyTagList);
            MemoTag memoTag = new MemoTag(memo, tag);
            memoRepository.save(memo);
            memoTagRepository.save(memoTag);
        }

        //when
        List<MemoSearchQueryResponse> memoSearchQueryResponsePage = tagQueryService.getMemoSearchQueryResponsePage(tag.getId(), null);

        //then
        assertThat(memoSearchQueryResponsePage).hasSize(10);
        assertThat(memoSearchQueryResponsePage.get(0).getText()).isEqualTo("a10");
        assertThat(memoSearchQueryResponsePage.get(9).getText()).isEqualTo("a1");
    }

    @Test
    void 메모의_두_번째_페이지_조회를_요청할_수_있다() {
        // given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, userId);
        tagRepository.save(tag);

        String text = "a";
        for (int i = 1; i <= 20; i++) {
            DailyTagList dailyTagList = new DailyTagList();
            dailyTagListRepository.save(dailyTagList);

            Memo memo = new Memo(text + i, dailyTagList);
            MemoTag memoTag = new MemoTag(memo, tag);
            memoRepository.save(memo);
            memoTagRepository.save(memoTag);
        }

        // when
        List<MemoSearchQueryResponse> memoSearchQueryResponsePage = tagQueryService.getMemoSearchQueryResponsePage(tag.getId(), 11L);

        // then
        assertThat(memoSearchQueryResponsePage).hasSize(10);
        assertThat(memoSearchQueryResponsePage.get(0).getText()).isEqualTo("a10");
        assertThat(memoSearchQueryResponsePage.get(9).getText()).isEqualTo("a1");
    }

    @Test
    void 모든_태그를_조회할_수_있다() {
        // given
        Long userId = 1L;
        for (int i = 0; i < 5; i++) {
            Tag tag = new Tag(TAG_NAME + i, userId);
            tagRepository.save(tag);
        }

        // when
        List<TagQueryResponse> tagQueryResponses = tagQueryService.getTagQueryResponses();

        // then
        assertThat(tagQueryResponses).hasSize(5);
        for (int i = 0; i < 5; i++) {
            assertThat(tagQueryResponses.get(i).getName()).isEqualTo(TAG_NAME + i);
        }
    }

    @Test
    void 검색_키워드로_시작하는_태그_이름을_가진_태그들을_조회할_수_있다() {
        // given
        Long userId = 1L;
        for (int i = 0; i < 5; i++) {
            Tag tag = new Tag(TAG_NAME + i, userId);
            tagRepository.save(tag);
        }
        for (int i = 0; i < 5; i++) {
            Tag tag = new Tag(i + TAG_NAME, userId);
            tagRepository.save(tag);
        }

        // when
        List<TagSearchQueryResponse> tagSearchQueryResponses = tagQueryService.getTagSearchQueryResponses(TAG_NAME);

        // then
        assertThat(tagSearchQueryResponses).hasSize(5);
        for (int i = 0; i < 5; i++) {
            assertThat(tagSearchQueryResponses.get(i).getName()).isEqualTo(TAG_NAME + i);
        }
    }

}
