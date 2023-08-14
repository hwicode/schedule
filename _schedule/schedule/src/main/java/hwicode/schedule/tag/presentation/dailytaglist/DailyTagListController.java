package hwicode.schedule.tag.presentation.dailytaglist;

import hwicode.schedule.tag.application.DailyTagListService;
import hwicode.schedule.tag.presentation.dailytaglist.dto.tag_add.DailyTagListTagAddRequest;
import hwicode.schedule.tag.presentation.dailytaglist.dto.tag_add.DailyTagListTagAddResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
public class DailyTagListController {

    private final DailyTagListService dailyTagListService;

    @PostMapping("/dailyschedule/daily-tag-lists/{dailyTagListId}/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public DailyTagListTagAddResponse addTagToDailyTagList(@PathVariable @Positive Long dailyTagListId,
                                                           @RequestBody @Valid DailyTagListTagAddRequest dailyTagListTagAddRequest) {
        dailyTagListService.addTagToDailyTagList(dailyTagListId, dailyTagListTagAddRequest.getTagId());
        return new DailyTagListTagAddResponse(dailyTagListId, dailyTagListTagAddRequest.getTagId());
    }

}
