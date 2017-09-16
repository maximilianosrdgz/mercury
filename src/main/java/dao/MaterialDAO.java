package dao;

import domain.Material;
import org.springframework.stereotype.Component;

/**
 * Created by MaxPower on 12/09/2017.
 */
@Component
public class MaterialDAO extends AbstractDAO<Material> {

    public MaterialDAO() {
        super(Material.class);
    }

    public void create(Material entity) {
        super.create(entity);
    }

    public Material find(int id) {
        return super.find(id);
    }
}
