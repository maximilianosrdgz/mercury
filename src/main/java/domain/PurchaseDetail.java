package domain;

import lombok.Builder;
import lombok.Data;

/**
 * Created by MaxPower on 03/09/2017.
 */
@Data
@Builder
public class PurchaseDetail {
    private int id;
    private int purchaseId;
    private Product product;
    private int quantity;
    private double unitPrice;
}
