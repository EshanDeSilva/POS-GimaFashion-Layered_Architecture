package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.dao.custom.OrderDao;
import lk.ijse.pos.dao.custom.impl.util.CrudUtil;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.model.OrderDetailsDto;
import lk.ijse.pos.model.OrderDto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    OrderDetailsDaoImpl orderDetailsDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.ORDER_DETAILS);
    PaymentDaoImpl paymentDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.PAYMENT);

    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT orderId FROM orders ORDER BY orderId DESC LIMIT 1");
        if (resultSet.next()){
            int num = Integer.valueOf(resultSet.getString(1).split("-")[1]);
            return String.format("ORD-%08d",++num);
        }
        return "ORD-00000001";
    }

    @Override
    public OrderDto find(String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(OrderDto dto) throws SQLException {
        Connection connection=null;
        try{
            connection= DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            Boolean orderSaved = CrudUtil.execute("INSERT INTO orders VALUES (?,?,?,?,?,?,?,?)",
                    dto.getOrderId(), dto.getDate(), dto.getTotalDiscount(), dto.getTotal(), dto.getEmployerId(),
                    dto.getCustomerName(), dto.getCustomerEmail(), dto.getCustomerContact());
            if (orderSaved) {
                boolean paymentSaved = paymentDao.save(dto.getPaymentDto());
                if (paymentSaved) {
                    boolean detailSaved = true;
                    for (OrderDetailsDto dto1 : dto.getDetailDto()) {
                        if (!orderDetailsDao.save(dto1)) {
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
    public boolean update(OrderDto dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exists(OrderDto orderDto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List<OrderDto> findAll() throws SQLException, ClassNotFoundException {
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
                    orderDetailsDao.findAll(resultSet.getString(1)),
                    paymentDao.getPayments(resultSet.getString(1))
            ));
        }
        return list;
    }

    @Override
    public double getDailySalesTotal() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orders.total) FROM orders WHERE DAY(date)=DAY(CURDATE())");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0;
    }

    @Override
    public double getMonthlySalesTotal() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orders.total) FROM orders WHERE MONTH(date)=MONTH(CURDATE())");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0;
    }

    @Override
    public double getAnnualSalesTotal() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orders.total) FROM orders WHERE YEAR(date)=YEAR(CURDATE())");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0;
    }
}
