package lk.ijse.pos.model.tm;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployerTm extends RecursiveTreeObject<EmployerTm> {
    private String id;
    private String title;
    private String name;
    private String nic;
    private String dob;
    private String address;
    private String bankAcc;
    private String bankBranch;
    private String contact;
    private JFXButton btn;
}
