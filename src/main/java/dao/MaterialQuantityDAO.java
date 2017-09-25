package dao;

import domain.MaterialQuantity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MaterialQuantityDAO extends AbstractDAO<MaterialQuantity> {

    public MaterialQuantityDAO() {
        super(MaterialQuantity.class);
    }

    public void create(MaterialQuantity entity) {
        super.create(entity);
    }

    public MaterialQuantity find(int id) {
        return super.find(id);
    }

    public List<MaterialQuantity> findByProductId(int id) {
        String statement = String.format("SELECT quantity FROM MaterialQuantity quantity WHERE quantity.product.id = '%d'", id);
        return super.findByQuery(statement);
    }
}
