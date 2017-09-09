package domain;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Created by MaxPower on 03/09/2017.
 */
@Data
@Builder
public class Material {
    private int id;
    private String description;
    private Set<Supplier> suppliers;
    private Set<Category> categories;
}
