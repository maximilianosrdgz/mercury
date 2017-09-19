package dao;

import persistence.EntityManagerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Created by MaxPower on 12/09/2017.
 */
@Component
public abstract class AbstractDAO<T> {

    private Class<T> entityClass;
    @Autowired
    private EntityManagerUtils emu;

    public AbstractDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void create(T entity) {
        //EntityManager em = emu.getEntityManager();
        try {
            emu.getEntityManager().getTransaction().begin();
            emu.getEntityManager().persist(entity);
            emu.getEntityManager().getTransaction().commit();
        } catch (Exception e) {
            emu.getEntityManager().getTransaction().rollback();
        }
    }

    public T find(int id) {
        //EntityManager em = emu.getEntityManager();
        T entity = null;
        try {
            emu.getEntityManager().getTransaction().begin();
            entity = emu.getEntityManager().find(entityClass, id);
            emu.getEntityManager().getTransaction().commit();
        } catch (Exception e) {
            emu.getEntityManager().getTransaction().rollback();
        }
        return entity;
    }

    public List<T> findAll() {
        //EntityManager em = emu.getEntityManager();
        CriteriaQuery<T> cq = emu.getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return emu.getEntityManager().createQuery(cq).getResultList();
    }

    public void update(T entity) {
        try {
            emu.getEntityManager().getTransaction().begin();
            emu.getEntityManager().merge(entity);
            emu.getEntityManager().getTransaction().commit();
        } catch (Exception e) {
            emu.getEntityManager().getTransaction().rollback();
        }
    }
}
