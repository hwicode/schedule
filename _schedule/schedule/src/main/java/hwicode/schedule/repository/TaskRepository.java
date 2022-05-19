package hwicode.schedule.repository;

import hwicode.schedule.domain.Task;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class TaskRepository {

    private final EntityManager entityManager;

    public TaskRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Task task) {
        entityManager.persist(task);
    }

    public Task findOne(Long id) {
        return entityManager.find(Task.class, id);
    }
}
