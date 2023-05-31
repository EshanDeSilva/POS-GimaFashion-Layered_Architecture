package lk.ijse.pos.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Orders {
    private String orderId;
    private String date;
    private double totalDiscount;
    private double total;
    private String employerId;
    private String customerName;
    private String customerEmail;
    private String customerContact;
}
