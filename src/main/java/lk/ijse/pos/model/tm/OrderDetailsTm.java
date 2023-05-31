package lk.ijse.pos.model.tm;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetailsTm extends RecursiveTreeObject<OrderDetailsTm> {
    private String orderId;
    private String date;
    private double total;
    private String customerName;
    private String contact;
    private String email;
    private String employer;
    private double arrears;
}
