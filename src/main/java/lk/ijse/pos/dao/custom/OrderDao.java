package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.model.OrderDto;

import java.sql.SQLException;

public interface OrderDao extends CrudDao<OrderDto,String> {
    double getDailySalesTotal() throws SQLException, ClassNotFoundException;
    double getMonthlySalesTotal() throws SQLException, ClassNotFoundException;
    double getAnnualSalesTotal() throws SQLException, ClassNotFoundException;
}
