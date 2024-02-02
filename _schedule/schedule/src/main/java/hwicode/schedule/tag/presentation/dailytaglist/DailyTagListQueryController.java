package hwicode.schedule.tag.presentation.dailytaglist;

import hwicode.schedule.common.login.LoginInfo;
import hwicode.schedule.common.login.LoginUser;
import hwicode.schedule.tag.application.query.DailyTagListQueryService;
import hwicode.schedule.tag.application.query.dto.DailyTagListMemoQueryResponse;
import hwicode.schedule.tag.application.query.dto.DailyTagQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
public class DailyTagListQueryController {

    private final DailyTagListQueryService dailyTagListQueryService;

    @GetMapping("/dailyschedule/daily-tag-lists")
    @ResponseStatus(HttpStatus.OK)
    public List<DailyTagQueryResponse> getDailyTagQueryResponses(@LoginUser LoginInfo loginInfo,
                                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return dailyTagListQueryService.getDailyTagQueryResponses(loginInfo.getUserId(), date);
    }

    @GetMapping("/dailyschedule/daily-tag-lists/{dailyTagListId}/memos")
    @ResponseStatus(HttpStatus.OK)
    public List<DailyTagListMemoQueryResponse> getDailyTagListMemoQueryResponses(@LoginUser LoginInfo loginInfo,
                                                                                 @PathVariable @Positive Long dailyTagListId) {
        return dailyTagListQueryService.getDailyTagListMemoQueryResponses(loginInfo.getUserId(), dailyTagListId);
    }

}
