package controller;

import domain.Category;
import domain.Client;
import domain.Location;
import domain.Material;
import domain.Product;
import domain.Purchase;
import domain.PurchaseDetail;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by MaxPower on 09/09/2017.
 */
@Component
public class EntityManagerUtils {

    Client client = null;

    public EntityManager getEntityManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");
        return emf.createEntityManager();
    }
    /*
    public void saveClient() {
        try {
            em.getTransaction().begin();
            em.persist(buildClient());
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }

        if(client != null) {
            System.out.println(client);
        }
    }

    public Client findClient(int clientId) {
        Client client = null;
        try {
            em.getTransaction().begin();
            client = em.find(Client.class, clientId);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
        return client;
    }

    public void deleteClient(int clientId) {
        try {
            em.getTransaction().begin();
            Client client = em.find(Client.class, clientId);
            em.remove(client);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    public void saveCategory() {
        try {
            em.getTransaction().begin();
            em.persist(buildCategory());
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }

        if(client != null) {
            System.out.println(client);
        }
    }

    public void saveLocation() {
        try {
            em.getTransaction().begin();
            em.persist(buildLocation());
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }

        if(client != null) {
            System.out.println(client);
        }
    }

    public void saveMaterial() {
        try {
            em.getTransaction().begin();
            em.persist(buildMaterial());
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }

        if(client != null) {
            System.out.println(client);
        }
    }

    public void saveProduct() {
        try {
            em.getTransaction().begin();
            em.persist(buildProduct());
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }

        if(client != null) {
            System.out.println(client);
        }
    }

    public void savePurchase() {
        try {
            em.getTransaction().begin();
            em.persist(buildPurchase());
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }

        if(client != null) {
            System.out.println(client);
        }
    }

    public void savePurchaseDetail() {
        try {
            em.getTransaction().begin();
            em.persist(buildPurchaseDetail());
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }

        if(client != null) {
            System.out.println(client);
        }
    }

    public void saveSupplier() {
        try {
            em.getTransaction().begin();
            em.persist(buildClient());
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }

        if(client != null) {
            System.out.println(client);
        }
    }*/

    public Client buildClient() {/*
        Location location = buildLocation();

        Category category = buildCategory();

        Set<Category> categories = new HashSet();
        categories.add(category);

        Material material = buildMaterial();

        Set<Material> materials = new HashSet();
        materials.add(material);

        Product product = buildProduct();

        PurchaseDetail detail = buildPurchaseDetail();

        List<PurchaseDetail> details = new ArrayList();
        details.add(detail);

        Purchase purchase = buildPurchase();
        */
        List<Purchase> purchases = new ArrayList();
        purchases.add(buildPurchase());

        return Client.builder()
                .name("Clientín")
                .email("email@domain.com")
                .provinceId(1)
                .birthYear(1991)
                .buyer(true)
                .consultant(true)
                //.purchases(purchases)
                .build();
    }

    public Category buildBaseCategory() {
        return Category.builder()
                .description("Category 1")
                .product(false)
                .build();
    }

    public Category buildCategory() {
        return Category.builder()
                .id(1)
                .description("Category 1")
                .product(false)
                .build();
    }

    public Location buildLocation() {
        return Location.builder()
                .city("Cordoba Capital")
                .provinceId(1)
                .address("Fake Street 123")
                .build();
    }

    public Material buildMaterial() {
        Set<Category> categories = new HashSet();
        categories.add(buildCategory());

        return Material.builder()
                .description("Material 1")
                .categories(categories)
                .build();
    }

    public Product buildProduct() {
        Set<Category> categories = new HashSet();
        categories.add(buildCategory());

        Set<Material> materials = new HashSet();
        materials.add(buildMaterial());

        return Product.builder()
                .description("Product 1")
                .price(200)
                .categories(categories)
                .materials(materials)
                .build();
    }

    public PurchaseDetail buildPurchaseDetail() {
        return PurchaseDetail.builder()
                .purchaseId(0)
                .product(buildProduct())
                .quantity(1)
                .unitPrice(10)
                .build();
    }

    public Purchase buildPurchase() {
        List<PurchaseDetail> details = new ArrayList();
        details.add(buildPurchaseDetail());

        return Purchase.builder()
                .date(new Date())
                .purchaseDetails(details)
                .build();
    }
}
