package dao;

import domain.Client;
import org.springframework.stereotype.Component;

/**
 * Created by MaxPower on 12/09/2017.
 */
@Component
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

    public void update(Client client) {
        super.update(client);
    }
}
