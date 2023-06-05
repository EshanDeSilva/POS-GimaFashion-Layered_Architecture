package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.bo.BoFactory;
import lk.ijse.pos.bo.custom.OrderBo;
import lk.ijse.pos.bo.custom.OrderDetailsBo;
import lk.ijse.pos.bo.custom.PaymentBo;
import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.dao.custom.OrderDao;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.entity.Orders;
import lk.ijse.pos.model.OrderDetailsDto;
import lk.ijse.pos.model.OrderDto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderBoImpl implements OrderBo {
    OrderDao orderDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.ORDER);
    PaymentBo paymentBo = BoFactory.getInstance().getBoType(BoFactory.BoType.PAYMENT_BO);
    OrderDetailsBo orderDetailsBo = BoFactory.getInstance().getBoType(BoFactory.BoType.ORDER_DETAILS_BO);

    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        return orderDao.getId();
    }

    @Override
    public boolean saveOrder(OrderDto dto) throws SQLException {
        Connection connection=null;
        try{
            connection= DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean orderSaved = orderDao.save(new Orders(dto.getOrderId(),dto.getDate(),dto.getTotalDiscount(),dto.getTotal(),
                    dto.getEmployerId(),dto.getCustomerName(),dto.getCustomerEmail(),dto.getCustomerContact()));
            if (orderSaved) {
                boolean paymentSaved = paymentBo.savePayment(dto.getPaymentDto());
                if (paymentSaved) {
                    boolean detailSaved = true;
                    for (OrderDetailsDto dto1 : dto.getDetailDto()) {
                        if (!orderDetailsBo.saveOrderDetail(dto1)) {
                            detailSaved = false;
                        }
                    }
                    //System.out.println("detail" + detailSaved);
                    if (detailSaved) {
                        connection.commit();
                        return true;
                    }
                }
            }
            return false;
        }catch (SQLException | ClassNotFoundException er) {
            connection.rollback();
            er.printStackTrace();
            return false;
        } finally {
            //System.out.println("finally");
            connection.setAutoCommit(true);
        }
    }

    @Override
    public List<OrderDto> findAllOrders() throws SQLException, ClassNotFoundException {
        List<OrderDto> dtoList = new ArrayList<>();
        for (Orders order:orderDao.findAll()) {
            dtoList.add(new OrderDto(
                    order.getOrderId(),order.getDate(),order.getTotalDiscount(),order.getTotal(),order.getEmployerId(),
                    order.getCustomerName(),order.getCustomerEmail(),order.getCustomerContact(),
                    orderDetailsBo.findAllOrderDetails(order.getOrderId()),paymentBo.getPayments(order.getOrderId())
            ));
        }
        return dtoList;
    }

    @Override
    public double getDailySalesTotal() throws SQLException, ClassNotFoundException {
        return orderDao.getDailySalesTotal();
    }

    @Override
    public double getMonthlySalesTotal() throws SQLException, ClassNotFoundException {
        return orderDao.getMonthlySalesTotal();
    }

    @Override
    public double getAnnualSalesTotal() throws SQLException, ClassNotFoundException {
        return orderDao.getAnnualSalesTotal();
    }
}
