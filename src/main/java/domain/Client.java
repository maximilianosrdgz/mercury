package domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by MaxPower on 31/08/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private String id;
    private String name;
    private String email;
    private Location location;
    private int birthYear;
    private boolean buyer;
    private boolean consultant;
    private List<Purchase> purchases;
}
