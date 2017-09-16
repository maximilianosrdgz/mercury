package dao;

import domain.Location;
import org.springframework.stereotype.Component;

/**
 * Created by MaxPower on 12/09/2017.
 */
@Component
public class LocationDAO extends AbstractDAO<Location> {

    public LocationDAO() {
        super(Location.class);
    }

    public void create(Location entity) {
        super.create(entity);
    }

    public Location find(int id) {
        return super.find(id);
    }
}
