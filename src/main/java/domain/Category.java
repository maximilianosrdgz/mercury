package domain;

import lombok.Builder;
import lombok.Data;

/**
 * Created by MaxPower on 03/09/2017.
 */
@Data
@Builder
public class Category {
    private int id;
    private String description;
    private boolean product;
}
