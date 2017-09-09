package domain;

import lombok.Builder;
import lombok.Data;

/**
 * Created by MaxPower on 03/09/2017.
 */
@Data
@Builder
public class Location {
    private int id;
    private String city;
    private String province;
    private String address;
}
