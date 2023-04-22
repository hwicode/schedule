package hwicode.schedule.dailyschedule.timetable.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.timetable.application.LearningTimeService;
import hwicode.schedule.dailyschedule.timetable.presentation.dto.subject_modify.LearningTimeSubjectModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.dto.subject_modify.LearningTimeSubjectModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.dto.subjectoftask_modify.LearningTimeSubjectOfTaskModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.dto.subjectoftask_modify.LearningTimeSubjectOfTaskModifyResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(LearningTimeController.class)
class LearningTImeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LearningTimeService learningTimeService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 학습_시간의_학습_주제_삭제을_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        Long learningTimeId = 1L;

        // when
        ResultActions perform = mockMvc.perform(
                delete(String.format("/dailyschedule/timetable/%s/subject", learningTimeId))
        );

        // then
        perform.andExpect(status().isNoContent());

        verify(learningTimeService).deleteSubject(any());
    }

    @Test
    void 학습_시간의_학습_주제_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        Long learningTimeId = 1L;
        LearningTimeSubjectModifyRequest learningTimeSubjectModifyRequest = createLearningTimeSubjectModifyRequest(NEW_SUBJECT);
        LearningTimeSubjectModifyResponse learningTimeSubjectModifyResponse = createLearningTimeSubjectModifyResponse(learningTimeId, NEW_SUBJECT);

        given(learningTimeService.changeSubject(any(), any()))
                .willReturn(NEW_SUBJECT);

        // when
        ResultActions perform = mockMvc.perform(
                patch(String.format("/dailyschedule/timetable/%s/subject", learningTimeId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(learningTimeSubjectModifyRequest)
                        ));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(learningTimeSubjectModifyResponse)
                ));

        verify(learningTimeService).changeSubject(any(), any());
    }

    @Test
    void 학습_시간의_Task_학습_주제_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        Long learningTimeId = 1L;
        Long subjectOfTaskId = 1L;

        LearningTimeSubjectOfTaskModifyRequest learningTimeSubjectOfTaskModifyRequest = createLearningTimeSubjectOfTaskModifyRequest(subjectOfTaskId);
        LearningTimeSubjectOfTaskModifyResponse learningTimeSubjectOfTaskModifyResponse = createLearningTimeSubjectOfTaskModifyResponse(learningTimeId, NEW_SUBJECT);

        given(learningTimeService.changeSubjectOfTask(any(), any()))
                .willReturn(NEW_SUBJECT);

        // when
        ResultActions perform = mockMvc.perform(
                patch(String.format("/dailyschedule/timetable/%s/subjectoftask", learningTimeId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(learningTimeSubjectOfTaskModifyRequest)
                        ));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(learningTimeSubjectOfTaskModifyResponse)
                ));

        verify(learningTimeService).changeSubjectOfTask(any(), any());
    }

}
