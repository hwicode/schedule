package hwicode.schedule.tag.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static hwicode.schedule.tag.TagDataHelper.TAG_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemoTest {

    @Test
    void Memo에_Tag를_추가할_수_있다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(dailyTagList);
        Tag tag = new Tag(TAG_NAME);

        // when
        MemoTag memoTag = memo.addTag(tag);

        // then
        assertThat(memoTag.isSameTag(tag)).isTrue();
    }

    @Test
    void Memo에_추가하는_Tag의_이름이_중복되면_에러가_발생한다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(dailyTagList);
        Tag tag = new Tag(TAG_NAME);

        memo.addTag(tag);

        // when then
        assertThatThrownBy(() -> memo.addTag(tag))
                .isInstanceOf(RuntimeException.class);
    }

}

class Memo {

    private DailyTagList dailyTagList;
    private final List<MemoTag> memoTags = new ArrayList<>();

    public Memo(DailyTagList dailyTagList) {
        this.dailyTagList = dailyTagList;
    }

    public MemoTag addTag(Tag tag) {
        validateTag(tag);
        MemoTag memoTag = new MemoTag(this, tag);
        memoTags.add(memoTag);
        return memoTag;
    }

    private void validateTag(Tag tag) {
        boolean duplication = memoTags.stream().anyMatch(m -> m.isSameTag(tag));

        if (duplication) {
            throw new RuntimeException();
        }
    }
}

class MemoTag {

    private Memo memo;
    private Tag tag;

    MemoTag(Memo memo, Tag tag) {
        this.memo = memo;
        this.tag = tag;
    }

    boolean isSameTag(Tag tag) {
        return this.tag.isSame(tag);
    }

}
