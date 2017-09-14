package dao;

import controller.EntityManagerUtils;
import domain.Purchase;

/**
 * Created by MaxPower on 12/09/2017.
 */
public class PurchaseDAO extends AbstractDAO<Purchase> {

    public PurchaseDAO(EntityManagerUtils emu) {
        super(Purchase.class);
    }

    public void create(Purchase entity) {
        super.create(entity);
    }

    public Purchase find(int id) {
        return super.find(id);
    }
}
