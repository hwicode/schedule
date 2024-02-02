package hwicode.schedule.timetable.presentation;

import hwicode.schedule.auth.infra.token.DecodedToken;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.timetable.TimeTableDataHelper;
import hwicode.schedule.timetable.application.query.TimeTableQueryService;
import hwicode.schedule.timetable.application.query.dto.subject_totaltime_response.SubjectOfSubTaskTotalLearningTimeResponse;
import hwicode.schedule.timetable.application.query.dto.subject_totaltime_response.SubjectOfTaskTotalLearningTimeResponse;
import hwicode.schedule.timetable.application.query.dto.subject_totaltime_response.SubjectTotalLearningTimeResponse;
import hwicode.schedule.timetable.presentation.timetable.TimeTableQueryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static hwicode.schedule.auth.AuthDataHelper.BEARER;
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

    @MockBean
    TokenProvider tokenProvider;

    @BeforeEach
    void decodeToken() {
        given(tokenProvider.decodeToken(any()))
                .willReturn(new DecodedToken(1L));
    }


    @Test
    void 날짜로_계획표의_학습_시간_조회를_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        LocalDate date = LocalDate.of(2023, 8, 23);

        given(timeTableQueryService.getLearningTimeQueryResponses(any(), any()))
                .willReturn(List.of());

        // when
        ResultActions perform = mockMvc.perform(get("/dailyschedule/timetables")
                .queryParam("date", String.valueOf(date))
                .header("Authorization", BEARER + "accessToken"));

        // then
        perform.andExpect(status().isOk());

        verify(timeTableQueryService).getLearningTimeQueryResponses(any(), any());
    }

}
