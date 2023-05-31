package lk.ijse.pos.model.tm;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SuppliesTm extends RecursiveTreeObject<SuppliesTm> {
    private String itemCode;
    private String description;
    private int qty;
}
