package hwicode.schedule.timetable.presentation;

import hwicode.schedule.timetable.application.query.TimeTableQueryService;
import hwicode.schedule.timetable.presentation.timetable.TimeTableQueryController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TimeTableQueryController.class)
class TimeTableQueryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TimeTableQueryService timeTableQueryService;

    @Test
    void 날짜로_계획표의_조회를_요청하면_리다이렉트_상태코드가_리턴된다() throws Exception {
        // given
        LocalDate date = LocalDate.of(2023, 8, 23);

        given(timeTableQueryService.getLearningTimeQueryResponses(any()))
                .willReturn(List.of());

        // when
        ResultActions perform = mockMvc.perform(get("/dailyschedule/timetables")
                .queryParam("date", String.valueOf(date)));

        // then
        perform.andExpect(status().isOk());

        verify(timeTableQueryService).getLearningTimeQueryResponses(any());
    }

}
