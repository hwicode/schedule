package hwicode.schedule.tag.domain;

import hwicode.schedule.tag.exception.domain.dailytaglist.DailyTagDuplicateException;
import hwicode.schedule.tag.exception.domain.dailytaglist.DailyTagNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static hwicode.schedule.tag.TagDataHelper.*;
import static hwicode.schedule.tag.TagDataHelper.TAG_NAME4;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DailyTagListTest {

    private static Stream<List<Tag>> provideTags() {
        return Stream.of(
                List.of(new Tag(TAG_NAME)),
                List.of(new Tag(TAG_NAME), new Tag(TAG_NAME2)),
                List.of(new Tag(TAG_NAME), new Tag(TAG_NAME2), new Tag(TAG_NAME3)),
                List.of(new Tag(TAG_NAME), new Tag(TAG_NAME2), new Tag(TAG_NAME3), new Tag(TAG_NAME4))
        );
    }

    @MethodSource("provideTags")
    @ParameterizedTest
    void DailyTagList에_Tag를_추가할_수_있다(List<Tag> tags) {
        // given
        DailyTagList dailyTagList = new DailyTagList();

        for (Tag tag : tags) {
            // when
            DailyTag dailyTag = dailyTagList.addTag(tag);

            // then
            assertThat(dailyTag.isSameTag(tag)).isTrue();
        }
    }

    @Test
    void DailyTagList에_추가하는_Tag의_이름이_중복되면_에러가_발생한다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        List<Tag> tags = List.of(new Tag(TAG_NAME), new Tag(TAG_NAME2), new Tag(TAG_NAME3));
        dailyTagList.addTag(tags.get(0));
        dailyTagList.addTag(tags.get(1));
        dailyTagList.addTag(tags.get(2));

        // when then
        for (Tag tag : tags) {
            assertThatThrownBy(() -> dailyTagList.addTag(tag))
                    .isInstanceOf(DailyTagDuplicateException.class);
        }
    }

    @MethodSource("provideTags")
    @ParameterizedTest
    void DailyTagList에_Tag를_삭제할_수_있다(List<Tag> tags) {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        for (Tag tag : tags) {
            dailyTagList.addTag(tag);
        }

        for (Tag tag : tags) {
            // when
            dailyTagList.deleteTag(tag);

            // then
            assertThatThrownBy(() -> dailyTagList.deleteTag(tag))
                    .isInstanceOf(DailyTagNotFoundException.class);
        }
    }

    @MethodSource("provideTags")
    @ParameterizedTest
    void DailyTagList에_메인_태그를_설정할_수_있다(List<Tag> tags) {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        for (Tag tag : tags) {
            dailyTagList.addTag(tag);
        }

        for (Tag tag : tags) {
            // when
            String mainTagName = dailyTagList.changeMainTag(tag);

            // then
            assertThat(tag.getName()).isEqualTo(mainTagName);
        }
    }

    @MethodSource("provideTags")
    @ParameterizedTest
    void DailyTagList에_메인_태그를_삭제하면_메인_태그는_null이_된다(List<Tag> tags) {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        for (Tag tag : tags) {
            dailyTagList.addTag(tag);
        }

        for (Tag tag : tags) {
            dailyTagList.changeMainTag(tag);

            // when
            dailyTagList.deleteTag(tag);

            // then
            assertThat(dailyTagList.getMainTag()).isNull();
        }
    }

    @Test
    void DailyTagList에_존재하지_않는_Tag를_조회하면_에러가_발생한다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Tag tag = new Tag(TAG_NAME);

        // when then
        assertThatThrownBy(() -> dailyTagList.deleteTag(tag))
                .isInstanceOf(DailyTagNotFoundException.class);
    }

}
