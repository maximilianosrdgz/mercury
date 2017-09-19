package domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Calendar;
import java.util.List;

/**
 * Created by MaxPower on 31/08/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String email;

    @ManyToOne//(cascade = CascadeType.PERSIST)
    private Province province;

    @Column
    private int birthYear;

    @Column
    private boolean buyer;

    @Column
    private boolean consultant;

    @OneToMany
    private List<Purchase> purchases;

    @Column
    private boolean blackListed;

    public int getAge() {
        return Calendar.getInstance().get(Calendar.YEAR) - birthYear;
    }

    public boolean getBooleanBuyer() {
        return buyer;
    }

    public boolean getBooleanConsultant() {
        return consultant;
    }

    public boolean getBooleanBlacklisted() {
        return blackListed;
    }

    public String isBuyer() {
        if(buyer) {
            return "Sí";
        }
        else {
            return "No";
        }
    }

    public String isConsultant() {
        if(consultant) {
            return "Sí";
        }
        else {
            return "No";
        }
    }

    public String isBlacklist() {
        if(blackListed) {
            return "Sí";
        }
        else {
            return "No";
        }
    }
}
