package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.OrderDao;
import lk.ijse.pos.dao.custom.impl.util.CrudUtil;
import lk.ijse.pos.entity.Orders;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
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
    public Orders find(String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Orders order) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO orders VALUES (?,?,?,?,?,?,?,?)",
                order.getOrderId(), order.getDate(), order.getTotalDiscount(), order.getTotal(), order.getEmployerId(),
                order.getCustomerName(), order.getCustomerEmail(), order.getCustomerContact());
    }

    @Override
    public boolean update(Orders order) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exists(Orders order) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List<Orders> findAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM orders");
        List<Orders> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(new Orders(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8)
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
