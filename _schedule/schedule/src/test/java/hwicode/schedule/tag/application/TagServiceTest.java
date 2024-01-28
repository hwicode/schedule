package hwicode.schedule.tag.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.tag.application.dto.daily_tag_list.DailyTagListSaveTagCommand;
import hwicode.schedule.tag.application.dto.memo.MemoAddTagsCommand;
import hwicode.schedule.tag.application.dto.tag.TagDeleteCommand;
import hwicode.schedule.tag.application.dto.tag.TagModifyNameCommand;
import hwicode.schedule.tag.application.dto.tag.TagSaveCommand;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.domain.Memo;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.exception.application.TagDuplicateException;
import hwicode.schedule.tag.exception.application.TagNotFoundException;
import hwicode.schedule.tag.exception.domain.tag.TagForbiddenException;
import hwicode.schedule.tag.infra.jpa_repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
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
    DailyTagListService dailyTagListService;

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
        // given
        Long userId = 1L;
        TagSaveCommand command = new TagSaveCommand(userId, TAG_NAME);

        // when
        Long tagId = tagService.saveTag(command);

        // then
        assertThat(tagRepository.existsById(tagId)).isTrue();
    }

    @Test
    void 태그를_생성할_때_이름이_중복되면_에러가_발생한다() {
        // given
        Long userId = 1L;
        TagSaveCommand command = new TagSaveCommand(userId, TAG_NAME);

        tagService.saveTag(command);

        // when then
        assertThatThrownBy(() -> tagService.saveTag(command))
                .isInstanceOf(TagDuplicateException.class);
    }

    @Test
    void 태그의_이름을_변경할_수_있다() {
        // given
        Long userId = 1L;
        TagSaveCommand saveCommand = new TagSaveCommand(userId, TAG_NAME);

        Long tagId = tagService.saveTag(saveCommand);

        TagModifyNameCommand command = new TagModifyNameCommand(userId, tagId, NEW_TAG_NAME);

        // when
        String changedTagName = tagService.changeTagName(command);

        // then
        Tag tag = tagRepository.findById(tagId).orElseThrow();
        assertThat(tag.changeName(changedTagName)).isFalse();
    }

    @Test
    void 태그의_이름을_변경할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        TagSaveCommand saveCommand = new TagSaveCommand(userId, TAG_NAME);

        Long tagId = tagService.saveTag(saveCommand);

        TagModifyNameCommand command = new TagModifyNameCommand(2L, tagId, NEW_TAG_NAME);

        // when then
        assertThatThrownBy(() -> tagService.changeTagName(command))
                .isInstanceOf(TagForbiddenException.class);
    }

    @Test
    void 태그를_삭제할_수_있다() {
        // given
        Long userId = 1L;
        TagSaveCommand saveCommand = new TagSaveCommand(userId, TAG_NAME);

        Long tagId = tagService.saveTag(saveCommand);

        TagDeleteCommand command = new TagDeleteCommand(userId, tagId);

        // when
        tagService.deleteTag(command);

        // then
        assertThatThrownBy(() -> tagService.deleteTag(command))
                .isInstanceOf(TagNotFoundException.class);
    }

    @Test
    void 태그를_삭제할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        TagSaveCommand saveCommand = new TagSaveCommand(userId, TAG_NAME);

        Long tagId = tagService.saveTag(saveCommand);

        TagDeleteCommand command = new TagDeleteCommand(2L, tagId);

        // when then
        assertThatThrownBy(() -> tagService.deleteTag(command))
                .isInstanceOf(TagForbiddenException.class);
    }

    private static Stream<List<DailyTagList>> provideDailyTagLists() {
        return Stream.of(
                List.of(new DailyTagList(LocalDate.now(), 1L)),
                List.of(new DailyTagList(LocalDate.now(), 1L), new DailyTagList(LocalDate.now(), 1L)),
                List.of(new DailyTagList(LocalDate.now(), 1L), new DailyTagList(LocalDate.now(), 1L), new DailyTagList(LocalDate.now(), 1L)),
                List.of(new DailyTagList(LocalDate.now(), 1L), new DailyTagList(LocalDate.now(), 1L), new DailyTagList(LocalDate.now(), 1L), new DailyTagList(LocalDate.now(), 1L))
        );
    }

    @ParameterizedTest
    @MethodSource("provideDailyTagLists")
    void 태그를_삭제할_때_DailyTag도_같이_삭제할_수_있다(List<DailyTagList> dailyTagLists) {
        // given
        Long userId = 1L;
        TagSaveCommand saveCommand = new TagSaveCommand(userId, TAG_NAME);

        Long tagId = tagService.saveTag(saveCommand);

        dailyTagListRepository.saveAll(dailyTagLists);
        dailyTagLists.forEach(
                dailyTagList -> dailyTagListService.addTagToDailyTagList(
                        new DailyTagListSaveTagCommand(userId, dailyTagList.getId(), tagId)
                )
        );

        TagDeleteCommand command = new TagDeleteCommand(userId, tagId);

        // when
        tagService.deleteTag(command);

        // then
        assertThatThrownBy(() -> tagService.deleteTag(command))
                .isInstanceOf(TagNotFoundException.class);
        assertThat(tagRepository.findAll()).isEmpty();
        assertThat(dailyTagRepository.findAll()).isEmpty();
    }

    @Test
    void 태그를_삭제할_때_MemoTag도_같이_삭제할_수_있다() {
        // given
        Long userId = 1L;
        TagSaveCommand saveCommand = new TagSaveCommand(userId, TAG_NAME);

        Long tagId = tagService.saveTag(saveCommand);

        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        dailyTagListRepository.save(dailyTagList);

        Memo memo = dailyTagList.createMemo(MEMO_TEXT);
        Memo memo2 = dailyTagList.createMemo(MEMO_TEXT2);
        memoRepository.save(memo);
        memoRepository.save(memo2);

        memoService.addTagsToMemo(
                new MemoAddTagsCommand(userId, memo.getId(), List.of(tagId))
        );
        memoService.addTagsToMemo(
                new MemoAddTagsCommand(userId, memo2.getId(), List.of(tagId))
        );

        TagDeleteCommand command = new TagDeleteCommand(userId, tagId);

        // when
        tagService.deleteTag(command);

        // then
        assertThatThrownBy(() -> tagService.deleteTag(command))
                .isInstanceOf(TagNotFoundException.class);
        assertThat(tagRepository.findAll()).isEmpty();
        assertThat(memoTagRepository.findAll()).isEmpty();
    }

    @Test
    void 태그를_삭제할_때_DailyTag와_MemoTag도_같이_삭제할_수_있다() {
        // given
        Long userId = 1L;
        TagSaveCommand saveCommand = new TagSaveCommand(userId, TAG_NAME);

        Long tagId = tagService.saveTag(saveCommand);

        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);
        DailyTagList dailyTagList2 = new DailyTagList(LocalDate.now(), userId);
        dailyTagListRepository.save(dailyTagList);
        dailyTagListRepository.save(dailyTagList2);

        dailyTagListService.addTagToDailyTagList(
                new DailyTagListSaveTagCommand(userId, dailyTagList.getId(), tagId)
        );
        dailyTagListService.addTagToDailyTagList(
                new DailyTagListSaveTagCommand(userId, dailyTagList2.getId(), tagId)
        );

        Memo memo = dailyTagList.createMemo(MEMO_TEXT);
        Memo memo2 = dailyTagList.createMemo(MEMO_TEXT2);
        memoRepository.save(memo);
        memoRepository.save(memo2);

        memoService.addTagsToMemo(
                new MemoAddTagsCommand(userId, memo.getId(), List.of(tagId))
        );
        memoService.addTagsToMemo(
                new MemoAddTagsCommand(userId, memo2.getId(), List.of(tagId))
        );

        TagDeleteCommand command = new TagDeleteCommand(userId, tagId);

        // when
        tagService.deleteTag(command);

        // then
        assertThatThrownBy(() -> tagService.deleteTag(command))
                .isInstanceOf(TagNotFoundException.class);
        assertThat(tagRepository.findAll()).isEmpty();
        assertThat(dailyTagRepository.findAll()).isEmpty();
        assertThat(memoTagRepository.findAll()).isEmpty();
    }

}
