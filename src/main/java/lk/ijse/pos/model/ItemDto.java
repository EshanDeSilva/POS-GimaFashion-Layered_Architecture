package lk.ijse.pos.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ItemDto {
    private String code;
    private String supplierId;
    private String description;
    private String qty;
    private String sellingPrice;
    private String buyingPrice;
    private CategoryDto categoryDto;
}
