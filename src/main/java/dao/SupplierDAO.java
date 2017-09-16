package dao;

import domain.Supplier;
import org.springframework.stereotype.Component;

/**
 * Created by MaxPower on 12/09/2017.
 */
@Component
public class SupplierDAO extends AbstractDAO<Supplier> {

    public SupplierDAO() {
        super(Supplier.class);
    }

    public void create(Supplier entity) {
        super.create(entity);
    }

    public Supplier find(int id) {
        return super.find(id);
    }
}
