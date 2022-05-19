package hwicode.schedule.repository;

import hwicode.schedule.domain.Schedule;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ScheduleRepository {

    private final EntityManager entityManager;

    public ScheduleRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Schedule schedule) {
        entityManager.persist(schedule);
    }

    public Schedule findOne(Long id) {
        return entityManager.find(Schedule.class, id);
    }

    public List<Schedule> findByDate(String date) {
        return entityManager.createQuery("select s from Schedule s where s.scheduleDate = :date", Schedule.class)
                .setParameter("date", date)
                .getResultList();
    }
}
