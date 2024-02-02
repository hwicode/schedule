package hwicode.schedule.tag.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.common.login.validator.OwnerForbiddenException;
import hwicode.schedule.tag.application.dto.memo.*;
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

import java.time.LocalDate;
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
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        dailyTagListRepository.save(dailyTagList);

        MemoSaveCommand command = new MemoSaveCommand(userId, dailyTagList.getId(), MEMO_TEXT);

        // when
        Long memoId = memoService.saveMemo(command);

        // then
        assertThat(memoRepository.existsById(memoId)).isTrue();
    }

    @Test
    void 메모를_생성할_때_DailyTagList의_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        dailyTagListRepository.save(dailyTagList);

        MemoSaveCommand command = new MemoSaveCommand(2L, dailyTagList.getId(), MEMO_TEXT);

        // when then
        assertThatThrownBy(() -> memoService.saveMemo(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

    @Test
    void 메모의_내용를_변경할_수_있다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        Memo memo = dailyTagList.createMemo(MEMO_TEXT);

        dailyTagListRepository.save(dailyTagList);
        memoRepository.save(memo);

        MemoModifyTextCommand command = new MemoModifyTextCommand(userId, memo.getId(), NEW_MEMO_TEXT);

        // when
        String changedText = memoService.changeMemoText(command);

        // then
        Memo savedMemo = memoRepository.findById(memo.getId()).orElseThrow();
        assertThat(savedMemo.changeText(changedText)).isFalse();
    }

    @Test
    void 메모의_내용를_변경할_때_메모의_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        Memo memo = dailyTagList.createMemo(MEMO_TEXT);

        dailyTagListRepository.save(dailyTagList);
        memoRepository.save(memo);

        MemoModifyTextCommand command = new MemoModifyTextCommand(2L, memo.getId(), NEW_MEMO_TEXT);

        // when then
        assertThatThrownBy(() -> memoService.changeMemoText(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

    @Test
    void 존재하지_않는_메모를_조회하면_에러가_발생한다() {
        // given
        Long userId = 1L;
        Long noneExistId = 1L;

        MemoModifyTextCommand command = new MemoModifyTextCommand(userId, noneExistId, MEMO_TEXT);

        // when then
        assertThatThrownBy(() -> memoService.changeMemoText(command))
                .isInstanceOf(MemoNotFoundException.class);
    }

    @Test
    void 메모에_태그를_여러_개_추가할_수_있다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        Memo memo = dailyTagList.createMemo(MEMO_TEXT);

        dailyTagListRepository.save(dailyTagList);
        memoRepository.save(memo);

        List<Tag> tags = List.of(
                new Tag(TAG_NAME, userId), new Tag(TAG_NAME2, userId), new Tag(TAG_NAME3, userId)
        );
        List<Long> tagIds = tagRepository.saveAll(tags)
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        MemoAddTagsCommand command = new MemoAddTagsCommand(userId, memo.getId(), tagIds);

        // when
        memoService.addTagsToMemo(command);

        // then
        assertThat(memoTagRepository.findAll()).hasSize(tags.size());
    }

    @Test
    void 메모에_태그를_여러_개_추가할_때_메모의_소유자가_아니라면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        Memo memo = dailyTagList.createMemo(MEMO_TEXT);

        dailyTagListRepository.save(dailyTagList);
        memoRepository.save(memo);

        List<Tag> tags = List.of(
                new Tag(TAG_NAME, userId), new Tag(TAG_NAME2, userId), new Tag(TAG_NAME3, userId)
        );
        List<Long> tagIds = tagRepository.saveAll(tags)
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        MemoAddTagsCommand command = new MemoAddTagsCommand(2L, memo.getId(), tagIds);

        // when then
        assertThatThrownBy(() -> memoService.addTagsToMemo(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

    @Test
    void 메모에_태그를_여러_개_추가할_때_태그의_소유자가_아니라면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        Memo memo = dailyTagList.createMemo(MEMO_TEXT);

        dailyTagListRepository.save(dailyTagList);
        memoRepository.save(memo);

        List<Tag> tags = List.of(
                new Tag(TAG_NAME, 2L), new Tag(TAG_NAME2, 2L), new Tag(TAG_NAME3, 2L)
        );
        List<Long> tagIds = tagRepository.saveAll(tags)
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        MemoAddTagsCommand command = new MemoAddTagsCommand(userId, memo.getId(), tagIds);

        // when then
        assertThatThrownBy(() -> memoService.addTagsToMemo(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

    @Test
    void 메모에_존재하는_태그를_삭제할_수_있다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        dailyTagListRepository.save(dailyTagList);

        List<Tag> tags = List.of(
                new Tag(TAG_NAME, userId), new Tag(TAG_NAME2, userId), new Tag(TAG_NAME3, userId)
        );
        List<Long> tagIds = tagRepository.saveAll(tags)
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        Long memoId = memoService.saveMemoWithTags(
                new MemoSaveWithTagsCommand(userId, dailyTagList.getId(), tagIds, MEMO_TEXT)
        );

        MemoDeleteTagCommand command = new MemoDeleteTagCommand(userId, memoId, tagIds.get(0));

        // when
        memoService.deleteTagToMemo(command);

        // then
        int numberOfMemoTags = tags.size() - 1;
        assertThat(memoTagRepository.findAll()).hasSize(numberOfMemoTags);
        assertThat(memoRepository.existsById(memoId)).isTrue();
    }

    @Test
    void 메모에_존재하는_태그를_삭제할_때_메모의_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        dailyTagListRepository.save(dailyTagList);

        List<Tag> tags = List.of(
                new Tag(TAG_NAME, userId), new Tag(TAG_NAME2, userId), new Tag(TAG_NAME3, userId)
        );
        List<Long> tagIds = tagRepository.saveAll(tags)
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        Long memoId = memoService.saveMemoWithTags(
                new MemoSaveWithTagsCommand(userId, dailyTagList.getId(), tagIds, MEMO_TEXT)
        );

        MemoDeleteTagCommand command = new MemoDeleteTagCommand(2L, memoId, tagIds.get(0));

        // when then
        assertThatThrownBy(() -> memoService.deleteTagToMemo(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

    @Test
    void 메모에_존재하는_태그를_삭제할_때_태그의_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        Memo memo = dailyTagList.createMemo(MEMO_TEXT);

        dailyTagListRepository.save(dailyTagList);
        memoRepository.save(memo);

        Tag tag = new Tag(TAG_NAME, 2L);
        tagRepository.save(tag);

        MemoDeleteTagCommand command = new MemoDeleteTagCommand(userId, memo.getId(), tag.getId());

        // when then
        assertThatThrownBy(() -> memoService.deleteTagToMemo(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

    @Test
    void 메모를_삭제할_수_있다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        Memo memo = dailyTagList.createMemo(MEMO_TEXT);

        dailyTagListRepository.save(dailyTagList);
        memoRepository.save(memo);

        MemoDeleteCommand command = new MemoDeleteCommand(userId, memo.getId());

        // when
        memoService.deleteMemo(command);

        // then
        assertThat(memoRepository.existsById(memo.getId())).isFalse();
    }

    @Test
    void 메모를_삭제할_때_메모의_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        Memo memo = dailyTagList.createMemo(MEMO_TEXT);

        dailyTagListRepository.save(dailyTagList);
        memoRepository.save(memo);

        MemoDeleteCommand command = new MemoDeleteCommand(2L, memo.getId());

        // when then
        assertThatThrownBy(() -> memoService.deleteMemo(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

    private static Stream<List<Tag>> provideTags() {
        return Stream.of(
                List.of(new Tag(TAG_NAME, 1L)),
                List.of(new Tag(TAG_NAME, 1L), new Tag(TAG_NAME2, 1L)),
                List.of(new Tag(TAG_NAME, 1L), new Tag(TAG_NAME2, 1L), new Tag(TAG_NAME3, 1L))
        );
    }

    @MethodSource({"provideTags"})
    @ParameterizedTest
    void 메모와_태그를_여러_개를_같이_생성할_수_있다(List<Tag> tags) {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        dailyTagListRepository.save(dailyTagList);

        List<Long> tagIds = tagRepository.saveAll(tags)
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        MemoSaveWithTagsCommand command = new MemoSaveWithTagsCommand(userId, dailyTagList.getId(), tagIds, MEMO_TEXT);

        // when
        Long memoId = memoService.saveMemoWithTags(command);

        // then
        assertThat(memoRepository.existsById(memoId)).isTrue();
        assertThat(memoTagRepository.findAll()).hasSize(tags.size());
    }

    @MethodSource("provideTags")
    @ParameterizedTest
    void 메모를_삭제하면_메모에_존재하는_태그들도_삭제되어야_한다(List<Tag> tags) {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        dailyTagListRepository.save(dailyTagList);

        List<Long> tagIds = tagRepository.saveAll(tags)
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        Long memoId = memoService.saveMemoWithTags(
                new MemoSaveWithTagsCommand(userId, dailyTagList.getId(), tagIds, MEMO_TEXT)
        );

        MemoDeleteCommand command = new MemoDeleteCommand(userId, memoId);

        // when
        memoService.deleteMemo(command);

        // then
        assertThat(memoRepository.existsById(memoId)).isFalse();
        assertThat(memoTagRepository.findAll()).isEmpty();
    }

    @Test
    void 메모에_태그를_여러_개_추가할_때_태그의_id가_null이면_아무일도_일어나지_않는다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        Memo memo = dailyTagList.createMemo(MEMO_TEXT);

        dailyTagListRepository.save(dailyTagList);
        memoRepository.save(memo);

        List<Long> tagIds = new ArrayList<>();
        tagIds.add(null);

        MemoAddTagsCommand command = new MemoAddTagsCommand(userId, memo.getId(), tagIds);

        // when
        memoService.addTagsToMemo(command);

        // then
        assertThat(memoTagRepository.findAll()).isEmpty();
    }

    @Test
    void 메모와_태그를_여러_개를_같이_생성할_때_DailyTagList의_소유자가_아니라면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        dailyTagListRepository.save(dailyTagList);

        List<Long> tagIds = new ArrayList<>();
        tagIds.add(null);

        MemoSaveWithTagsCommand command = new MemoSaveWithTagsCommand(2L, dailyTagList.getId(), tagIds, MEMO_TEXT);

        // when then
        assertThatThrownBy(() -> memoService.saveMemoWithTags(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

    @Test
    void 메모와_태그를_여러_개를_같이_생성할_때_태그의_소유자가_아니라면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        dailyTagListRepository.save(dailyTagList);

        List<Tag> tags = List.of(
                new Tag(TAG_NAME, 2L), new Tag(TAG_NAME2, 2L), new Tag(TAG_NAME3, 2L)
        );

        List<Long> tagIds = tagRepository.saveAll(tags)
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        MemoSaveWithTagsCommand command = new MemoSaveWithTagsCommand(userId, dailyTagList.getId(), tagIds, MEMO_TEXT);

        // when then
        assertThatThrownBy(() -> memoService.saveMemoWithTags(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

    @Test
    void 메모와_태그를_여러_개를_같이_생성할_때_태그의_id가_null이면_메모만_생성된다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        dailyTagListRepository.save(dailyTagList);

        List<Long> tagIds = new ArrayList<>();
        tagIds.add(null);

        MemoSaveWithTagsCommand command = new MemoSaveWithTagsCommand(userId, dailyTagList.getId(), tagIds, MEMO_TEXT);

        // when
        Long memoId = memoService.saveMemoWithTags(command);

        // then
        assertThat(memoRepository.existsById(memoId)).isTrue();
        assertThat(memoTagRepository.findAll()).isEmpty();
    }

    @Test
    void 메모에_태그를_여러_개_추가할_때_tagIds에_null이_존재하면_null인_값은_무시된다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        Memo memo = dailyTagList.createMemo(MEMO_TEXT);

        dailyTagListRepository.save(dailyTagList);
        memoRepository.save(memo);

        Tag tag = new Tag(TAG_NAME, userId);
        tagRepository.save(tag);

        List<Long> tagIds = new ArrayList<>();
        tagIds.add(null);
        tagIds.add(tag.getId());

        MemoAddTagsCommand command = new MemoAddTagsCommand(userId, memo.getId(), tagIds);

        // when
        memoService.addTagsToMemo(command);

        // then
        assertThat(memoRepository.existsById(memo.getId())).isTrue();
        assertThat(memoTagRepository.findAll()).hasSize(1);
    }

    @Test
    void 메모와_태그를_여러_개를_같이_생성할_때_tagIds에_null이_존재하면_null인_값은_무시된다() {
        // given
        Long userId = 1L;
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        dailyTagListRepository.save(dailyTagList);

        Tag tag = new Tag(TAG_NAME, userId);
        tagRepository.save(tag);

        List<Long> tagIds = new ArrayList<>();
        tagIds.add(null);
        tagIds.add(tag.getId());

        MemoSaveWithTagsCommand command = new MemoSaveWithTagsCommand(userId, dailyTagList.getId(), tagIds, MEMO_TEXT);

        // when
        Long memoId = memoService.saveMemoWithTags(command);

        // then
        assertThat(memoRepository.existsById(memoId)).isTrue();
        assertThat(memoTagRepository.findAll()).hasSize(1);
    }

}
