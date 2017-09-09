package domain;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Created by MaxPower on 03/09/2017.
 */
@Data
@Builder
public class Product {
    private int id;
    private String description;
    private double price;
    private Set<Category> categories;
    private Set<Material> materials;
}
