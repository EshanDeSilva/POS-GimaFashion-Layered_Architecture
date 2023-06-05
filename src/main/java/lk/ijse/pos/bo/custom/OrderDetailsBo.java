package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBo;
import lk.ijse.pos.model.OrderDetailsDto;
import java.sql.SQLException;
import java.util.List;

public interface OrderDetailsBo extends SuperBo {
    boolean saveOrderDetail(OrderDetailsDto dto) throws SQLException, ClassNotFoundException;
    double getDailyGentsSaleCount() throws SQLException, ClassNotFoundException;
    double getDailyLadiesSaleCount() throws SQLException, ClassNotFoundException;
    double getDailyKidsSaleCount() throws SQLException, ClassNotFoundException;
    double getDailyOtherSaleCount() throws SQLException, ClassNotFoundException;
    List<OrderDetailsDto> findAllOrderDetails(String orderId) throws SQLException, ClassNotFoundException;
    double getMonthlySalesCount(String month) throws SQLException, ClassNotFoundException;
    int getMonthlySalesCount() throws SQLException, ClassNotFoundException;
    int getDailySalesCount(int date) throws SQLException, ClassNotFoundException;
    double getAnnualIncome() throws SQLException, ClassNotFoundException;
    int getAnnualSalesCount() throws SQLException, ClassNotFoundException;
    double getMonthlyIncome() throws SQLException, ClassNotFoundException;
    int getDailySalesCount() throws SQLException, ClassNotFoundException;
    double getDailyIncome() throws SQLException, ClassNotFoundException;
}
