package hwicode.schedule.dailyschedule.daily_schedule_query.presentation;

import hwicode.schedule.dailyschedule.daily_schedule_query.application.DailyScheduleQueryService;
import hwicode.schedule.dailyschedule.daily_schedule_query.exception.DailyScheduleNotExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DailyScheduleQueryController.class)
class DailyScheduleQueryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DailyScheduleQueryService dailyScheduleQueryService;

    @Test
    void daily_schedule_한_달_치_요약본의_조회를_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        YearMonth yearMonth = YearMonth.of(2023, 8);

        given(dailyScheduleQueryService.getMonthlyDailyScheduleQueryResponses(any(), any()))
                .willReturn(List.of());

        // when
        ResultActions perform = mockMvc.perform(get("/dailyschedule/calendar/daily-todo-lists")
                .queryParam("yearMonth", String.valueOf(yearMonth)));

        // then
        perform.andExpect(status().isOk());

        verify(dailyScheduleQueryService).getMonthlyDailyScheduleQueryResponses(any(), any());
    }

    @Test
    void daily_schedule_조회를_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        LocalDate date = LocalDate.of(2023, 8, 1);
        given(dailyScheduleQueryService.getDailyScheduleQueryResponse(any(), any()))
                .willReturn(null);

        // when
        ResultActions perform = mockMvc.perform(get("/dailyschedule/daily-todo-lists")
                .queryParam("date", String.valueOf(date)));

        // then
        perform.andExpect(status().isOk());

        verify(dailyScheduleQueryService).getDailyScheduleQueryResponse(any(), any());
    }

    @Test
    void daily_schedule_조회를_요청할_때_daily_schedule가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        LocalDate date = LocalDate.of(2023, 8, 1);
        DailyScheduleNotExistException dailyScheduleNotExistException = new DailyScheduleNotExistException();

        given(dailyScheduleQueryService.getDailyScheduleQueryResponse(any(), any()))
                .willThrow(dailyScheduleNotExistException);

        // when
        ResultActions perform = mockMvc.perform(get("/dailyschedule/daily-todo-lists")
                .queryParam("date", String.valueOf(date)));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(dailyScheduleNotExistException.getMessage()));

        verify(dailyScheduleQueryService).getDailyScheduleQueryResponse(any(), any());
    }

}
