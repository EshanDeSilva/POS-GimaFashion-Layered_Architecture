package lk.ijse.pos.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetails {
    private String orderId;
    private String itemCode;
    private int orderQty;
    private double unitPrice;
    private double totalProfit;
    private double discountRate;
}
