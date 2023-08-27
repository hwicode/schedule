package hwicode.schedule.timetable.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.timetable.TimeTableDataHelper;
import hwicode.schedule.timetable.application.query.TimeTableQueryService;
import hwicode.schedule.timetable.presentation.timetable.TimeTableQueryController;
import hwicode.schedule.timetable.application.query.dto.subject_totaltime_response.SubjectOfSubTaskTotalLearningTimeResponse;
import hwicode.schedule.timetable.application.query.dto.subject_totaltime_response.SubjectOfTaskTotalLearningTimeResponse;
import hwicode.schedule.timetable.application.query.dto.subject_totaltime_response.SubjectTotalLearningTimeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 날짜로_계획표의_학습_시간_조회를_요청하면_200_상태코드가_리턴된다() throws Exception {
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

    @Test
    void 특정_학습_주제를_가진_학습_시간의_총_시간을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        int totalLearningTime = 100;
        SubjectTotalLearningTimeResponse subjectTotalLearningTimeResponse = new SubjectTotalLearningTimeResponse(totalLearningTime);

        given(timeTableQueryService.calculateSubjectTotalLearningTime(any(), any()))
                .willReturn(totalLearningTime);

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.get("/dailyschedule/timetables/{timeTableId}/subject-total-time", TimeTableDataHelper.TIME_TABLE_ID)
                        .queryParam("subject", TimeTableDataHelper.SUBJECT));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subjectTotalLearningTimeResponse)
                ));

        verify(timeTableQueryService).calculateSubjectTotalLearningTime(any(), any());
    }

    @Test
    void Task_학습_주제를_가진_학습_시간의_총_시간을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        int totalLearningTime = 100;
        SubjectOfTaskTotalLearningTimeResponse subjectOfTaskTotalLearningTimeResponse = new SubjectOfTaskTotalLearningTimeResponse(totalLearningTime);

        given(timeTableQueryService.calculateSubjectOfTaskTotalLearningTime(any(), any()))
                .willReturn(totalLearningTime);

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.get("/dailyschedule/timetables/{timeTableId}/task-total-time", TimeTableDataHelper.TIME_TABLE_ID)
                        .queryParam("subject_of_task_id", String.valueOf(TimeTableDataHelper.SUBJECT_OF_TASK_ID)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subjectOfTaskTotalLearningTimeResponse)
                ));

        verify(timeTableQueryService).calculateSubjectOfTaskTotalLearningTime(any(), any());
    }

    @Test
    void SubTask_학습_주제를_가진_학습_시간의_총_시간을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        int totalLearningTime = 100;
        SubjectOfSubTaskTotalLearningTimeResponse subjectOfSubTaskTotalLearningTimeResponse = new SubjectOfSubTaskTotalLearningTimeResponse(totalLearningTime);

        given(timeTableQueryService.calculateSubjectOfSubTaskTotalLearningTime(any(), any()))
                .willReturn(totalLearningTime);

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.get("/dailyschedule/timetables/{timeTableId}/subtask-total-time", TimeTableDataHelper.TIME_TABLE_ID)
                        .queryParam("subject_of_subtask_id", String.valueOf(TimeTableDataHelper.SUBJECT_OF_SUBTASK_ID)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subjectOfSubTaskTotalLearningTimeResponse)
                ));

        verify(timeTableQueryService).calculateSubjectOfSubTaskTotalLearningTime(any(), any());
    }

}
