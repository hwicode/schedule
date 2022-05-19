package hwicode.schedule.repository;

import hwicode.schedule.domain.DetailTask;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class DetailTaskRepository {

    private final EntityManager entityManager;

    public DetailTaskRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(DetailTask detailTask) {
        entityManager.persist(detailTask);
    }

    public DetailTask findOne(Long id) {
        return entityManager.find(DetailTask.class, id);
    }
}
