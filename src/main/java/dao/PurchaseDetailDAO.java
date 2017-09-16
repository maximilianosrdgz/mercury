package dao;

import domain.PurchaseDetail;
import org.springframework.stereotype.Component;

/**
 * Created by MaxPower on 12/09/2017.
 */
@Component
public class PurchaseDetailDAO extends AbstractDAO<PurchaseDetail> {

    public PurchaseDetailDAO() {
        super(PurchaseDetail.class);
    }

    public void create(PurchaseDetail entity) {
        super.create(entity);
    }

    public PurchaseDetail find(int id) {
        return super.find(id);
    }
}
