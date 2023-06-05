package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.entity.Payment;
import lk.ijse.pos.model.PaymentDto;

import java.sql.SQLException;
import java.util.List;

public interface PaymentDao extends CrudDao<Payment,String> {
    boolean save(List<Payment> dtos) throws SQLException, ClassNotFoundException;
    List<Payment> getPayments(String id) throws SQLException, ClassNotFoundException;
}
