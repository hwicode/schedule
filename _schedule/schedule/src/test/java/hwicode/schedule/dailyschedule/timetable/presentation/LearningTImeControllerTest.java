package hwicode.schedule.dailyschedule.timetable.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.timetable.application.LearningTimeService;
import hwicode.schedule.dailyschedule.timetable.exception.domain.application.SubjectOfSubTaskNotFoundException;
import hwicode.schedule.dailyschedule.timetable.exception.domain.application.SubjectOfTaskNotFoundException;
import hwicode.schedule.dailyschedule.timetable.exception.domain.timetable.LearningTimeNotFoundException;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.LearningTimeController;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subject_modify.LearningTimeSubjectModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subject_modify.LearningTimeSubjectModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subjectofsubtask_modify.LearningTimeSubjectOfSubTaskModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subjectofsubtask_modify.LearningTimeSubjectOfSubTaskModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subjectoftask_modify.LearningTimeSubjectOfTaskModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subjectoftask_modify.LearningTimeSubjectOfTaskModifyResponse;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        // when
        ResultActions perform = mockMvc.perform(
                delete(String.format("/dailyschedule/learning-times/%s", LEARNING_TIME_ID))
        );

        // then
        perform.andExpect(status().isNoContent());

        verify(learningTimeService).deleteSubject(any());
    }

    @Test
    void 학습_시간의_학습_주제_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        LearningTimeSubjectModifyRequest learningTimeSubjectModifyRequest = createLearningTimeSubjectModifyRequest(NEW_SUBJECT);
        LearningTimeSubjectModifyResponse learningTimeSubjectModifyResponse = createLearningTimeSubjectModifyResponse(LEARNING_TIME_ID, NEW_SUBJECT);

        given(learningTimeService.changeSubject(any(), any()))
                .willReturn(NEW_SUBJECT);

        // when
        ResultActions perform = mockMvc.perform(
                patch(String.format("/dailyschedule/learning-times/%s/subject", LEARNING_TIME_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(learningTimeSubjectModifyRequest)
                        )
        );

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
        LearningTimeSubjectOfTaskModifyRequest learningTimeSubjectOfTaskModifyRequest = createLearningTimeSubjectOfTaskModifyRequest(SUBJECT_OF_TASK_ID);
        LearningTimeSubjectOfTaskModifyResponse learningTimeSubjectOfTaskModifyResponse = createLearningTimeSubjectOfTaskModifyResponse(LEARNING_TIME_ID, NEW_SUBJECT);

        given(learningTimeService.changeSubjectOfTask(any(), any()))
                .willReturn(NEW_SUBJECT);

        // when
        ResultActions perform = mockMvc.perform(
                patch(String.format("/dailyschedule/learning-times/%s/subject-of-task", LEARNING_TIME_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(learningTimeSubjectOfTaskModifyRequest)
                        )
        );

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(learningTimeSubjectOfTaskModifyResponse)
                ));

        verify(learningTimeService).changeSubjectOfTask(any(), any());
    }

    @Test
    void 학습_시간의_SubTask_학습_주제_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        LearningTimeSubjectOfSubTaskModifyRequest learningTimeSubjectOfSubTaskModifyRequest = createLearningTimeSubjectOfSubTaskModifyRequest(SUBJECT_OF_SUBTASK_ID);
        LearningTimeSubjectOfSubTaskModifyResponse learningTimeSubjectOfSubTaskModifyResponse = createLearningTimeSubjectOfSubTaskModifyResponse(LEARNING_TIME_ID, NEW_SUBJECT);

        given(learningTimeService.changeSubjectOfSubTask(any(), any()))
                .willReturn(NEW_SUBJECT);

        // when
        ResultActions perform = mockMvc.perform(
                patch(String.format("/dailyschedule/learning-times/%s/subject-of-subtask", LEARNING_TIME_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(learningTimeSubjectOfSubTaskModifyRequest)
                        )
        );

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(learningTimeSubjectOfSubTaskModifyResponse)
                ));

        verify(learningTimeService).changeSubjectOfSubTask(any(), any());
    }

    @Test
    void 학습_시간의_학습_주제_삭제을_요청할_때_학습_시간이_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        LearningTimeNotFoundException learningTimeNotFoundException = new LearningTimeNotFoundException();

        given(learningTimeService.deleteSubject(any()))
                .willThrow(learningTimeNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                delete(String.format("/dailyschedule/learning-times/%s", LEARNING_TIME_ID))
        );

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(learningTimeNotFoundException.getMessage()));

        verify(learningTimeService).deleteSubject(any());
    }

    @Test
    void 학습_시간의_Task_학습_주제_변경을_요청할_때_Task_학습_주제가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        SubjectOfTaskNotFoundException subjectOfTaskNotFoundException = new SubjectOfTaskNotFoundException();

        LearningTimeSubjectOfTaskModifyRequest learningTimeSubjectOfTaskModifyRequest = createLearningTimeSubjectOfTaskModifyRequest(SUBJECT_OF_TASK_ID);

        given(learningTimeService.changeSubjectOfTask(any(), any()))
                .willThrow(subjectOfTaskNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                patch(String.format("/dailyschedule/learning-times/%s/subject-of-task", LEARNING_TIME_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(learningTimeSubjectOfTaskModifyRequest)
                        )
        );

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(subjectOfTaskNotFoundException.getMessage()));

        verify(learningTimeService).changeSubjectOfTask(any(), any());
    }

    @Test
    void 학습_시간의_Task_학습_주제_변경을_요청할_때_SubTask_학습_주제가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        SubjectOfSubTaskNotFoundException subjectOfSubTaskNotFoundException = new SubjectOfSubTaskNotFoundException();

        LearningTimeSubjectOfSubTaskModifyRequest learningTimeSubjectOfSubTaskModifyRequest = createLearningTimeSubjectOfSubTaskModifyRequest(SUBJECT_OF_SUBTASK_ID);

        given(learningTimeService.changeSubjectOfSubTask(any(), any()))
                .willThrow(subjectOfSubTaskNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                patch(String.format("/dailyschedule/learning-times/%s/subject-of-subtask", LEARNING_TIME_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(learningTimeSubjectOfSubTaskModifyRequest)
                        )
        );

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(subjectOfSubTaskNotFoundException.getMessage()));

        verify(learningTimeService).changeSubjectOfSubTask(any(), any());
    }

}
