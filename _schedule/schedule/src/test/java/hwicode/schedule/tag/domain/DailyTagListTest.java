package hwicode.schedule.tag.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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

}

class DailyTagList {

    private final List<DailyTag> dailyTags = new ArrayList<>();

    public DailyTag addTag(Tag tag) {
        validateTag(tag);
        DailyTag dailyTag = new DailyTag(this, tag);
        dailyTags.add(dailyTag);
        return dailyTag;
    }

    private void validateTag(Tag tag) {
        boolean duplication = dailyTags.stream()
                .anyMatch(dailyTag -> dailyTag.isSameTag(tag));

        if (duplication) {
            throw new RuntimeException();
        }
    }
}

class DailyTag {

    private DailyTagList dailyTagList;
    private Tag tag;

    DailyTag(DailyTagList dailyTagList, Tag tag) {
        this.dailyTagList = dailyTagList;
        this.tag = tag;
    }

    boolean isSameTag(Tag tag) {
        return this.tag.isSame(tag);
    }

}
