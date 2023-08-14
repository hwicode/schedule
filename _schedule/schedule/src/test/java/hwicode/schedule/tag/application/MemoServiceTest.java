package hwicode.schedule.tag.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.domain.Memo;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.exception.application.MemoNotFoundException;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagListRepository;
import hwicode.schedule.tag.infra.jpa_repository.MemoRepository;
import hwicode.schedule.tag.infra.jpa_repository.MemoTagRepository;
import hwicode.schedule.tag.infra.jpa_repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
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

    @Test
    void 메모에_태그를_여러_개_추가할_수_있다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        Memo memo = new Memo(MEMO_TEXT, dailyTagList);

        dailyTagListRepository.save(dailyTagList);
        memoRepository.save(memo);

        List<Tag> tags = List.of(
                new Tag(TAG_NAME), new Tag(TAG_NAME2), new Tag(TAG_NAME3)
        );
        List<Long> tagIds = tagRepository.saveAll(tags)
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        // when
        memoService.addTagsToMemo(memo.getId(), tagIds);

        // then
        assertThat(memoTagRepository.findAll()).hasSize(tags.size());
    }

    @Test
    void 메모에_존재하는_태그를_삭제할_수_있다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);

        List<Tag> tags = List.of(
                new Tag(TAG_NAME), new Tag(TAG_NAME2), new Tag(TAG_NAME3)
        );
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

    private static Stream<List<Tag>> provideTags() {
        return Stream.of(
                List.of(new Tag(TAG_NAME)),
                List.of(new Tag(TAG_NAME), new Tag(TAG_NAME2)),
                List.of(new Tag(TAG_NAME), new Tag(TAG_NAME2), new Tag(TAG_NAME3))
        );
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

    @Test
    void 메모에_태그를_여러_개_추가할_때_태그의_id가_null이면_아무일도_일어나지_않는다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);

        Long memoId = memoService.saveMemo(dailyTagList.getId(), MEMO_TEXT);

        List<Long> tagIds = new ArrayList<>();
        tagIds.add(null);

        // when
        memoService.addTagsToMemo(memoId, tagIds);

        // then
        assertThat(memoTagRepository.findAll()).isEmpty();
    }

    @Test
    void 메모와_태그를_여러_개를_같이_생성할_때_태그의_id가_null이면_메모만_생성된다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);

        List<Long> tagIds = new ArrayList<>();
        tagIds.add(null);

        // when
        Long memoId = memoService.saveMemoWithTags(dailyTagList.getId(), MEMO_TEXT, tagIds);

        // then
        assertThat(memoRepository.existsById(memoId)).isTrue();
        assertThat(memoTagRepository.findAll()).isEmpty();
    }

    @Test
    void 메모에_태그를_여러_개_추가할_때_tagIds에_null이_존재하면_null인_값은_무시된다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);

        Long memoId = memoService.saveMemo(dailyTagList.getId(), MEMO_TEXT);

        Tag tag = new Tag(TAG_NAME);
        tagRepository.save(tag);

        List<Long> tagIds = new ArrayList<>();
        tagIds.add(null);
        tagIds.add(tag.getId());

        // when
        memoService.addTagsToMemo(memoId, tagIds);

        // then
        assertThat(memoRepository.existsById(memoId)).isTrue();
        assertThat(memoTagRepository.findAll()).hasSize(1);
    }

    @Test
    void 메모와_태그를_여러_개를_같이_생성할_때_tagIds에_null이_존재하면_null인_값은_무시된다() {
        // given
        DailyTagList dailyTagList = new DailyTagList();
        dailyTagListRepository.save(dailyTagList);

        Tag tag = new Tag(TAG_NAME);
        tagRepository.save(tag);

        List<Long> tagIds = new ArrayList<>();
        tagIds.add(null);
        tagIds.add(tag.getId());

        // when
        Long memoId = memoService.saveMemoWithTags(dailyTagList.getId(), MEMO_TEXT, tagIds);

        // then
        assertThat(memoRepository.existsById(memoId)).isTrue();
        assertThat(memoTagRepository.findAll()).hasSize(1);
    }

}
