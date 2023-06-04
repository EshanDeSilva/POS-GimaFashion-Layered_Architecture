package lk.ijse.pos.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SuppliesDto {
    private String itemCode;
    private String description;
    private int qty;
}
