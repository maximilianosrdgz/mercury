package controller;

import domain.Category;
import domain.Client;
import domain.Location;
import domain.Material;
import domain.Product;
import domain.Purchase;
import domain.PurchaseDetail;

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
public class EntityManagerUtils {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");
    EntityManager em = emf.createEntityManager();
    Client client = null;

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
    }

    private Client buildClient() {/*
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

        List<Purchase> purchases = new ArrayList();
        purchases.add(purchase);*/

        return Client.builder()
                .name("Clientín")
                .email("email@domain.com")
                .location(buildLocation())
                .birthYear(1991)
                .buyer(true)
                .consultant(true)
                //.purchases(purchases)
                .build();
    }

    private Category buildCategory() {
        return Category.builder()
                .description("Category 1")
                .product(false)
                .build();
    }

    private Location buildLocation() {
        return Location.builder()
                .city("Cordoba Capital")
                .province("Cordoba")
                .address("Fake Street 123")
                .build();
    }

    private Material buildMaterial() {
        Set<Category> categories = new HashSet();
        categories.add(buildCategory());

        return Material.builder()
                .description("Material 1")
                .categories(categories)
                .build();
    }

    private Product buildProduct() {
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

    private PurchaseDetail buildPurchaseDetail() {
        return PurchaseDetail.builder()
                .purchaseId(0)
                .product(buildProduct())
                .quantity(1)
                .unitPrice(10)
                .build();
    }

    private Purchase buildPurchase() {
        List<PurchaseDetail> details = new ArrayList();
        details.add(buildPurchaseDetail());

        return Purchase.builder()
                .date(new Date())
                .purchaseDetails(details)
                .build();
    }
}
