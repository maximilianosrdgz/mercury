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

    public ProductStock findByProductId(int id) {
        String statement = String.format("SELECT stock FROM ProductStock stock WHERE stock.product.id = '%d'", id);
        return super.findByQuery(statement).get(0);
    }

    public void updateByProductId(int id) {
        super.update(this.findByProductId(id));
    }
}
