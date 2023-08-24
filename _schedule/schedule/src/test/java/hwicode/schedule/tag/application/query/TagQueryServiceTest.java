package hwicode.schedule.tag.application.query;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.tag.application.query.dto.TagQueryResponse;
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
import static hwicode.schedule.tag.TagDataHelper.TAG_NAME2;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TagQueryServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TagQueryService tagQueryService;

    @Autowired
    DailyTagListRepository dailyTagListRepository;

    @Autowired
    DailyTagRepository dailyTagRepository;

    @Autowired
    TagRepository tagRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 특정_날짜의_태그들을_조회할_수_있다() {
        // given
        LocalDate date = LocalDate.of(2023, 8, 24);

        DailyTagList dailyTagList = new DailyTagList(date);
        dailyTagListRepository.save(dailyTagList);

        TagQueryResponse tagQueryResponse = createTagWithDailyTag(dailyTagList, TAG_NAME);
        TagQueryResponse tagQueryResponse2 = createTagWithDailyTag(dailyTagList, TAG_NAME2);

        // when
        List<TagQueryResponse> result = tagQueryService.getTagQueryResponses(date);

        // then
        List<TagQueryResponse> expectedResponses = List.of(tagQueryResponse, tagQueryResponse2);
        assertThat(result).isEqualTo(expectedResponses);
    }

    private TagQueryResponse createTagWithDailyTag(DailyTagList dailyTagList, String tagName) {
        Tag tag = new Tag(tagName);
        tagRepository.save(tag);

        DailyTag dailyTag = new DailyTag(dailyTagList, tag);
        dailyTagRepository.save(dailyTag);

        return TagQueryResponse.builder()
                .id(tag.getId())
                .name(tagName)
                .build();
    }

}
