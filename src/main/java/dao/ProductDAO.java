package dao;

import domain.Product;
import org.springframework.stereotype.Component;

/**
 * Created by MaxPower on 12/09/2017.
 */
@Component
public class ProductDAO extends AbstractDAO<Product> {

    public ProductDAO() {
        super(Product.class);
    }

    public void create(Product entity) {
        super.create(entity);
    }

    public Product find(int id) {
        return super.find(id);
    }
}
