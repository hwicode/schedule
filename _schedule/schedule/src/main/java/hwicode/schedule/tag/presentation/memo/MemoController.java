package hwicode.schedule.tag.presentation.memo;

import hwicode.schedule.common.config.auth.LoginInfo;
import hwicode.schedule.common.config.auth.LoginUser;
import hwicode.schedule.tag.application.MemoService;
import hwicode.schedule.tag.application.dto.memo.*;
import hwicode.schedule.tag.presentation.memo.dto.save.MemoSaveRequest;
import hwicode.schedule.tag.presentation.memo.dto.save.MemoSaveResponse;
import hwicode.schedule.tag.presentation.memo.dto.save_with_tags.MemoSaveWithTagsRequest;
import hwicode.schedule.tag.presentation.memo.dto.save_with_tags.MemoSaveWithTagsResponse;
import hwicode.schedule.tag.presentation.memo.dto.tags_add.MemoTagsAddRequest;
import hwicode.schedule.tag.presentation.memo.dto.tags_add.MemoTagsAddResponse;
import hwicode.schedule.tag.presentation.memo.dto.text_modify.MemoTextModifyRequest;
import hwicode.schedule.tag.presentation.memo.dto.text_modify.MemoTextModifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
public class MemoController {

    private final MemoService memoService;

    @PostMapping("/dailyschedule/memos")
    @ResponseStatus(HttpStatus.CREATED)
    public MemoSaveResponse saveMemo(@LoginUser LoginInfo loginInfo,
                                     @RequestBody @Valid MemoSaveRequest request) {
        MemoSaveCommand command = new MemoSaveCommand(
                loginInfo.getUserId(), request.getDailyTagListId(), request.getText()
        );
        Long memoId = memoService.saveMemo(command);
        return new MemoSaveResponse(command.getDailyTagListId(), memoId, command.getText());
    }

    @PatchMapping("/dailyschedule/memos/{memoId}")
    @ResponseStatus(HttpStatus.OK)
    public MemoTextModifyResponse changeMemoText(@LoginUser LoginInfo loginInfo,
                                                 @PathVariable @Positive Long memoId,
                                                 @RequestBody @Valid MemoTextModifyRequest request) {
        MemoModifyTextCommand command = new MemoModifyTextCommand(loginInfo.getUserId(), memoId, request.getNewText());
        memoService.changeMemoText(command);
        return new MemoTextModifyResponse(memoId, command.getText());
    }

    @PostMapping("/dailyschedule/memos/{memoId}/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public MemoTagsAddResponse addTagsToMemo(@LoginUser LoginInfo loginInfo,
                                             @PathVariable @Positive Long memoId,
                                             @RequestBody @Valid MemoTagsAddRequest request) {
        MemoAddTagsCommand command = new MemoAddTagsCommand(loginInfo.getUserId(), memoId, request.getTagIds());
        memoService.addTagsToMemo(command);
        return new MemoTagsAddResponse(memoId, command.getTagIds());
    }

    @PostMapping("/dailyschedule/memos/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public MemoSaveWithTagsResponse saveMemoWithTags(@LoginUser LoginInfo loginInfo,
                                                     @RequestBody @Valid MemoSaveWithTagsRequest request) {
        MemoSaveWithTagsCommand command = new MemoSaveWithTagsCommand(
                loginInfo.getUserId(), request.getDailyTagListId(), request.getTagIds(), request.getText()
        );
        Long memoId = memoService.saveMemoWithTags(command);
        return new MemoSaveWithTagsResponse(command.getDailyTagListId(), memoId, command.getText(), command.getTagIds());
    }

    @DeleteMapping("/dailyschedule/memos/{memoId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTagToMemo(@LoginUser LoginInfo loginInfo,
                                @PathVariable @Positive Long memoId,
                                @PathVariable @Positive Long tagId) {
        MemoDeleteTagCommand command = new MemoDeleteTagCommand(loginInfo.getUserId(), memoId, tagId);
        memoService.deleteTagToMemo(command);
    }

    @DeleteMapping("/dailyschedule/memos/{memoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMemo(@LoginUser LoginInfo loginInfo,
                           @PathVariable @Positive Long memoId) {
        MemoDeleteCommand command = new MemoDeleteCommand(loginInfo.getUserId(), memoId);
        memoService.deleteMemo(command);
    }

}
