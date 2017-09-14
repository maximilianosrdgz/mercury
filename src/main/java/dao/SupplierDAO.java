package dao;

import controller.EntityManagerUtils;
import domain.Supplier;

/**
 * Created by MaxPower on 12/09/2017.
 */
public class SupplierDAO extends AbstractDAO<Supplier> {

    public SupplierDAO(EntityManagerUtils emu) {
        super(Supplier.class);
    }

    public void create(Supplier entity) {
        super.create(entity);
    }

    public Supplier find(int id) {
        return super.find(id);
    }
}
