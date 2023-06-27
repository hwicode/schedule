package hwicode.schedule.validation_annotation_test.timetable;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.common.exception.GlobalErrorCode;
import hwicode.schedule.timetable.application.TimeTableAggregateService;
import hwicode.schedule.timetable.presentation.timetable.TimeTableController;
import hwicode.schedule.timetable.presentation.timetable.dto.save.LearningTimeSaveRequest;
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

import static hwicode.schedule.timetable.TimeTableDataHelper.*;
import static hwicode.schedule.validation_annotation_test.ValidationDataHelper.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TimeTableController.class)
class TimeTableValidatedTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TimeTableAggregateService timeTableAggregateService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 타입_테이블의_id값이_0보다_큰_정수값이면_통과된다() throws Exception {
        // given
        LearningTimeSaveRequest learningTimeSaveRequest = new LearningTimeSaveRequest(START_TIME);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/timetables/{timeTableId}/learning-times", TIME_TABLE_ID)
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

    @Test
    void 특정_학습_주제가_제대로_들어오면_통과된다() throws Exception {
        // given when
        ResultActions perform = mockMvc.perform(
                get("/dailyschedule/timetables/{timeTableId}/subject-total-time", TIME_TABLE_ID)
                        .queryParam("subject", SUBJECT));

        // then
        perform.andExpect(status().isOk());
    }

    @Test
    void 특정_학습_주제에_null_값이_들어오면_400에러가_발생한다() throws Exception {
        // given
        String subject = null;

        // when
        ResultActions perform = mockMvc.perform(
                get("/dailyschedule/timetables/{timeTableId}/subject-total-time", TIME_TABLE_ID)
                        .queryParam("subject", subject));

        // then
        String message = "Required request parameter 'subject' for method parameter type String is not present";
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(message));
    }

    private static Stream<Arguments> provideWrongSubject() {
        return Stream.of(
                arguments("", NOT_BLANK_ERROR_MESSAGE),
                arguments(" ", NOT_BLANK_ERROR_MESSAGE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideWrongSubject")
    void 특정_학습_주제에_잘못된_값이_들어오면_400에러가_발생한다(String subject, String errorMessage) throws Exception {
        // given when
        ResultActions perform = mockMvc.perform(
                get("/dailyschedule/timetables/{timeTableId}/subject-total-time", TIME_TABLE_ID)
                        .queryParam("subject", subject));

        // then
        String field = "getSubjectTotalLearningTime.subject";
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(GlobalErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(jsonPath("$.errors[0].field").value(field))
                .andExpect(jsonPath("$.errors[0].message").value(errorMessage));
    }

    @Test
    void Task_학습_주제가_제대로_들어오면_통과된다() throws Exception {
        // given when
        ResultActions perform = mockMvc.perform(
                get("/dailyschedule/timetables/{timeTableId}/task-total-time", TIME_TABLE_ID)
                        .queryParam("subject_of_task_id", String.valueOf(SUBJECT_OF_TASK_ID)));

        // then
        perform.andExpect(status().isOk());
    }

    @Test
    void Task_학습_주제에_형식에_맞지_않는_값이_들어오면_400에러가_발생한다() throws Exception {
        // given
        String subjectOfTaskId = "ww";

        // when
        ResultActions perform = mockMvc.perform(
                get("/dailyschedule/timetables/{timeTableId}/task-total-time", TIME_TABLE_ID)
                        .queryParam("subject_of_task_id", subjectOfTaskId));

        // then
        String message = getNumberFormatExceptionMessage(subjectOfTaskId);
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void Task_학습_주제에_null_값이_들어오면_400에러가_발생한다() throws Exception {
        // given
        Long subjectOfTaskId = null;

        // when
        ResultActions perform = mockMvc.perform(
                get("/dailyschedule/timetables/{timeTableId}/task-total-time", TIME_TABLE_ID)
                        .queryParam("subject_of_task_id", String.valueOf(subjectOfTaskId)));

        // then
        String message = getNumberFormatExceptionMessage(String.valueOf(subjectOfTaskId));
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(message));
    }

    private static Stream<Arguments> provideWrongSubjectOfTaskId() {
        return Stream.of(
                arguments(-1L, POSITIVE_ERROR_MESSAGE),
                arguments(0L, POSITIVE_ERROR_MESSAGE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideWrongSubjectOfTaskId")
    void Task_학습_주제에_잘못된_값이_들어오면_400에러가_발생한다(Long subjectOfTaskId, String errorMessage) throws Exception {
        // given when
        ResultActions perform = mockMvc.perform(
                get("/dailyschedule/timetables/{timeTableId}/task-total-time", TIME_TABLE_ID)
                        .queryParam("subject_of_task_id", String.valueOf(subjectOfTaskId)));

        // then
        String field = "getSubjectOfTaskTotalLearningTime.subjectOfTaskId";
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(GlobalErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(jsonPath("$.errors[0].field").value(field))
                .andExpect(jsonPath("$.errors[0].message").value(errorMessage));
    }

}
