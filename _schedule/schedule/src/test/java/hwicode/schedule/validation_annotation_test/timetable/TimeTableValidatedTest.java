package hwicode.schedule.validation_annotation_test.timetable;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.auth.infra.token.DecodedToken;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.common.exception.GlobalErrorCode;
import hwicode.schedule.timetable.application.TimeTableAggregateService;
import hwicode.schedule.timetable.presentation.timetable.TimeTableController;
import hwicode.schedule.timetable.presentation.timetable.dto.save.LearningTimeSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static hwicode.schedule.auth.AuthDataHelper.BEARER;
import static hwicode.schedule.timetable.TimeTableDataHelper.START_TIME;
import static hwicode.schedule.timetable.TimeTableDataHelper.TIME_TABLE_ID;
import static hwicode.schedule.validation_annotation_test.ValidationDataHelper.POSITIVE_ERROR_MESSAGE;
import static hwicode.schedule.validation_annotation_test.ValidationDataHelper.getNumberFormatExceptionMessage;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TimeTableController.class)
class TimeTableValidatedTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TimeTableAggregateService timeTableAggregateService;

    @MockBean
    TokenProvider tokenProvider;

    @BeforeEach
    void decodeToken() {
        given(tokenProvider.decodeToken(any()))
                .willReturn(new DecodedToken(1L));
    }

    @Test
    void 타입_테이블의_id값이_0보다_큰_정수값이면_통과된다() throws Exception {
        // given
        LearningTimeSaveRequest learningTimeSaveRequest = new LearningTimeSaveRequest(START_TIME);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/timetables/{timeTableId}/learning-times", TIME_TABLE_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(learningTimeSaveRequest)
                        )
        );

        // then
        perform.andExpect(status().isCreated());
    }

    @Test
    void 타입_테이블의_id에_형식에_맞지_않는_값이_들어오면_400에러가_발생한다() throws Exception {
        // given
        String wrongTimeTableId = "ww";
        LearningTimeSaveRequest learningTimeSaveRequest = new LearningTimeSaveRequest(START_TIME);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/timetables/{timeTableId}/learning-times", wrongTimeTableId)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(learningTimeSaveRequest)
                        )
        );

        // then
        String message = getNumberFormatExceptionMessage(wrongTimeTableId);
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void 타입_테이블의_id에_null_값이_들어오면_404에러가_발생한다() throws Exception {
        // given
        Long wrongTimeTableId = null;
        LearningTimeSaveRequest learningTimeSaveRequest = new LearningTimeSaveRequest(START_TIME);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/timetables/{timeTableId}/learning-times", wrongTimeTableId)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(learningTimeSaveRequest)
                        )
        );

        // then
        perform.andExpect(status().isNotFound());
    }

    private static Stream<Arguments> provideWrongTimeTableId() {
        return Stream.of(
                arguments(-1L, POSITIVE_ERROR_MESSAGE),
                arguments(0L, POSITIVE_ERROR_MESSAGE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideWrongTimeTableId")
    void 타입_테이블의_id값에_잘못된_값이_들어오면_400에러가_발생한다(Long wrongTimeTableId, String errorMessage) throws Exception {
        // given
        LearningTimeSaveRequest learningTimeSaveRequest = new LearningTimeSaveRequest(START_TIME);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/timetables/{timeTableId}/learning-times", wrongTimeTableId)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(learningTimeSaveRequest)
                        )
        );

        // then
        String field = "saveLearningTime.timeTableId";
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(GlobalErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(jsonPath("$.errors[0].field").value(field))
                .andExpect(jsonPath("$.errors[0].message").value(errorMessage));
    }

}
