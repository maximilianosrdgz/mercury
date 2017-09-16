package dao;

import domain.Purchase;
import org.springframework.stereotype.Component;

/**
 * Created by MaxPower on 12/09/2017.
 */
@Component
public class PurchaseDAO extends AbstractDAO<Purchase> {

    public PurchaseDAO() {
        super(Purchase.class);
    }

    public void create(Purchase entity) {
        super.create(entity);
    }

    public Purchase find(int id) {
        return super.find(id);
    }
}
