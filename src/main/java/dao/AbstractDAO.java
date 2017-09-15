package dao;

import controller.EntityManagerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * Created by MaxPower on 12/09/2017.
 */
@Repository
public abstract class AbstractDAO<T> {

    private Class<T> entityClass;
    //@Autowired
    private EntityManagerUtils emu;

    public AbstractDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void create(T entity) {
        emu = new EntityManagerUtils();
        EntityManager em = emu.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
        finally {
            em.close();
        }

        if(entity != null) {
            System.out.println(entity);
        }
    }

    public T find(int id) {
        emu = new EntityManagerUtils();
        EntityManager em = emu.getEntityManager();
        T entity = null;
        try {
            em.getTransaction().begin();
            entity = em.find(entityClass, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
        return entity;
    }
}
