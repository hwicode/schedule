package hwicode.schedule.tag.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static hwicode.schedule.tag.TagDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemoTest {

    @Test
    void Memo의_내용을_수정할_때_내용이_동일하면_변경이_없으므로_false가_리턴된다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList);

        // when
        boolean isChange = memo.changeText(MEMO_TEXT);

        // then
        assertThat(isChange).isFalse();
    }

    @Test
    void Memo의_내용을_수정할_때_내용이_다르면_변경이_있으므로_true가_리턴된다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList);

        // when
        boolean isChange = memo.changeText(NEW_MEMO_TEXT);

        // then
        assertThat(isChange).isTrue();
    }

    @Test
    void Memo에_Tag를_추가할_수_있다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList);
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
        Memo memo = new Memo(MEMO_TEXT, dailyTagList);
        Tag tag = new Tag(TAG_NAME);

        memo.addTag(tag);

        // when then
        assertThatThrownBy(() -> memo.addTag(tag))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void Memo에_Tag를_삭제할_수_있다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList);
        Tag tag = new Tag(TAG_NAME);

        memo.addTag(tag);

        // when
        memo.deleteTag(tag);

        // then
        assertThatThrownBy(() -> memo.deleteTag(tag))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void Memo에_존재하지_않는_Tag를_조회하면_에러가_발생한다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList);
        Tag tag = new Tag(TAG_NAME);

        // when then
        assertThatThrownBy(() -> memo.deleteTag(tag))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void Memo에_여러_개의_Tag를_추가할_수_있다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList);
        List<Tag> tags = List.of(
                new Tag(TAG_NAME),
                new Tag(TAG_NAME2),
                new Tag(TAG_NAME3)
        );

        // when
        List<MemoTag> memoTags = memo.addTags(tags);

        // then
        for (int i = 0; i < tags.size(); i++) {
            MemoTag memoTag = memoTags.get(i);
            Tag tag = tags.get(i);
            assertThat(memoTag.isSameTag(tag)).isTrue();
        }
    }

}

class Memo {

    private String text;
    private DailyTagList dailyTagList;
    private final List<MemoTag> memoTags = new ArrayList<>();

    public Memo(String text, DailyTagList dailyTagList) {
        this.text = text;
        this.dailyTagList = dailyTagList;
    }

    public boolean changeText(String text) {
        if (this.text.equals(text)) {
            return false;
        }
        this.text = text;
        return true;
    }

    public List<MemoTag> addTags(List<Tag> tags) {
        return tags.stream()
                .map(this::addTag)
                .collect(Collectors.toList());
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

    public void deleteTag(Tag tag) {
        MemoTag memoTag = findMemoTagBy(tag);
        memoTags.remove(memoTag);
    }

    private MemoTag findMemoTagBy(Tag tag) {
        return memoTags.stream()
                .filter(memoTag -> memoTag.isSameTag(tag))
                .findFirst()
                .orElseThrow(RuntimeException::new);
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
