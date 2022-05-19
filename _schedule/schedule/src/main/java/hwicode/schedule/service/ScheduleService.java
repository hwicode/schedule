package hwicode.schedule.service;

import hwicode.schedule.domain.Schedule;
import hwicode.schedule.domain.User;
import hwicode.schedule.repository.ScheduleRepository;
import hwicode.schedule.service.response.ScheduleIdResponse;
import hwicode.schedule.service.response.ScheduleResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional(readOnly = true)
    public ScheduleResponse findById(Long id) {
        Schedule schedule = scheduleRepository.findOne(id);
        return schedule.toDto();
    }

    @Transactional
    public ScheduleIdResponse makeSchedule(String date) {
        // 더미 유저 생성
        User user = new User();

        List<Schedule> foundSchedule = scheduleRepository.findByDate(date);
        if (!foundSchedule.isEmpty()) {
            throw new IllegalStateException("계획표가 이미 존재하는 날짜입니다.");
        }

        Schedule schedule = Schedule.createSchedule(user, date);
        scheduleRepository.save(schedule);
        return new ScheduleIdResponse(schedule.getId());
    }

    public void changeFeedback(Long scheduleId, String feedback) {
        Schedule schedule = scheduleRepository.findOne(scheduleId);
        schedule.setDayFeedback(feedback);
    }
}
