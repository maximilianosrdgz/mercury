package dao;

import domain.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreDAO extends AbstractDAO<Store> {

    public StoreDAO() {
        super(Store.class);
    }

    public void create(Store entity) {
        super.create(entity);
    }

    public Store find(int id) {
        return super.find(id);
    }
}
