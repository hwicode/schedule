package hwicode.schedule.tag.domain;

import hwicode.schedule.tag.exception.domain.memo.InvalidNumberOfTagsException;
import hwicode.schedule.tag.exception.domain.memo.MemoTagDuplicateException;
import hwicode.schedule.tag.exception.domain.memo.MemoTagNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static hwicode.schedule.tag.TagDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemoTest {

    @Test
    void Memo의_내용을_수정할_때_내용이_동일하면_변경이_없으므로_false가_리턴된다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList, 1L);

        // when
        boolean isChange = memo.changeText(MEMO_TEXT);

        // then
        assertThat(isChange).isFalse();
    }

    @Test
    void Memo의_내용을_수정할_때_내용이_다르면_변경이_있으므로_true가_리턴된다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList, 1L);

        // when
        boolean isChange = memo.changeText(NEW_MEMO_TEXT);

        // then
        assertThat(isChange).isTrue();
    }

    private static Stream<List<Tag>> provideTags() {
        return Stream.of(
                List.of(new Tag(TAG_NAME, 1L)),
                List.of(new Tag(TAG_NAME, 1L), new Tag(TAG_NAME2, 1L)),
                List.of(new Tag(TAG_NAME, 1L), new Tag(TAG_NAME2, 1L), new Tag(TAG_NAME3, 1L)),
                List.of(new Tag(TAG_NAME, 1L), new Tag(TAG_NAME2, 1L), new Tag(TAG_NAME3, 1L), new Tag(TAG_NAME4, 1L))
        );
    }

    @MethodSource("provideTags")
    @ParameterizedTest
    void Memo에_Tag를_추가할_수_있다(List<Tag> tags) {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList, 1L);

        for (Tag tag : tags) {
            // when
            MemoTag memoTag = memo.addTag(tag);

            // then
            assertThat(memoTag.isSameTag(tag)).isTrue();
        }
    }

    @Test
    void Memo에_추가하는_Tag의_이름이_중복되면_에러가_발생한다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList, 1L);
        List<Tag> tags = List.of(
                new Tag(TAG_NAME, 1L), new Tag(TAG_NAME2, 1L), new Tag(TAG_NAME3, 1L)
        );

        memo.addTags(tags);

        // when then
        Tag duplicatedTag = tags.get(0);
        assertThatThrownBy(() -> memo.addTag(duplicatedTag))
                .isInstanceOf(MemoTagDuplicateException.class);
    }

    @MethodSource("provideTags")
    @ParameterizedTest
    void Memo에_Tag를_삭제할_수_있다(List<Tag> tags) {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList, 1L);
        memo.addTags(tags);

        for (Tag tag : tags) {
            // when
            memo.deleteTag(tag);

            // then
            assertThatThrownBy(() -> memo.deleteTag(tag))
                    .isInstanceOf(MemoTagNotFoundException.class);
        }
    }

    @Test
    void Memo에_존재하지_않는_Tag를_조회하면_에러가_발생한다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList, 1L);
        Tag tag = new Tag(TAG_NAME, 1L);

        // when then
        assertThatThrownBy(() -> memo.deleteTag(tag))
                .isInstanceOf(MemoTagNotFoundException.class);
    }

    @MethodSource("provideTags")
    @ParameterizedTest
    void Memo에_여러_개의_Tag를_추가할_수_있다(List<Tag> tags) {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList, 1L);

        // when
        List<MemoTag> memoTags = memo.addTags(tags);

        // then
        for (int i = 0; i < tags.size(); i++) {
            MemoTag memoTag = memoTags.get(i);
            Tag tag = tags.get(i);
            assertThat(memoTag.isSameTag(tag)).isTrue();
        }
    }

    @Test
    void Memo에_10보다_큰_수의_Tag를_추가하면_에러가_발생한다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList, 1L);
        List<Tag> tags = List.of(
                new Tag(TAG_NAME, 1L), new Tag(TAG_NAME2, 1L), new Tag(TAG_NAME3, 1L),
                new Tag(TAG_NAME4, 1L), new Tag(TAG_NAME5, 1L), new Tag(TAG_NAME6, 1L),
                new Tag(TAG_NAME7, 1L), new Tag(TAG_NAME8, 1L), new Tag(TAG_NAME9, 1L),
                new Tag(TAG_NAME10, 1L), new Tag(TAG_NAME11, 1L)
        );

        // when then
        assertThatThrownBy(() -> memo.addTags(tags))
                .isInstanceOf(InvalidNumberOfTagsException.class);
    }

}
