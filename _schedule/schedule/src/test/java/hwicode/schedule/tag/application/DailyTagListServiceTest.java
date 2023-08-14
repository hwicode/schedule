package hwicode.schedule.tag.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.exception.domain.dailytaglist.DailyTagNotFoundException;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagListRepository;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagRepository;
import hwicode.schedule.tag.infra.jpa_repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.tag.TagDataHelper.TAG_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class DailyTagListServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyTagListService dailyTagListService;

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
        Long dailyTagId = dailyTagListService.addTagToDailyTagList(dailyTagList.getId(), tag.getId());

        // then
        assertThat(dailyTagRepository.existsById(dailyTagId)).isTrue();
    }

    @Test
    void DailyTagList에_태그를_삭제할_수_있다() {
        // given
        Tag tag = new Tag(TAG_NAME);
        DailyTagList dailyTagList = new DailyTagList();

        tagRepository.save(tag);
        dailyTagListRepository.save(dailyTagList);

        Long dailyTagId = dailyTagListService.addTagToDailyTagList(dailyTagList.getId(), tag.getId());

        // when
        dailyTagListService.deleteTagToDailyTagList(dailyTagId, tag.getId());

        // then
        Long tagId = tag.getId();
        assertThatThrownBy(() -> dailyTagListService.deleteTagToDailyTagList(dailyTagId, tagId))
                .isInstanceOf(DailyTagNotFoundException.class);
        assertThat(dailyTagRepository.findAll()).isEmpty();
    }

}
