package hwicode.schedule.calendar.presentation;

import hwicode.schedule.calendar.application.DailyScheduleProviderService;
import hwicode.schedule.calendar.presentation.daily_schedule.DailyScheduleController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DailyScheduleController.class)
class DailyScheduleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DailyScheduleProviderService dailyScheduleProviderService;

    @Test
    void 날짜로_계획표의_조회를_요청하면_리다이렉트_상태코드가_리턴된다() throws Exception {
        // given
        LocalDate date = LocalDate.of(2023, 8, 23);
        Long dailyScheduleId = 1L;

        given(dailyScheduleProviderService.provideDailyScheduleId(any()))
                .willReturn(dailyScheduleId);

        // when
        ResultActions perform = mockMvc.perform(get("/daily-todo-lists")
                .queryParam("date", String.valueOf(date)));

        // then
        perform.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dailyschedule/daily-todo-lists/" + dailyScheduleId));
    }

}
