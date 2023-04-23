package hwicode.schedule.dailyschedule.timetable.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.timetable.application.TimeTableService;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.TimeTableController;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save.LearningTimeSaveRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save.LearningTimeSaveResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static hwicode.schedule.dailyschedule.timetable.TimeTableDataHelper.START_TIME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        Long timeTableId = 1L;
        Long learningTimeId = 1L;
        LearningTimeSaveRequest learningTimeSaveRequest = new LearningTimeSaveRequest(START_TIME);
        LearningTimeSaveResponse learningTimeSaveResponse = new LearningTimeSaveResponse(learningTimeId, START_TIME);

        given(timeTableService.saveLearningTime(any(), any()))
                .willReturn(learningTimeId);

        // when
        ResultActions perform = mockMvc.perform(
                post(String.format("/dailyschedule/timetables/%s", timeTableId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(learningTimeSaveRequest)
                        ));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(learningTimeSaveResponse)
                ));

        verify(timeTableService).saveLearningTime(any(), any());
    }

}


