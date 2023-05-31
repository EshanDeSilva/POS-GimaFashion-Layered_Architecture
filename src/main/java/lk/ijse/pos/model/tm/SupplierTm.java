package lk.ijse.pos.model.tm;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SupplierTm extends RecursiveTreeObject<SupplierTm> {
    private String supplierId;
    private String title;
    private String name;
    private String company;
    private String contact;
    private JFXButton btn;
}
