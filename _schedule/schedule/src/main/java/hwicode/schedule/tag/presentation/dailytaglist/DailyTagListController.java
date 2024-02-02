package hwicode.schedule.tag.presentation.dailytaglist;

import hwicode.schedule.common.login.LoginInfo;
import hwicode.schedule.common.login.LoginUser;
import hwicode.schedule.tag.application.DailyTagListService;
import hwicode.schedule.tag.application.dto.daily_tag_list.DailyTagListDeleteTagCommand;
import hwicode.schedule.tag.application.dto.daily_tag_list.DailyTagListModifyMainTagCommand;
import hwicode.schedule.tag.application.dto.daily_tag_list.DailyTagListSaveTagCommand;
import hwicode.schedule.tag.presentation.dailytaglist.dto.main_tag_modify.DailyTagListMainTagModifyResponse;
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
    public DailyTagListTagAddResponse addTagToDailyTagList(@LoginUser LoginInfo loginInfo,
                                                           @PathVariable @Positive Long dailyTagListId,
                                                           @RequestBody @Valid DailyTagListTagAddRequest request) {
        DailyTagListSaveTagCommand command = new DailyTagListSaveTagCommand(
                loginInfo.getUserId(), dailyTagListId, request.getTagId()
        );
        dailyTagListService.addTagToDailyTagList(command);
        return new DailyTagListTagAddResponse(dailyTagListId, command.getTagId());
    }

    @DeleteMapping("/dailyschedule/daily-tag-lists/{dailyTagListId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTagToDailyTagList(@LoginUser LoginInfo loginInfo,
                                        @PathVariable @Positive Long dailyTagListId,
                                        @PathVariable @Positive Long tagId) {
        DailyTagListDeleteTagCommand command = new DailyTagListDeleteTagCommand(
                loginInfo.getUserId(), dailyTagListId, tagId
        );
        dailyTagListService.deleteTagToDailyTagList(command);
    }

    @PatchMapping("/dailyschedule/daily-tag-lists/{dailyTagListId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    public DailyTagListMainTagModifyResponse changeMainTag(@LoginUser LoginInfo loginInfo,
                                                           @PathVariable @Positive Long dailyTagListId,
                                                           @PathVariable @Positive Long tagId) {
        DailyTagListModifyMainTagCommand command = new DailyTagListModifyMainTagCommand(
                loginInfo.getUserId(), dailyTagListId, tagId
        );
        String mainTagName = dailyTagListService.changeMainTag(command);
        return new DailyTagListMainTagModifyResponse(dailyTagListId, tagId, mainTagName);
    }

}
