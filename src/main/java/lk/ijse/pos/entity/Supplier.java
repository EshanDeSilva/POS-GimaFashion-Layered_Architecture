package lk.ijse.pos.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Supplier {
    private String supplierId;
    private String title;
    private String supplierName;
    private String company;
    private String contact;
}
