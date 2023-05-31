package lk.ijse.pos.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetailsDto {
    private String orderId;
    private String itemCode;
    private int orderQty;
    private double unitPrice;
    private double totalProfit;
    private double discRate;
}
