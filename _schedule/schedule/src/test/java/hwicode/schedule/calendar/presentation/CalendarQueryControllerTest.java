package hwicode.schedule.calendar.presentation;

import hwicode.schedule.calendar.application.CalendarProviderService;
import hwicode.schedule.calendar.application.query.CalendarQueryService;
import hwicode.schedule.calendar.application.query.dto.CalendarQueryResponse;
import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.exception.application.CalendarNotFoundException;
import hwicode.schedule.calendar.presentation.calendar.CalendarQueryController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.YearMonth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalendarQueryController.class)
class CalendarQueryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CalendarProviderService calendarProviderService;

    @MockBean
    CalendarQueryService calendarQueryService;

    @Test
    void 캘린더의_조회를_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        YearMonth yearMonth = YearMonth.of(2023, 8);

        given(calendarProviderService.provideCalendar(any()))
                .willReturn(new Calendar(yearMonth));
        given(calendarQueryService.getCalendarQueryResponse(any()))
                .willReturn(CalendarQueryResponse.builder().build());

        // when
        ResultActions perform = mockMvc.perform(get("/calendars")
                .queryParam("yearMonth", String.valueOf(yearMonth)));

        // then
        perform.andExpect(status().isOk());

        verify(calendarProviderService).provideCalendar(any());
        verify(calendarQueryService).getCalendarQueryResponse(any());
    }

    @Test
    void 캘린더의_조회를_요청할_때_캘린더가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        CalendarNotFoundException calendarNotFoundException = new CalendarNotFoundException();

        YearMonth yearMonth = YearMonth.of(2023, 8);

        given(calendarProviderService.provideCalendar(any()))
                .willReturn(new Calendar(yearMonth));
        given(calendarQueryService.getCalendarQueryResponse(any()))
                .willThrow(calendarNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(get("/calendars")
                .queryParam("yearMonth", String.valueOf(yearMonth)));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(calendarNotFoundException.getMessage()));

        verify(calendarProviderService).provideCalendar(any());
        verify(calendarQueryService).getCalendarQueryResponse(any());
    }

}
