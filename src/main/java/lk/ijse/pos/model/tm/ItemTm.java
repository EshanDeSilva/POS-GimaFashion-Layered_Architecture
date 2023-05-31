package lk.ijse.pos.model.tm;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ItemTm extends RecursiveTreeObject<ItemTm> {
    private String code;
    private String description;
    private String qty;
    private String sellingPrice;
    private String buyingPrice;
    private String size;
    private String type;
    private String profit;
    private String supplierId;
    private JFXButton btn;
}
