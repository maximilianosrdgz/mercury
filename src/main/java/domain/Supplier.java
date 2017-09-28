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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by MaxPower on 03/09/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @ManyToOne//(cascade = CascadeType.PERSIST)
    private Province province;

    @ManyToMany(fetch = FetchType.EAGER)//(cascade = CascadeType.MERGE)
    @JoinTable(name = "supplier_category")
    private Set<Category> categories;
}
