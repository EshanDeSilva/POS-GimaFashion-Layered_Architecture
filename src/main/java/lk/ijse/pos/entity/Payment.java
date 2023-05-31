package lk.ijse.pos.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Payment {
    private String paymentId;
    private double cash;
    private boolean isPayByCash;
    private double balance;
    private String date;
    private String orderId;
}
