package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBo;
import lk.ijse.pos.model.PaymentDto;

import java.sql.SQLException;
import java.util.List;

public interface PaymentBo extends SuperBo {
    String getId() throws SQLException, ClassNotFoundException;
    boolean savePayment(List<PaymentDto> dtos) throws SQLException, ClassNotFoundException;
    List<PaymentDto> getPayments(String id) throws SQLException, ClassNotFoundException;
}
