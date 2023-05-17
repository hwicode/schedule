package hwicode.schedule.dailyschedule.timetable.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.timetable.application.TimeTableAggregateService;
import hwicode.schedule.dailyschedule.timetable.exception.application.TimeTableNotFoundException;
import hwicode.schedule.dailyschedule.timetable.exception.domain.learningtime.EndTimeNotValidException;
import hwicode.schedule.dailyschedule.timetable.exception.domain.timetable.LearningTimeNotFoundException;
import hwicode.schedule.dailyschedule.timetable.exception.domain.timetablevalidator.ContainOtherTimeException;
import hwicode.schedule.dailyschedule.timetable.exception.domain.timetablevalidator.InvalidDateValidException;
import hwicode.schedule.dailyschedule.timetable.exception.domain.timetablevalidator.StartTimeDuplicateException;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.TimeTableController;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.delete.LearningTimeDeleteRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.endtime_modify.EndTimeModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.endtime_modify.EndTimeModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save.LearningTimeSaveRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save.LearningTimeSaveResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.starttime_modify.StartTimeModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.starttime_modify.StartTimeModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.subject_totaltime_response.SubjectOfSubTaskTotalLearningTimeResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.subject_totaltime_response.SubjectOfTaskTotalLearningTimeResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.subject_totaltime_response.SubjectTotalLearningTimeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static hwicode.schedule.dailyschedule.timetable.TimeTableDataHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TimeTableController.class)
class TimeTableControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TimeTableAggregateService timeTableAggregateService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 학습_시간_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        LearningTimeSaveRequest learningTimeSaveRequest = new LearningTimeSaveRequest(START_TIME);
        LearningTimeSaveResponse learningTimeSaveResponse = new LearningTimeSaveResponse(LEARNING_TIME_ID, START_TIME);

        given(timeTableAggregateService.saveLearningTime(any(), any()))
                .willReturn(LEARNING_TIME_ID);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/timetables/{timeTableId}/learning-times", TIME_TABLE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(learningTimeSaveRequest)
                        )
        );

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(learningTimeSaveResponse)
                ));

        verify(timeTableAggregateService).saveLearningTime(any(), any());
    }

    @Test
    void 학습_시간의_시작시간_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        StartTimeModifyRequest startTimeModifyRequest = new StartTimeModifyRequest(START_TIME, NEW_START_TIME);
        StartTimeModifyResponse startTimeModifyResponse = new StartTimeModifyResponse(NEW_START_TIME);

        given(timeTableAggregateService.changeLearningTimeStartTime(any(), any(), any()))
                .willReturn(NEW_START_TIME);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}/start-time", TIME_TABLE_ID, LEARNING_TIME_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(startTimeModifyRequest)
                        )
        );

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(startTimeModifyResponse)
                ));

        verify(timeTableAggregateService).changeLearningTimeStartTime(any(), any(), any());
    }

    @Test
    void 학습_시간의_끝나는_시간_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        EndTimeModifyRequest endTimeModifyRequest = new EndTimeModifyRequest(START_TIME, END_TIME);
        EndTimeModifyResponse endTimeModifyResponse = new EndTimeModifyResponse(END_TIME);

        given(timeTableAggregateService.changeLearningTimeEndTime(any(), any(), any()))
                .willReturn(END_TIME);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}/end-time", TIME_TABLE_ID, LEARNING_TIME_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(endTimeModifyRequest)
                        )
        );

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(endTimeModifyResponse)
                ));

        verify(timeTableAggregateService).changeLearningTimeEndTime(any(), any(), any());
    }

    @Test
    void 학습_시간_삭제를_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        LearningTimeDeleteRequest learningTimeDeleteRequest = new LearningTimeDeleteRequest(START_TIME);

        // when
        ResultActions perform = mockMvc.perform(
                delete("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}", TIME_TABLE_ID, LEARNING_TIME_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(learningTimeDeleteRequest)
                        )
        );

        // then
        perform.andExpect(status().isNoContent());

        verify(timeTableAggregateService).deleteLearningTime(any(), any());
    }

    @Test
    void 특정_학습_주제를_가진_학습_시간의_총_시간을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        int totalLearningTime = 100;
        SubjectTotalLearningTimeResponse subjectTotalLearningTimeResponse = new SubjectTotalLearningTimeResponse(totalLearningTime);

        given(timeTableAggregateService.calculateSubjectTotalLearningTime(any(), any()))
                .willReturn(totalLearningTime);

        // when
        ResultActions perform = mockMvc.perform(
                get("/dailyschedule/timetables/{timeTableId}/subject-total-time", TIME_TABLE_ID)
                        .queryParam("subject", SUBJECT));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subjectTotalLearningTimeResponse)
                ));

        verify(timeTableAggregateService).calculateSubjectTotalLearningTime(any(), any());
    }

    @Test
    void Task_학습_주제를_가진_학습_시간의_총_시간을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        int totalLearningTime = 100;
        SubjectOfTaskTotalLearningTimeResponse subjectOfTaskTotalLearningTimeResponse = new SubjectOfTaskTotalLearningTimeResponse(totalLearningTime);

        given(timeTableAggregateService.calculateSubjectOfTaskTotalLearningTime(any(), any()))
                .willReturn(totalLearningTime);

        // when
        ResultActions perform = mockMvc.perform(
                get("/dailyschedule/timetables/{timeTableId}/task-total-time", TIME_TABLE_ID)
                        .queryParam("subject_of_task_id", String.valueOf(SUBJECT_OF_TASK_ID)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subjectOfTaskTotalLearningTimeResponse)
                ));

        verify(timeTableAggregateService).calculateSubjectOfTaskTotalLearningTime(any(), any());
    }

    @Test
    void SubTask_학습_주제를_가진_학습_시간의_총_시간을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        int totalLearningTime = 100;
        SubjectOfSubTaskTotalLearningTimeResponse subjectOfSubTaskTotalLearningTimeResponse = new SubjectOfSubTaskTotalLearningTimeResponse(totalLearningTime);

        given(timeTableAggregateService.calculateSubjectOfSubTaskTotalLearningTime(any(), any()))
                .willReturn(totalLearningTime);

        // when
        ResultActions perform = mockMvc.perform(
                get("/dailyschedule/timetables/{timeTableId}/subtask-total-time", TIME_TABLE_ID)
                        .queryParam("subject_of_subtask_id", String.valueOf(SUBJECT_OF_SUBTASK_ID)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subjectOfSubTaskTotalLearningTimeResponse)
                ));

        verify(timeTableAggregateService).calculateSubjectOfSubTaskTotalLearningTime(any(), any());
    }

    @Test
    void 학습_시간_생성을_요청할_때_타임_테이블이_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        TimeTableNotFoundException timeTableNotFoundException = new TimeTableNotFoundException();
        LearningTimeSaveRequest learningTimeSaveRequest = new LearningTimeSaveRequest(START_TIME);

        given(timeTableAggregateService.saveLearningTime(any(), any()))
                .willThrow(timeTableNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/timetables/{timeTableId}/learning-times", TIME_TABLE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(learningTimeSaveRequest)
                        )
        );

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(timeTableNotFoundException.getMessage()));

        verify(timeTableAggregateService).saveLearningTime(any(), any());
    }

    @Test
    void 학습_시간의_끝나는_시간_변경을_요청할_때_끝나는_시간이_시작_시간보다_앞서면_에러가_발생한다() throws Exception {
        // given
        EndTimeNotValidException endTimeNotValidException = new EndTimeNotValidException();
        EndTimeModifyRequest endTimeModifyRequest = new EndTimeModifyRequest(START_TIME, END_TIME);

        given(timeTableAggregateService.changeLearningTimeEndTime(any(), any(), any()))
                .willThrow(endTimeNotValidException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}/end-time", TIME_TABLE_ID, LEARNING_TIME_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(endTimeModifyRequest)
                        )
        );

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(endTimeNotValidException.getMessage()));

        verify(timeTableAggregateService).changeLearningTimeEndTime(any(), any(), any());
    }

    @Test
    void 학습_시간의_끝나는_시간을_수정할_때_학습_시간이_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        LearningTimeNotFoundException learningTimeNotFoundException = new LearningTimeNotFoundException();
        EndTimeModifyRequest endTimeModifyRequest = new EndTimeModifyRequest(START_TIME, END_TIME);

        given(timeTableAggregateService.changeLearningTimeEndTime(any(), any(), any()))
                .willThrow(learningTimeNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/timetables/{timeTableId}/learning-times/{startTime}/end-time", TIME_TABLE_ID, LEARNING_TIME_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(endTimeModifyRequest)
                        )
        );

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(learningTimeNotFoundException.getMessage()));

        verify(timeTableAggregateService).changeLearningTimeEndTime(any(), any(), any());
    }

    @Test
    void 학습_시간의_시작시간_변경을_요청할_때_시작시간이_다른_학습_시간의_시간대에_포함되면_에러가_발생한다() throws Exception {
        // given
        ContainOtherTimeException containOtherTimeException = new ContainOtherTimeException();
        StartTimeModifyRequest startTimeModifyRequest = new StartTimeModifyRequest(START_TIME, NEW_START_TIME);

        given(timeTableAggregateService.changeLearningTimeStartTime(any(), any(), any()))
                .willThrow(containOtherTimeException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}/start-time", TIME_TABLE_ID, LEARNING_TIME_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(startTimeModifyRequest)
                        )
        );

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containOtherTimeException.getMessage()));

        verify(timeTableAggregateService).changeLearningTimeStartTime(any(), any(), any());
    }

    @Test
    void 학습_시간의_시작시간_변경을_요청할_때_요청_날짜가_타임_테이블의_날짜_또는_그_다음날이_아닌_경우_에러가_발생한다() throws Exception {
        // given
        InvalidDateValidException invalidDateValidException = new InvalidDateValidException();
        StartTimeModifyRequest startTimeModifyRequest = new StartTimeModifyRequest(START_TIME, NEW_START_TIME);

        given(timeTableAggregateService.changeLearningTimeStartTime(any(), any(), any()))
                .willThrow(invalidDateValidException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}/start-time", TIME_TABLE_ID, LEARNING_TIME_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(startTimeModifyRequest)
                        )
        );

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(invalidDateValidException.getMessage()));

        verify(timeTableAggregateService).changeLearningTimeStartTime(any(), any(), any());
    }

    @Test
    void 학습_시간의_시작시간_변경을_요청할_때_시작시간이_중복되는_경우_에러가_발생한다() throws Exception {
        // given
        StartTimeDuplicateException startTimeDuplicateException = new StartTimeDuplicateException();
        StartTimeModifyRequest startTimeModifyRequest = new StartTimeModifyRequest(START_TIME, NEW_START_TIME);

        given(timeTableAggregateService.changeLearningTimeStartTime(any(), any(), any()))
                .willThrow(startTimeDuplicateException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}/start-time", TIME_TABLE_ID, LEARNING_TIME_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(startTimeModifyRequest)
                        )
        );

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(startTimeDuplicateException.getMessage()));

        verify(timeTableAggregateService).changeLearningTimeStartTime(any(), any(), any());
    }
}
