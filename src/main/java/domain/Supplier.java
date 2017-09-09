package domain;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Created by MaxPower on 03/09/2017.
 */
@Data
@Builder
public class Supplier {
    private int id;
    private String name;
    private Location location;
    private Set<Category> categories;
}
