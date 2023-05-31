package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.model.PaymentDto;

import java.sql.SQLException;
import java.util.List;

public interface PaymentDao extends CrudDao<PaymentDto,String> {
    boolean save(List<PaymentDto> dtos) throws SQLException, ClassNotFoundException;
    List<PaymentDto> getPayments(String id) throws SQLException, ClassNotFoundException;
}
