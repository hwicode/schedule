package hwicode.schedule.tag.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.domain.Memo;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.exception.application.TagDuplicateException;
import hwicode.schedule.tag.exception.application.TagNotFoundException;
import hwicode.schedule.tag.infra.jpa_repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static hwicode.schedule.tag.TagDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TagServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TagService tagService;

    @Autowired
    DailyTagListAggregateService  dailyTagListAggregateService;

    @Autowired
    MemoService memoService;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    DailyTagListRepository dailyTagListRepository;

    @Autowired
    DailyTagRepository dailyTagRepository;

    @Autowired
    MemoRepository memoRepository;

    @Autowired
    MemoTagRepository memoTagRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 태그를_생성할_수_있다() {
        // when
        Long tagId = tagService.saveTag(TAG_NAME);

        // then
        assertThat(tagRepository.existsById(tagId)).isTrue();
    }

    @Test
    void 태그를_생성할_때_이름이_중복되면_에러가_발생한다() {
        // given
        tagService.saveTag(TAG_NAME);

        // when then
        assertThatThrownBy(() -> tagService.saveTag(TAG_NAME))
                .isInstanceOf(TagDuplicateException.class);
    }

    @Test
    void 태그의_이름을_변경할_수_있다() {
        // given
        Long tagId = tagService.saveTag(TAG_NAME);

        // when
        String changedTagName = tagService.changeTagName(tagId, NEW_TAG_NAME);

        // then
        Tag tag = tagRepository.findById(tagId).orElseThrow();
        assertThat(tag.changeName(changedTagName)).isFalse();
    }

    @Test
    void 태그를_삭제할_수_있다() {
        // given
        Long tagId = tagService.saveTag(TAG_NAME);

        // when
        tagService.deleteTag(tagId);

        // then
        assertThatThrownBy(() -> tagService.deleteTag(tagId))
                .isInstanceOf(TagNotFoundException.class);
    }

    private static Stream<List<DailyTagList>> provideDailyTagLists() {
        return Stream.of(
                List.of(new DailyTagList()),
                List.of(new DailyTagList(), new DailyTagList()),
                List.of(new DailyTagList(), new DailyTagList(), new DailyTagList()),
                List.of(new DailyTagList(), new DailyTagList(), new DailyTagList(), new DailyTagList())
        );
    }

    @ParameterizedTest
    @MethodSource("provideDailyTagLists")
    void 태그를_삭제할_때_DailyTag도_같이_삭제할_수_있다(List<DailyTagList> dailyTagLists) {
        // given
        Long tagId = tagService.saveTag(TAG_NAME);

        dailyTagListRepository.saveAll(dailyTagLists);
        dailyTagLists.forEach(
                dailyTagList -> dailyTagListAggregateService.addTagToDailyTagList(dailyTagList.getId(), tagId)
        );

        // when
        tagService.deleteTag(tagId);

        // then
        assertThatThrownBy(() -> tagService.deleteTag(tagId))
                .isInstanceOf(TagNotFoundException.class);
        assertThat(tagRepository.findAll()).isEmpty();
        assertThat(dailyTagRepository.findAll()).isEmpty();
    }

    @Test
    void 태그를_삭제할_때_MemoTag도_같이_삭제할_수_있다() {
        // given
        Long tagId = tagService.saveTag(TAG_NAME);

        DailyTagList dailyTagList = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);

        Memo memo = new Memo(MEMO_TEXT, dailyTagList);
        Memo memo2 = new Memo(MEMO_TEXT2, dailyTagList);
        memoRepository.save(memo);
        memoRepository.save(memo2);

        memoService.addTagsToMemo(memo.getId(), List.of(tagId));
        memoService.addTagsToMemo(memo2.getId(), List.of(tagId));

        // when
        tagService.deleteTag(tagId);

        // then
        assertThatThrownBy(() -> tagService.deleteTag(tagId))
                .isInstanceOf(TagNotFoundException.class);
        assertThat(tagRepository.findAll()).isEmpty();
        assertThat(memoTagRepository.findAll()).isEmpty();
    }

    @Test
    void 태그를_삭제할_때_DailyTag와_MemoTag도_같이_삭제할_수_있다() {
        // given
        Long tagId = tagService.saveTag(TAG_NAME);

        DailyTagList dailyTagList = new DailyTagList();
        DailyTagList dailyTagList2 = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);
        dailyTagListRepository.save(dailyTagList2);

        dailyTagListAggregateService.addTagToDailyTagList(dailyTagList.getId(), tagId);
        dailyTagListAggregateService.addTagToDailyTagList(dailyTagList2.getId(), tagId);

        Memo memo = new Memo(MEMO_TEXT, dailyTagList);
        Memo memo2 = new Memo(MEMO_TEXT2, dailyTagList);
        memoRepository.save(memo);
        memoRepository.save(memo2);

        memoService.addTagsToMemo(memo.getId(), List.of(tagId));
        memoService.addTagsToMemo(memo2.getId(), List.of(tagId));

        // when
        tagService.deleteTag(tagId);

        // then
        assertThatThrownBy(() -> tagService.deleteTag(tagId))
                .isInstanceOf(TagNotFoundException.class);
        assertThat(tagRepository.findAll()).isEmpty();
        assertThat(dailyTagRepository.findAll()).isEmpty();
        assertThat(memoTagRepository.findAll()).isEmpty();
    }

}
