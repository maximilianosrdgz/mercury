package dao;

import controller.EntityManagerUtils;
import domain.PurchaseDetail;

/**
 * Created by MaxPower on 12/09/2017.
 */
public class PurchaseDetailDAO extends AbstractDAO<PurchaseDetail> {

    public PurchaseDetailDAO(EntityManagerUtils emu) {
        super(PurchaseDetail.class);
    }

    public void create(PurchaseDetail entity) {
        super.create(entity);
    }

    public PurchaseDetail find(int id) {
        return super.find(id);
    }
}
