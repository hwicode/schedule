package hwicode.schedule.tag.presentation.memo;

import hwicode.schedule.tag.application.MemoService;
import hwicode.schedule.tag.presentation.memo.dto.save.MemoSaveRequest;
import hwicode.schedule.tag.presentation.memo.dto.save.MemoSaveResponse;
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

    @PostMapping("/dailyschedule/daily-tag-lists/{dailyTagListId}/memos")
    @ResponseStatus(HttpStatus.CREATED)
    public MemoSaveResponse saveMemo(@PathVariable @Positive Long dailyTagListId,
                                     @RequestBody @Valid MemoSaveRequest memoSaveRequest) {
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

}
