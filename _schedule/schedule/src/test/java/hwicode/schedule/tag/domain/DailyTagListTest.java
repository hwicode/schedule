package hwicode.schedule.tag.domain;

import org.junit.jupiter.api.Test;

import static hwicode.schedule.tag.TagDataHelper.TAG_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DailyTagListTest {

    @Test
    void DailyTagList에_Tag를_추가할_수_있다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Tag tag = new Tag(TAG_NAME);

        // when
        DailyTag dailyTag = dailyTagList.addTag(tag);

        // then
        assertThat(dailyTag.isSameTag(tag)).isTrue();
    }


    @Test
    void DailyTagList에_추가하는_Tag의_이름이_중복되면_에러가_발생한다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Tag tag = new Tag(TAG_NAME);
        dailyTagList.addTag(tag);

        // when then
        assertThatThrownBy(() -> dailyTagList.addTag(tag))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void DailyTagList에_Tag를_삭제할_수_있다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Tag tag = new Tag(TAG_NAME);
        dailyTagList.addTag(tag);

        // when
        dailyTagList.deleteTag(tag);

        // then
        assertThatThrownBy(() -> dailyTagList.deleteTag(tag))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void DailyTagList에_존재하지_않는_Tag를_조회하면_에러가_발생한다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Tag tag = new Tag(TAG_NAME);

        // when then
        assertThatThrownBy(() -> dailyTagList.deleteTag(tag))
                .isInstanceOf(RuntimeException.class);
    }

}
