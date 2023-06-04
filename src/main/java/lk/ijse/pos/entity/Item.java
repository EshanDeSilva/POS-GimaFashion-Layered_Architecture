package lk.ijse.pos.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Item {
    private String itemCode;
    private String supplierId;
    private String description;
    private int qtyOnHand;
    private double sellingPrice;
    private double buyingPrice;
    private String categoryId;
}
