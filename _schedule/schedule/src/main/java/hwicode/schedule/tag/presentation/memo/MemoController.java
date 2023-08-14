package hwicode.schedule.tag.presentation.memo;

import hwicode.schedule.tag.application.MemoService;
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
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
public class MemoController {

    private final MemoService memoService;

    @PostMapping("/dailyschedule/memos")
    @ResponseStatus(HttpStatus.CREATED)
    public MemoSaveResponse saveMemo(@RequestBody @Valid MemoSaveRequest memoSaveRequest) {
        Long dailyTagListId = memoSaveRequest.getDailyTagListId();
        String text = memoSaveRequest.getText();
        Long memoId = memoService.saveMemo(dailyTagListId, text);
        return new MemoSaveResponse(dailyTagListId, memoId, text);
    }

    @PatchMapping("/dailyschedule/memos/{memoId}")
    @ResponseStatus(HttpStatus.OK)
    public MemoTextModifyResponse changeMemoText(@PathVariable @Positive Long memoId,
                                                 @RequestBody @Valid MemoTextModifyRequest memoTextModifyRequest) {
        memoService.changeMemoText(memoId, memoTextModifyRequest.getNewText());
        return new MemoTextModifyResponse(memoId, memoTextModifyRequest.getNewText());
    }

    @PostMapping("/dailyschedule/memos/{memoId}/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public MemoTagsAddResponse addTagsToMemo(@PathVariable @Positive Long memoId,
                                             @RequestBody @Valid MemoTagsAddRequest memoTagsAddRequest) {
        List<Long> tagIds = memoTagsAddRequest.getTagIds();
        memoService.addTagsToMemo(memoId, tagIds);
        return new MemoTagsAddResponse(memoId, tagIds);
    }

    @PostMapping("/dailyschedule/memos/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public MemoSaveWithTagsResponse saveMemoWithTags(@RequestBody @Valid MemoSaveWithTagsRequest memoSaveWithTagsRequest) {
        Long dailyTagListId = memoSaveWithTagsRequest.getDailyTagListId();
        List<Long> tagIds = memoSaveWithTagsRequest.getTagIds();
        Long memoId = memoService.saveMemoWithTags(dailyTagListId, memoSaveWithTagsRequest.getText(), tagIds);
        return new MemoSaveWithTagsResponse(dailyTagListId, memoId, memoSaveWithTagsRequest.getText(), tagIds);
    }

    @DeleteMapping("/dailyschedule/memos/{memoId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTagToMemo(@PathVariable @Positive Long memoId,
                                @PathVariable @Positive Long tagId) {
        memoService.deleteTagToMemo(memoId, tagId);
    }

    @DeleteMapping("/dailyschedule/memos/{memoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMemo(@PathVariable @Positive Long memoId) {
        memoService.deleteMemo(memoId);
    }

}
