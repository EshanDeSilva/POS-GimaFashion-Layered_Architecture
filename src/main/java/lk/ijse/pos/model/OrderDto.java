package lk.ijse.pos.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDto {
    private String orderId;
    private String date;
    private double totalDiscount;
    private double total;
    private String employerId;
    private String customerName;
    private String customerEmail;
    private String customerContact;
    private List<OrderDetailsDto> detailDto;
    private List<PaymentDto> paymentDto;
}
