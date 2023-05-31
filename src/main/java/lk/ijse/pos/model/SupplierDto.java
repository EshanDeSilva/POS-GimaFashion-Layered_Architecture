package lk.ijse.pos.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SupplierDto {
    private String supplierId;
    private String title;
    private String name;
    private String company;
    private String contact;
}
