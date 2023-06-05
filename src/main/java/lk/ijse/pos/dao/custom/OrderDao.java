package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.entity.Orders;

import java.sql.SQLException;

public interface OrderDao extends CrudDao<Orders,String> {
    double getDailySalesTotal() throws SQLException, ClassNotFoundException;
    double getMonthlySalesTotal() throws SQLException, ClassNotFoundException;
    double getAnnualSalesTotal() throws SQLException, ClassNotFoundException;
}
