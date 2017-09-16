package dao;

import domain.Category;
import org.springframework.stereotype.Component;

/**
 * Created by MaxPower on 12/09/2017.
 */
@Component
public class CategoryDAO extends AbstractDAO<Category> {

    public CategoryDAO() {
        super(Category.class);
    }

    public void create(Category entity) {
        super.create(entity);
    }

    public Category find(int id) {
        return super.find(id);
    }
}
