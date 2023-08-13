package hwicode.schedule.tag.domain;

import org.junit.jupiter.api.Test;

import static hwicode.schedule.tag.TagDataHelper.NEW_TAG_NAME;
import static hwicode.schedule.tag.TagDataHelper.TAG_NAME;
import static org.assertj.core.api.Assertions.assertThat;

class TagTest {

    @Test
    void 태그의_이름을_수정할_때_이름이_동일하면_변경이_없으므로_false가_리턴된다() {
        // given
        Tag tag = new Tag(TAG_NAME);

        // when
        boolean isChange = tag.changeName(TAG_NAME);

        // then
        assertThat(isChange).isFalse();
    }

    @Test
    void 태그의_이름을_수정할_때_이름이_다르면_변경이_있으므로_true가_리턴된다() {
        // given
        Tag tag = new Tag(TAG_NAME);

        // when
        boolean isChange = tag.changeName(NEW_TAG_NAME);

        // then
        assertThat(isChange).isTrue();
    }

}
