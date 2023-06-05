package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.entity.OrderDetails;

import java.sql.SQLException;
import java.util.List;

public interface OrderDetailsDao extends CrudDao<OrderDetails,String> {
    double getDailyGentsSaleCount() throws SQLException, ClassNotFoundException;
    double getDailyLadiesSaleCount() throws SQLException, ClassNotFoundException;
    double getDailyKidsSaleCount() throws SQLException, ClassNotFoundException;
    double getDailyOtherSaleCount() throws SQLException, ClassNotFoundException;
    List<OrderDetails> findAll(String id) throws SQLException, ClassNotFoundException;
    double getMonthlySalesCount(String month) throws SQLException, ClassNotFoundException;
    int getMonthlySalesCount() throws SQLException, ClassNotFoundException;
    int getDailySalesCount(int date) throws SQLException, ClassNotFoundException;
    double getAnnualIncome() throws SQLException, ClassNotFoundException;
    int getAnnualSalesCount() throws SQLException, ClassNotFoundException;
    double getMonthlyIncome() throws SQLException, ClassNotFoundException;
    int getDailySalesCount() throws SQLException, ClassNotFoundException;
    double getDailyIncome() throws SQLException, ClassNotFoundException;
}
