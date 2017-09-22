package dao;

import domain.MaterialStock;
import org.springframework.stereotype.Component;

@Component
public class MaterialStockDAO extends AbstractDAO<MaterialStock> {

    public MaterialStockDAO() {
        super(MaterialStock.class);
    }

    public void create(MaterialStock entity) {
        super.create(entity);
    }

    public MaterialStock find(int id) {
        return super.find(id);
    }
}
