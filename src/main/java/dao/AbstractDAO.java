package dao;

import persistence.EntityManagerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        try {
            emu.getEntityManager().getTransaction().begin();
            emu.getEntityManager().persist(entity);
            emu.getEntityManager().getTransaction().commit();
        } catch (Exception e) {
            emu.getEntityManager().getTransaction().rollback();
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

    public List<T> findByQuery(String statement) {
        List<T> results = new ArrayList<>();
        try {
            Query query = emu.getEntityManager().createQuery(statement);
            results = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<T> findAll() {
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

    public T findByProductAndMaterialId(int productId, int materialId) {
        List<T> result = new ArrayList<>();
        try {
            Query query = emu.getEntityManager()
                    .createQuery("SELECT qu FROM MaterialQuantity qu WHERE qu.product.id = ?1 AND qu.material.id = ?2");
            query.setParameter(1, productId);
            query.setParameter(2, materialId);
            result = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(result.size() == 0) {
            return null;
        }
        else {
            return result.get(0);
        }
    }
}
