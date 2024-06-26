package hwicode.schedule.calendar.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.auth.infra.token.DecodedToken;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.calendar.application.daily_schedule.DailyScheduleService;
import hwicode.schedule.calendar.exception.application.DailyScheduleDateException;
import hwicode.schedule.calendar.presentation.daily_schedule.DailyScheduleController;
import hwicode.schedule.calendar.presentation.daily_schedule.dto.DailyScheduleSaveRequest;
import hwicode.schedule.calendar.presentation.daily_schedule.dto.DailyScheduleSaveResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static hwicode.schedule.auth.AuthDataHelper.BEARER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DailyScheduleController.class)
class DailyScheduleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    DailyScheduleService dailyScheduleService;

    @MockBean
    TokenProvider tokenProvider;

    @BeforeEach
    void decodeToken() {
        given(tokenProvider.decodeToken(any()))
                .willReturn(new DecodedToken(1L));
    }

    @Test
    void 계획표_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        LocalDate date = LocalDate.of(2023, 8, 23);
        Long dailyScheduleId = 1L;

        DailyScheduleSaveRequest dailyScheduleSaveRequest = new DailyScheduleSaveRequest(date);
        DailyScheduleSaveResponse dailyScheduleSaveResponse = new DailyScheduleSaveResponse(dailyScheduleId, date);

        given(dailyScheduleService.saveDailySchedule(any()))
                .willReturn(dailyScheduleId);

        // when
        ResultActions perform = mockMvc.perform(post("/daily-todo-lists")
                .header("Authorization", BEARER + "accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dailyScheduleSaveRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(dailyScheduleSaveResponse)
                ));

        verify(dailyScheduleService).saveDailySchedule(any());
    }

    @Test
    void 계획표_생성을_요청할_때_당일이_아니라면_에러가_발생한다() throws Exception {
        // given
        LocalDate date = LocalDate.of(2023, 8, 23);
        DailyScheduleSaveRequest dailyScheduleSaveRequest = new DailyScheduleSaveRequest(date);

        DailyScheduleDateException dailyScheduleDateException = new DailyScheduleDateException();
        given(dailyScheduleService.saveDailySchedule(any()))
                .willThrow(dailyScheduleDateException);

        // when
        ResultActions perform = mockMvc.perform(post("/daily-todo-lists")
                .header("Authorization", BEARER + "accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dailyScheduleSaveRequest)));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(dailyScheduleDateException.getMessage()));
    }

}
