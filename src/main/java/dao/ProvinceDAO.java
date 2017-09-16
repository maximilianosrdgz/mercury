package dao;

import domain.Province;
import org.springframework.stereotype.Component;

/**
 * Created by MaxPower on 16/09/2017.
 */
@Component
public class ProvinceDAO extends AbstractDAO<Province> {

    public ProvinceDAO() {
        super(Province.class);
    }

    public void create(Province entity) {
        super.create(entity);
    }

    public Province find(int id) {
        return super.find(id);
    }
}
