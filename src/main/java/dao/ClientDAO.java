package dao;

import domain.Client;
import org.springframework.stereotype.Repository;

/**
 * Created by MaxPower on 12/09/2017.
 */
@Repository
public class ClientDAO extends AbstractDAO<Client> {

    public ClientDAO() {
        super(Client.class);
    }

    public void create(Client entity) {
        super.create(entity);
    }

    public Client find(int id) {
        return super.find(id);
    }
}
