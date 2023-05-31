package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.impl.util.CrudUtil;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.model.OrderDetailsDto;
import lk.ijse.pos.model.OrderDto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl {
    public static String getId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT orderId FROM orders ORDER BY orderId DESC LIMIT 1");
        if (resultSet.next()){
            int num = Integer.valueOf(resultSet.getString(1).split("-")[1]);
            return String.format("ORD-%08d",++num);
        }
        return "ORD-00000001";
    }

    public static Boolean save(OrderDto dto) throws SQLException {
        Connection connection=null;
        try{
            connection= DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            Boolean orderSaved = CrudUtil.execute("INSERT INTO orders VALUES (?,?,?,?,?,?,?,?)",
                    dto.getOrderId(), dto.getDate(), dto.getTotalDiscount(), dto.getTotal(), dto.getEmployerId(),
                    dto.getCustomerName(), dto.getCustomerEmail(), dto.getCustomerContact());
            if (orderSaved) {
                boolean paymentSaved = PaymentDaoImpl.save(dto.getPaymentDto());
                if (paymentSaved) {
                    Boolean detailSaved = true;
                    for (OrderDetailsDto dto1 : dto.getDetailDto()) {
                        if (!OrderDetailsDaoImpl.save(dto1)) {
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

    public static List<OrderDto> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM orders");
        List<OrderDto> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(new OrderDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    OrderDetailsDaoImpl.getAll(resultSet.getString(1)),
                    PaymentDaoImpl.getPayments(resultSet.getString(1))
            ));
        }
        return list;
    }

    public static double getDailySalesTotal() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orders.total) FROM orders WHERE DAY(date)=DAY(CURDATE())");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0;
    }

    public static double getMonthlySalesTotal() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orders.total) FROM orders WHERE MONTH(date)=MONTH(CURDATE())");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0;
    }

    public static double getAnnualSalesTotal() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orders.total) FROM orders WHERE YEAR(date)=YEAR(CURDATE())");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0;
    }
}
