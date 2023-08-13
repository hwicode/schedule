package hwicode.schedule.tag.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.domain.Memo;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.exception.application.MemoNotFoundException;
import hwicode.schedule.tag.infra.DailyTagListRepository;
import hwicode.schedule.tag.infra.MemoRepository;
import hwicode.schedule.tag.infra.MemoTagRepository;
import hwicode.schedule.tag.infra.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static hwicode.schedule.tag.TagDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MemoServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    MemoService memoService;

    @Autowired
    MemoRepository memoRepository;

    @Autowired
    DailyTagListRepository dailyTagListRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    MemoTagRepository memoTagRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 메모를_생성할_수_있다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);

        // when
        Long memoId = memoService.saveMemo(dailyTagList.getId(), MEMO_TEXT);

        // then
        assertThat(memoRepository.existsById(memoId)).isTrue();
    }


    @Test
    void 메모의_내용를_변경할_수_있다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList);

        dailyTagListRepository.save(dailyTagList);
        memoRepository.save(memo);

        // when
        String changedText = memoService.changeMemoText(memo.getId(), NEW_MEMO_TEXT);

        // then
        Memo savedMemo = memoRepository.findById(memo.getId()).orElseThrow();
        assertThat(savedMemo.changeText(changedText)).isFalse();
    }

    @Test
    void 존재하지_않는_메모를_조회하면_에러가_발생한다() {
        // given
        Long noneExistId = 1L;

        // when then
        assertThatThrownBy(() -> memoService.changeMemoText(noneExistId, MEMO_TEXT))
                .isInstanceOf(MemoNotFoundException.class);
    }

    private static Stream<List<Tag>> provideTags() {
        return Stream.of(
                List.of(new Tag(TAG_NAME)),
                List.of(new Tag(TAG_NAME), new Tag(TAG_NAME2)),
                List.of(new Tag(TAG_NAME), new Tag(TAG_NAME2), new Tag(TAG_NAME3))
        );
    }

    @MethodSource("provideTags")
    @ParameterizedTest
    void 메모에_태그를_여러_개_추가할_수_있다(List<Tag> tags) {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList);

        dailyTagListRepository.save(dailyTagList);
        memoRepository.save(memo);

        List<Long> tagIds = tagRepository.saveAll(tags)
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        // when
        memoService.addTagsToMemo(memo.getId(), tagIds);

        // then
        assertThat(memoTagRepository.findAll()).hasSize(tags.size());
    }

    @MethodSource("provideTags")
    @ParameterizedTest
    void 메모와_태그를_여러_개를_같이_생성할_수_있다(List<Tag> tags) {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);

        List<Long> tagIds = tagRepository.saveAll(tags)
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        // when
        Long memoId = memoService.saveMemoWithTags(dailyTagList.getId(), MEMO_TEXT, tagIds);

        // then
        assertThat(memoRepository.existsById(memoId)).isTrue();
        assertThat(memoTagRepository.findAll()).hasSize(tags.size());
    }

    @MethodSource("provideTags")
    @ParameterizedTest
    void 메모에_존재하는_태그를_삭제할_수_있다(List<Tag> tags) {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);

        List<Long> tagIds = tagRepository.saveAll(tags)
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        Long memoId = memoService.saveMemoWithTags(dailyTagList.getId(), MEMO_TEXT, tagIds);

        // when
        memoService.deleteTagToMemo(memoId, tagIds.get(0));

        // then
        int numberOfMemoTags = tags.size() - 1;
        assertThat(memoTagRepository.findAll()).hasSize(numberOfMemoTags);
        assertThat(memoRepository.existsById(memoId)).isTrue();
    }

    @Test
    void 메모를_삭제할_수_있다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);

        Long memoId = memoService.saveMemo(dailyTagList.getId(), MEMO_TEXT);

        // when
        memoService.deleteMemo(memoId);

        // then
        assertThat(memoRepository.existsById(memoId)).isFalse();
    }

    @MethodSource("provideTags")
    @ParameterizedTest
    void 메모를_삭제하면_메모에_존재하는_태그들도_삭제되어야_한다(List<Tag> tags) {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);

        List<Long> tagIds = tagRepository.saveAll(tags)
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        Long memoId = memoService.saveMemoWithTags(dailyTagList.getId(), MEMO_TEXT, tagIds);

        // when
        memoService.deleteMemo(memoId);

        // then
        assertThat(memoRepository.existsById(memoId)).isFalse();
        assertThat(memoTagRepository.findAll()).isEmpty();
    }

}
