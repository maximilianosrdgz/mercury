package dao;

import controller.EntityManagerUtils;
import domain.Location;

/**
 * Created by MaxPower on 12/09/2017.
 */
public class LocationDAO extends AbstractDAO<Location> {

    public LocationDAO(EntityManagerUtils emu) {
        super(Location.class);
    }

    public void create(Location entity) {
        super.create(entity);
    }

    public Location find(int id) {
        return super.find(id);
    }
}
