package dao;

import domain.ProductStock;
import org.springframework.stereotype.Component;

@Component
public class ProductStockDAO extends AbstractDAO<ProductStock> {

    public ProductStockDAO() {
        super(ProductStock.class);
    }

    public void create(ProductStock entity) {
        super.create(entity);
    }

    public ProductStock find(int id) {
        return super.find(id);
    }
}
