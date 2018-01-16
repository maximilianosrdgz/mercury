package dao;

import domain.Client;
import org.springframework.stereotype.Component;

import javax.management.Query;
import java.util.List;

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

    public List<Client> findByName(String name) {
        String statement = String.format("SELECT client FROM Client client WHERE client.name = '%s'", name);
        return super.findByQuery(statement);
    }

    public List<Client> findByEmail(String email) {
        String statement = String.format("SELECT client FROM Client client WHERE client.email = '%s'", email);
        return super.findByQuery(statement);
    }
}
