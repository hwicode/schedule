package hwicode.schedule.tag.application.query;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.tag.application.query.dto.DailyTagListQueryResponse;
import hwicode.schedule.tag.domain.DailyTag;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagListRepository;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagRepository;
import hwicode.schedule.tag.infra.jpa_repository.TagRepository;
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

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 데일리_태그_리스트의_첫_번째_페이지_조회를_요청할_수_있다() {
        //given
        Tag tag = new Tag(TAG_NAME);
        tagRepository.save(tag);

        LocalDate date = LocalDate.of(2023, 8, 27);
        int size = 10;
        for (int i = 1; i <= size; i++) {
            DailyTagList dailyTagList = new DailyTagList(date.plusDays(i));
            DailyTag dailyTag = new DailyTag(dailyTagList, tag);

            dailyTagListRepository.save(dailyTagList);
            dailyTagRepository.save(dailyTag);
        }

        //when
        List<DailyTagListQueryResponse> dailyTagListQueryResponsePage = tagQueryService.getDailyTagListQueryResponsePage(tag.getId(), null);

        //then
        assertThat(dailyTagListQueryResponsePage).hasSize(10);
        assertThat(dailyTagListQueryResponsePage.get(0).getYearAndMonthAndDay()).isEqualTo(date.plusDays(size));
        assertThat(dailyTagListQueryResponsePage.get(9).getYearAndMonthAndDay()).isEqualTo(date.plusDays(size - 9));
    }

    @Test
    void 데일리_태그_리스트의_두_번째_페이지_조회를_요청할_수_있다() {
        // given
        Tag tag = new Tag(TAG_NAME);
        tagRepository.save(tag);

        LocalDate date = LocalDate.of(2023, 8, 27);

        for (int i = 1; i <= 20; i++) {
            DailyTagList dailyTagList = new DailyTagList(date.plusDays(i));
            DailyTag dailyTag = new DailyTag(dailyTagList, tag);

            dailyTagListRepository.save(dailyTagList);
            dailyTagRepository.save(dailyTag);
        }

        // when
        List<DailyTagListQueryResponse> dailyTagListQueryResponsePage = tagQueryService.getDailyTagListQueryResponsePage(tag.getId(), 11L);

        // then
        assertThat(dailyTagListQueryResponsePage).hasSize(10);
        assertThat(dailyTagListQueryResponsePage.get(0).getYearAndMonthAndDay()).isEqualTo(date.plusDays(10));
        assertThat(dailyTagListQueryResponsePage.get(9).getYearAndMonthAndDay()).isEqualTo(date.plusDays(1));
    }

}
