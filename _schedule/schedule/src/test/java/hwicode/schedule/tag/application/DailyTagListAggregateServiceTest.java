package hwicode.schedule.tag.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.exception.application.DailyTagListNotFoundException;
import hwicode.schedule.tag.exception.domain.dailytaglist.DailyTagNotFoundException;
import hwicode.schedule.tag.infra.DailyTagListRepository;
import hwicode.schedule.tag.infra.DailyTagRepository;
import hwicode.schedule.tag.infra.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.tag.TagDataHelper.TAG_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class DailyTagListAggregateServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyTagListAggregateService dailyTagListAggregateService;

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
    void DailyTagList에_태그를_추가할_수_있다() {
        // given
        Tag tag = new Tag(TAG_NAME);
        DailyTagList dailyTagList = new DailyTagList();

        tagRepository.save(tag);
        dailyTagListRepository.save(dailyTagList);

        // when
        Long dailyTagId = dailyTagListAggregateService.addTagToDailyTagList(dailyTagList.getId(), tag.getId());

        // then
        assertThat(dailyTagRepository.existsById(dailyTagId)).isTrue();
    }

    @Test
    void 존재하지_않는_DailyTagList를_조회하면_에러가_발생한다() {
        // given
        Long noneExistId = 1L;
        Tag tag = new Tag(TAG_NAME);
        tagRepository.save(tag);

        Long tagId = tag.getId();

        // when
        assertThatThrownBy(() -> dailyTagListAggregateService.addTagToDailyTagList(noneExistId, tagId))
                .isInstanceOf(DailyTagListNotFoundException.class);
    }

    @Test
    void DailyTagList에_태그를_삭제할_수_있다() {
        // given
        Tag tag = new Tag(TAG_NAME);
        DailyTagList dailyTagList = new DailyTagList();

        tagRepository.save(tag);
        dailyTagListRepository.save(dailyTagList);

        Long dailyTagId = dailyTagListAggregateService.addTagToDailyTagList(dailyTagList.getId(), tag.getId());

        // when
        dailyTagListAggregateService.deleteTagToDailyTagList(dailyTagId, tag.getId());

        // then
        Long tagId = tag.getId();
        assertThatThrownBy(() -> dailyTagListAggregateService.deleteTagToDailyTagList(dailyTagId, tagId))
                .isInstanceOf(DailyTagNotFoundException.class);
        assertThat(dailyTagRepository.findAll()).isEmpty();
    }

}
