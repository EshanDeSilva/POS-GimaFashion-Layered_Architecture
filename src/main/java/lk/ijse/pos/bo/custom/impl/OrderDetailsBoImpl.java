package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.bo.custom.OrderDetailsBo;
import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.dao.custom.OrderDetailsDao;
import lk.ijse.pos.entity.OrderDetails;
import lk.ijse.pos.model.OrderDetailsDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsBoImpl implements OrderDetailsBo {
    OrderDetailsDao orderDetailsDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.ORDER_DETAILS);

    @Override
    public boolean saveOrderDetail(OrderDetailsDto dto) throws SQLException, ClassNotFoundException {
        return orderDetailsDao.save(new OrderDetails(dto.getOrderId(),dto.getItemCode(),
                dto.getOrderQty(),dto.getUnitPrice(),dto.getTotalProfit(),dto.getDiscRate()));
    }

    @Override
    public double getDailyGentsSaleCount() throws SQLException, ClassNotFoundException {
        return orderDetailsDao.getDailyGentsSaleCount();
    }

    @Override
    public double getDailyLadiesSaleCount() throws SQLException, ClassNotFoundException {
        return orderDetailsDao.getDailyLadiesSaleCount();
    }

    @Override
    public double getDailyKidsSaleCount() throws SQLException, ClassNotFoundException {
        return orderDetailsDao.getDailyKidsSaleCount();
    }

    @Override
    public double getDailyOtherSaleCount() throws SQLException, ClassNotFoundException {
        return orderDetailsDao.getDailyOtherSaleCount();
    }

    @Override
    public List<OrderDetailsDto> findAllOrderDetails(String orderId) throws SQLException, ClassNotFoundException {
        List<OrderDetailsDto> dtoList = new ArrayList<>();
        for (OrderDetails entity:orderDetailsDao.findAll(orderId)) {
            dtoList.add(new OrderDetailsDto(entity.getOrderId(),entity.getItemCode(),entity.getOrderQty(), entity.getUnitPrice(),entity.getTotalProfit(),entity.getDiscountRate()));
        }
        return dtoList;
    }

    @Override
    public double getMonthlySalesCount(String month) throws SQLException, ClassNotFoundException {
        return orderDetailsDao.getMonthlySalesCount(month);
    }

    @Override
    public int getMonthlySalesCount() throws SQLException, ClassNotFoundException {
        return orderDetailsDao.getMonthlySalesCount();
    }

    @Override
    public int getDailySalesCount(int date) throws SQLException, ClassNotFoundException {
        return orderDetailsDao.getDailySalesCount(date);
    }

    @Override
    public double getAnnualIncome() throws SQLException, ClassNotFoundException {
        return orderDetailsDao.getAnnualIncome();
    }

    @Override
    public int getAnnualSalesCount() throws SQLException, ClassNotFoundException {
        return orderDetailsDao.getAnnualSalesCount();
    }

    @Override
    public double getMonthlyIncome() throws SQLException, ClassNotFoundException {
        return orderDetailsDao.getMonthlyIncome();
    }

    @Override
    public int getDailySalesCount() throws SQLException, ClassNotFoundException {
        return orderDetailsDao.getDailySalesCount();
    }

    @Override
    public double getDailyIncome() throws SQLException, ClassNotFoundException {
        return orderDetailsDao.getDailyIncome();
    }
}
