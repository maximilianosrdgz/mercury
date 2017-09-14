package dao;

import controller.EntityManagerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by MaxPower on 12/09/2017.
 */
@Repository
public abstract class AbstractDAO<T> {

    private Class<T> entityClass;
    @Autowired
    private EntityManagerUtils emu;

    public AbstractDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void create(T entity) {
        try {
            emu.getEntityManager().getTransaction().begin();
            emu.getEntityManager().persist(entity);
            emu.getEntityManager().getTransaction().commit();
        } catch (Exception e) {
            emu.getEntityManager().getTransaction().rollback();
        }

        if(entity != null) {
            System.out.println(entity);
        }
    }

    public T find(int id) {
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
}
