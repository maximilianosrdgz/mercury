package domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by MaxPower on 03/09/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @Temporal(value = TemporalType.DATE)
    private Date date;

    @OneToMany(fetch = FetchType.EAGER)//(cascade = CascadeType.PERSIST)
    private List<PurchaseDetail> purchaseDetails;

    @Column
    private boolean canceled;

    public boolean getBooleanCanceled() {
        return canceled;
    }

    public String isCanceled() {
        if(canceled) {
            return "Anulada";
        }
        else {
            return "Válida";
        }
    }
}
