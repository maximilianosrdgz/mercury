package persistence;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by MaxPower on 09/09/2017.
 */
@Component
public class EntityManagerUtils {

    private EntityManager em;

    public EntityManager getEntityManager() {
        if(em == null) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");
            em = emf.createEntityManager();
        }
        return em;
    }
}
