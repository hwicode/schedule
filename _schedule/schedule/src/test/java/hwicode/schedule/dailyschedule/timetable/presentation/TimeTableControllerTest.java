package hwicode.schedule.dailyschedule.timetable.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.timetable.application.TimeTableService;
import hwicode.schedule.dailyschedule.timetable.exception.domain.application.TimeTableNotFoundException;
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
    TimeTableService timeTableService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 학습_시간_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        LearningTimeSaveRequest learningTimeSaveRequest = createLearningTimeSaveRequest(START_TIME);
        LearningTimeSaveResponse learningTimeSaveResponse = createLearningTimeSaveResponse(LEARNING_TIME_ID, START_TIME);

        given(timeTableService.saveLearningTime(any(), any()))
                .willReturn(LEARNING_TIME_ID);

        // when
        ResultActions perform = mockMvc.perform(
                post(String.format("/dailyschedule/timetables/%s", TIME_TABLE_ID))
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

        verify(timeTableService).saveLearningTime(any(), any());
    }

    @Test
    void 학습_시간의_시작시간_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        StartTimeModifyRequest startTimeModifyRequest = createStartTimeModifyRequest(TIME_TABLE_ID, NEW_START_TIME);
        StartTimeModifyResponse startTimeModifyResponse = createStartTimeModifyResponse(NEW_START_TIME);

        given(timeTableService.changeLearningTimeStartTime(any(), any(), any()))
                .willReturn(NEW_START_TIME);

        // when
        ResultActions perform = mockMvc.perform(
                patch(String.format("/dailyschedule/timetable/%s/starttime", START_TIME))
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

        verify(timeTableService).changeLearningTimeStartTime(any(), any(), any());
    }

    @Test
    void 학습_시간의_끝나는_시간_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        EndTimeModifyRequest endTimeModifyRequest = createEndTimeModifyRequest(TIME_TABLE_ID, END_TIME);
        EndTimeModifyResponse endTimeModifyResponse = createEndTimeModifyResponse(END_TIME);

        given(timeTableService.changeLearningTimeEndTime(any(), any(), any()))
                .willReturn(END_TIME);

        // when
        ResultActions perform = mockMvc.perform(
                patch(String.format("/dailyschedule/timetable/%s/endtime", START_TIME))
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

        verify(timeTableService).changeLearningTimeEndTime(any(), any(), any());
    }

    @Test
    void 학습_시간_삭제를_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        LearningTimeDeleteRequest learningTimeDeleteRequest = createLearningTimeDeleteRequest(TIME_TABLE_ID);

        // when
        ResultActions perform = mockMvc.perform(
                delete(String.format("/dailyschedule/timetable/%s", START_TIME))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(learningTimeDeleteRequest)
                        )
        );

        // then
        perform.andExpect(status().isNoContent());

        verify(timeTableService).deleteLearningTime(any(), any());
    }

    @Test
    void 특정_학습_주제를_가진_학습_시간의_총_시간을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        int totalLearningTime = 100;
        SubjectTotalLearningTimeResponse subjectTotalLearningTimeResponse = createSubjectTotalLearningTimeResponse(totalLearningTime);

        given(timeTableService.calculateSubjectTotalLearningTime(any(), any()))
                .willReturn(totalLearningTime);

        // when
        ResultActions perform = mockMvc.perform(
                get(String.format("/dailyschedule/timetables/%s/%s", TIME_TABLE_ID, SUBJECT))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subjectTotalLearningTimeResponse)
                ));

        verify(timeTableService).calculateSubjectTotalLearningTime(any(), any());
    }

    @Test
    void Task_학습_주제를_가진_학습_시간의_총_시간을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        int totalLearningTime = 100;
        SubjectOfTaskTotalLearningTimeResponse subjectOfTaskTotalLearningTimeResponse = createSubjectOfTaskTotalLearningTimeResponse(totalLearningTime);

        given(timeTableService.calculateSubjectOfTaskTotalLearningTime(any(), any()))
                .willReturn(totalLearningTime);

        // when
        ResultActions perform = mockMvc.perform(
                get(String.format("/dailyschedule/timetables/%s/subjectoftask/%s", TIME_TABLE_ID, SUBJECT_OF_TASK_ID))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subjectOfTaskTotalLearningTimeResponse)
                ));

        verify(timeTableService).calculateSubjectOfTaskTotalLearningTime(any(), any());
    }

    @Test
    void SubTask_학습_주제를_가진_학습_시간의_총_시간을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        int totalLearningTime = 100;
        SubjectOfSubTaskTotalLearningTimeResponse subjectOfSubTaskTotalLearningTimeResponse = createSubjectOfSubTaskTotalLearningTimeResponse(totalLearningTime);

        given(timeTableService.calculateSubjectOfSubTaskTotalLearningTime(any(), any()))
                .willReturn(totalLearningTime);

        // when
        ResultActions perform = mockMvc.perform(
                get(String.format("/dailyschedule/timetables/%s/subjectofsubtask/%s", TIME_TABLE_ID, SUBJECT_OF_SUBTASK_ID))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subjectOfSubTaskTotalLearningTimeResponse)
                ));

        verify(timeTableService).calculateSubjectOfSubTaskTotalLearningTime(any(), any());
    }

    @Test
    void 학습_시간_생성을_요청할_때_타임_테이블이_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        TimeTableNotFoundException timeTableNotFoundException = new TimeTableNotFoundException();
        LearningTimeSaveRequest learningTimeSaveRequest = createLearningTimeSaveRequest(START_TIME);

        given(timeTableService.saveLearningTime(any(), any()))
                .willThrow(timeTableNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                post(String.format("/dailyschedule/timetables/%s", TIME_TABLE_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(learningTimeSaveRequest)
                        )
        );

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(timeTableNotFoundException.getMessage()));

        verify(timeTableService).saveLearningTime(any(), any());
    }

}
