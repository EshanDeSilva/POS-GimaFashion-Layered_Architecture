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
    private String qtyOnHand;
    private String sellingPrice;
    private String buyingPrice;
    private String categoryId;
}
