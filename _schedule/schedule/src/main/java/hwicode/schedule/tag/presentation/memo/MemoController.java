package hwicode.schedule.tag.presentation.memo;

import hwicode.schedule.tag.application.MemoService;
import hwicode.schedule.tag.presentation.memo.dto.save.MemoSaveRequest;
import hwicode.schedule.tag.presentation.memo.dto.save.MemoSaveResponse;
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

    @PostMapping("/dailyschedule/daily-tag-lists/{dailyTagListId}/memos")
    @ResponseStatus(HttpStatus.CREATED)
    public MemoSaveResponse saveMemo(@PathVariable @Positive Long dailyTagListId,
                                     @RequestBody @Valid MemoSaveRequest memoSaveRequest) {
        String text = memoSaveRequest.getText();
        Long memoId = memoService.saveMemo(dailyTagListId, text);
        return new MemoSaveResponse(dailyTagListId, memoId, text);
    }

}
