package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBo;
import lk.ijse.pos.model.OrderDto;

import java.sql.SQLException;
import java.util.List;

public interface OrderBo extends SuperBo {
    String getId() throws SQLException, ClassNotFoundException;
    boolean saveOrder(OrderDto dto) throws SQLException;
    List<OrderDto> findAllOrders() throws SQLException, ClassNotFoundException;
    double getDailySalesTotal() throws SQLException, ClassNotFoundException;
    double getMonthlySalesTotal() throws SQLException, ClassNotFoundException;
    double getAnnualSalesTotal() throws SQLException, ClassNotFoundException;
}
