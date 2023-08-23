package hwicode.schedule.dailyschedule.daily_schedule_query.presentation;

import hwicode.schedule.dailyschedule.daily_schedule_query.application.DailyScheduleQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.YearMonth;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DailyScheduleQueryController.class)
class DailyScheduleQueryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DailyScheduleQueryService dailyScheduleQueryService;

    @Test
    void daily_schedule_요약본의_조회를_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        YearMonth yearMonth = YearMonth.of(2023, 8);

        given(dailyScheduleQueryService.getDailyScheduleSummaryQueryResponses(any()))
                .willReturn(List.of());

        // when
        ResultActions perform = mockMvc.perform(get("/dailyschedule/daily-todo-lists")
                .queryParam("yearMonth", String.valueOf(yearMonth)));

        // then
        perform.andExpect(status().isOk());

        verify(dailyScheduleQueryService).getDailyScheduleSummaryQueryResponses(any());
    }

}
