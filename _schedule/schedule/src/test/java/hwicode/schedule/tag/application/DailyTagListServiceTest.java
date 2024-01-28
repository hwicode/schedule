package hwicode.schedule.tag.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.tag.application.dto.daily_tag_list.DailyTagListDeleteTagCommand;
import hwicode.schedule.tag.application.dto.daily_tag_list.DailyTagListModifyMainTagCommand;
import hwicode.schedule.tag.application.dto.daily_tag_list.DailyTagListSaveTagCommand;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.exception.domain.dailytaglist.DailyTagListForbiddenException;
import hwicode.schedule.tag.exception.domain.dailytaglist.DailyTagNotFoundException;
import hwicode.schedule.tag.exception.domain.tag.TagForbiddenException;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagListRepository;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagRepository;
import hwicode.schedule.tag.infra.jpa_repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static hwicode.schedule.tag.TagDataHelper.TAG_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class DailyTagListServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyTagListService dailyTagListService;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    DailyTagListRepository dailyTagListRepository;

    @Autowired
    DailyTagRepository dailyTagRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void DailyTagList에_태그를_추가할_수_있다() {
        // given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, userId);
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);

        tagRepository.save(tag);
        dailyTagListRepository.save(dailyTagList);

        DailyTagListSaveTagCommand command = new DailyTagListSaveTagCommand(userId, dailyTagList.getId(), tag.getId());

        // when
        Long dailyTagId = dailyTagListService.addTagToDailyTagList(command);

        // then
        assertThat(dailyTagRepository.existsById(dailyTagId)).isTrue();
    }

    @Test
    void DailyTagList에_태그를_추가할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, userId);
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);

        tagRepository.save(tag);
        dailyTagListRepository.save(dailyTagList);

        DailyTagListSaveTagCommand command = new DailyTagListSaveTagCommand(2L, dailyTagList.getId(), tag.getId());

        // when then
        assertThatThrownBy(() -> dailyTagListService.addTagToDailyTagList(command))
                .isInstanceOf(TagForbiddenException.class);
    }

    @Test
    void DailyTagList에_태그를_추가할_때_DailyTagList의_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, userId);
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), 2L);

        tagRepository.save(tag);
        dailyTagListRepository.save(dailyTagList);

        DailyTagListSaveTagCommand command = new DailyTagListSaveTagCommand(userId, dailyTagList.getId(), tag.getId());

        // when then
        assertThatThrownBy(() -> dailyTagListService.addTagToDailyTagList(command))
                .isInstanceOf(DailyTagListForbiddenException.class);
    }

    @Test
    void DailyTagList에_태그를_추가할_때_태그의_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, 2L);
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);

        tagRepository.save(tag);
        dailyTagListRepository.save(dailyTagList);

        DailyTagListSaveTagCommand command = new DailyTagListSaveTagCommand(userId, dailyTagList.getId(), tag.getId());

        // when then
        assertThatThrownBy(() -> dailyTagListService.addTagToDailyTagList(command))
                .isInstanceOf(TagForbiddenException.class);
    }

    @Test
    void DailyTagList에_태그를_삭제할_수_있다() {
        // given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, userId);
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);

        tagRepository.save(tag);
        dailyTagListRepository.save(dailyTagList);

        DailyTagListSaveTagCommand saveCommand = new DailyTagListSaveTagCommand(userId, dailyTagList.getId(), tag.getId());

        Long dailyTagId = dailyTagListService.addTagToDailyTagList(saveCommand);

        DailyTagListDeleteTagCommand command = new DailyTagListDeleteTagCommand(userId, dailyTagId, tag.getId());

        // when
        dailyTagListService.deleteTagToDailyTagList(command);

        // then
        assertThatThrownBy(() -> dailyTagListService.deleteTagToDailyTagList(command))
                .isInstanceOf(DailyTagNotFoundException.class);
        assertThat(dailyTagRepository.existsById(dailyTagId)).isFalse();
    }

    @Test
    void DailyTagList에_태그를_삭제할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, userId);
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);

        tagRepository.save(tag);
        dailyTagListRepository.save(dailyTagList);

        DailyTagListSaveTagCommand saveCommand = new DailyTagListSaveTagCommand(userId, dailyTagList.getId(), tag.getId());

        Long dailyTagId = dailyTagListService.addTagToDailyTagList(saveCommand);

        DailyTagListDeleteTagCommand command = new DailyTagListDeleteTagCommand(2L, dailyTagId, tag.getId());

        // when then
        assertThatThrownBy(() -> dailyTagListService.deleteTagToDailyTagList(command))
                .isInstanceOf(TagForbiddenException.class);
    }

    @Test
    void DailyTagList에_태그를_삭제할_때_DailyTagList의_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, userId);
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), 2L);

        tagRepository.save(tag);
        dailyTagListRepository.save(dailyTagList);

        DailyTagListDeleteTagCommand command = new DailyTagListDeleteTagCommand(userId, dailyTagList.getId(), tag.getId());

        // when then
        assertThatThrownBy(() -> dailyTagListService.deleteTagToDailyTagList(command))
                .isInstanceOf(DailyTagListForbiddenException.class);
    }

    @Test
    void DailyTagList에_태그를_삭제할_때_태그의_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, 2L);
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);

        tagRepository.save(tag);
        dailyTagListRepository.save(dailyTagList);

        DailyTagListDeleteTagCommand command = new DailyTagListDeleteTagCommand(userId, dailyTagList.getId(), tag.getId());

        // when then
        assertThatThrownBy(() -> dailyTagListService.deleteTagToDailyTagList(command))
                .isInstanceOf(TagForbiddenException.class);
    }

    @Test
    void DailyTagList에_메인_태그를_변경할_수_있다() {
        // given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, userId);
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);

        tagRepository.save(tag);
        dailyTagListRepository.save(dailyTagList);

        DailyTagListSaveTagCommand saveCommand = new DailyTagListSaveTagCommand(userId, dailyTagList.getId(), tag.getId());

        dailyTagListService.addTagToDailyTagList(saveCommand);

        DailyTagListModifyMainTagCommand command = new DailyTagListModifyMainTagCommand(userId, dailyTagList.getId(), tag.getId());

        // when
        String mainTagName = dailyTagListService.changeMainTag(command);

        // then
        assertThat(mainTagName).isEqualTo(TAG_NAME);
    }

    @Test
    void DailyTagList에_메인_태그를_변경할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, userId);
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);

        tagRepository.save(tag);
        dailyTagListRepository.save(dailyTagList);

        DailyTagListSaveTagCommand saveCommand = new DailyTagListSaveTagCommand(userId, dailyTagList.getId(), tag.getId());

        dailyTagListService.addTagToDailyTagList(saveCommand);

        DailyTagListModifyMainTagCommand command = new DailyTagListModifyMainTagCommand(2L, dailyTagList.getId(), tag.getId());

        // when then
        assertThatThrownBy(() -> dailyTagListService.changeMainTag(command))
                .isInstanceOf(TagForbiddenException.class);
    }

    @Test
    void DailyTagList에_메인_태그를_변경할_때_DailyTagList의_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, userId);
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), 2L);

        tagRepository.save(tag);
        dailyTagListRepository.save(dailyTagList);

        DailyTagListModifyMainTagCommand command = new DailyTagListModifyMainTagCommand(userId, dailyTagList.getId(), tag.getId());

        // when then
        assertThatThrownBy(() -> dailyTagListService.changeMainTag(command))
                .isInstanceOf(DailyTagListForbiddenException.class);
    }

    @Test
    void DailyTagList에_메인_태그를_변경할_때_태그의_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        Tag tag = new Tag(TAG_NAME, 2L);
        DailyTagList dailyTagList = new DailyTagList(LocalDate.now(), userId);

        tagRepository.save(tag);
        dailyTagListRepository.save(dailyTagList);

        DailyTagListModifyMainTagCommand command = new DailyTagListModifyMainTagCommand(userId, dailyTagList.getId(), tag.getId());

        // when then
        assertThatThrownBy(() -> dailyTagListService.changeMainTag(command))
                .isInstanceOf(TagForbiddenException.class);
    }

}
