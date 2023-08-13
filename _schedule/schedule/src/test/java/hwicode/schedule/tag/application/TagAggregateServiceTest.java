package hwicode.schedule.tag.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.exception.application.TagDuplicateException;
import hwicode.schedule.tag.exception.application.TagNotFoundException;
import hwicode.schedule.tag.infra.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.tag.TagDataHelper.NEW_TAG_NAME;
import static hwicode.schedule.tag.TagDataHelper.TAG_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TagAggregateServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TagAggregateService tagAggregateService;

    @Autowired
    TagRepository tagRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 태그를_생성할_수_있다() {
        // when
        Long tagId = tagAggregateService.saveTag(TAG_NAME);

        // then
        assertThat(tagRepository.existsById(tagId)).isTrue();
    }

    @Test
    void 태그를_생성할_때_이름이_중복되면_에러가_발생한다() {
        // given
        tagAggregateService.saveTag(TAG_NAME);

        // when then
        assertThatThrownBy(() -> tagAggregateService.saveTag(TAG_NAME))
                .isInstanceOf(TagDuplicateException.class);
    }

    @Test
    void 태그의_이름을_변경할_수_있다() {
        // given
        Long tagId = tagAggregateService.saveTag(TAG_NAME);

        // when
        String changedTagName = tagAggregateService.changeTagName(tagId, NEW_TAG_NAME);

        // then
        Tag tag = tagRepository.findById(tagId).orElseThrow();
        assertThat(tag.changeName(changedTagName)).isFalse();
    }

    @Test
    void 존재하지_않는_태그를_조회하면_에러가_발생한다() {
        // given
        Long noneExistId = 1L;

        // when then
        assertThatThrownBy(() -> tagAggregateService.changeTagName(noneExistId, NEW_TAG_NAME))
                .isInstanceOf(TagNotFoundException.class);
    }

    @Test
    void 태그를_삭제할_수_있다() {
        // given
        Long tagId = tagAggregateService.saveTag(TAG_NAME);

        // when
        tagAggregateService.deleteTag(tagId);

        // then
        assertThatThrownBy(() -> tagAggregateService.deleteTag(tagId))
                .isInstanceOf(TagNotFoundException.class);
    }

}
