package lk.ijse.pos.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaymentDto {
    private String paymentId;
    private double cash;
    private boolean payByCash;
    private double balance;
    private String date;
    private String orderId;
}
