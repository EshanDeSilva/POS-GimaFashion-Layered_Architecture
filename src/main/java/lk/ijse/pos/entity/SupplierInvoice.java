package lk.ijse.pos.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SupplierInvoice {
    private String invoiceId;
    private String supplierId;
    private String itemCode;
    private String date;
    private String qty;
}
