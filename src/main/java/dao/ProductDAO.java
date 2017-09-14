package dao;

import controller.EntityManagerUtils;
import domain.Product;

/**
 * Created by MaxPower on 12/09/2017.
 */
public class ProductDAO extends AbstractDAO<Product> {

    public ProductDAO(EntityManagerUtils emu) {
        super(Product.class);
    }

    public void create(Product entity) {
        super.create(entity);
    }

    public Product find(int id) {
        return super.find(id);
    }
}
