package dao;

import controller.EntityManagerUtils;
import domain.Material;

/**
 * Created by MaxPower on 12/09/2017.
 */
public class MaterialDAO extends AbstractDAO<Material> {

    public MaterialDAO(EntityManagerUtils emu) {
        super(Material.class);
    }

    public void create(Material entity) {
        super.create(entity);
    }

    public Material find(int id) {
        return super.find(id);
    }
}
