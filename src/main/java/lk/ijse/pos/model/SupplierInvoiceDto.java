package lk.ijse.pos.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SupplierInvoiceDto {
    private String invoiceId;
    private String supplierId;
    private String itemCode;
    private String date;
    private String qty;
    private ItemDto dto;
}
