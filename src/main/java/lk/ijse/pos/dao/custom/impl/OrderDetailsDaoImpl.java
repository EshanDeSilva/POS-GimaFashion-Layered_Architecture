package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.OrderDetailsDao;
import lk.ijse.pos.model.OrderDetailsDto;
import lk.ijse.pos.dao.custom.impl.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsDaoImpl implements OrderDetailsDao {
    @Override
    public List<OrderDetailsDto> findAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(OrderDetailsDto dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO orderdetails VALUES (?,?,?,?,?,?)",
                dto.getOrderId(),dto.getItemCode(),dto.getOrderQty(),dto.getUnitPrice(),dto.getTotalProfit(),dto.getDiscRate());
    }

    @Override
    public boolean update(OrderDetailsDto dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exists(OrderDetailsDto orderDetailsDto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public OrderDetailsDto find(String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public double getDailyGentsSaleCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM category INNER JOIN item ON item.categoryId=category.categoryId INNER JOIN orderDetails ON item.itemCode=orderDetails.itemCode INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE orders.date=CURDATE() && category.gender=?", "Gents");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0.0;
    }

    @Override
    public double getDailyLadiesSaleCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM category INNER JOIN item ON item.categoryId=category.categoryId INNER JOIN orderDetails ON item.itemCode=orderDetails.itemCode INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE orders.date=CURDATE() && category.gender=?","Ladies");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0.0;
    }

    @Override
    public double getDailyKidsSaleCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM category INNER JOIN item ON item.categoryId=category.categoryId " +
                "INNER JOIN orderDetails ON item.itemCode=orderDetails.itemCode INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE orders.date=CURDATE() && category.gender=?","Kids");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0.0;
    }

    @Override
    public double getDailyOtherSaleCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM category INNER JOIN item ON item.categoryId=category.categoryId " +
                "INNER JOIN orderDetails ON item.itemCode=orderDetails.itemCode INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE orders.date=CURDATE() && " +
                "category.gender!=? && category.gender!=? && category.gender!=?","Ladies","Gents","Kids");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0.0;
    }

    @Override
    public List<OrderDetailsDto> findAll(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM orderDetails WHERE orderId=?",id);
        List<OrderDetailsDto> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(new OrderDetailsDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4),
                    resultSet.getDouble(5),
                    resultSet.getDouble(6)
            ));
        }
        return list;
    }

    @Override
    public double getMonthlySalesCount(String month) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM orderDetails INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE MONTHNAME(orders.date)=?",month);
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0;
    }

    @Override
    public int getMonthlySalesCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM orderDetails INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE MONTH(orders.date)=MONTH(CURDATE())");
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }


    @Override
    public int getDailySalesCount(int date) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM orderDetails INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE DAY(orders.date)=? AND MONTH(orders.date)=MONTH(CURDATE())",date);
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    @Override
    public double getAnnualIncome() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.totalProfit) FROM orderDetails INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE YEAR(orders.date)=YEAR(CURDATE())");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0;
    }

    @Override
    public int getAnnualSalesCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM orderDetails INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE YEAR(orders.date)=YEAR(CURDATE())");
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    @Override
    public double getMonthlyIncome() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.totalProfit) FROM orderDetails INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE MONTH(orders.date)=MONTH(CURDATE())");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0;
    }

    @Override
    public int getDailySalesCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM orderDetails INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE DAY(orders.date)=DAY(CURDATE()) AND MONTH(orders.date)=MONTH(CURDATE())");
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    @Override
    public double getDailyIncome() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.totalProfit) FROM orderDetails INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE DAY(orders.date)=DAY(CURDATE()) AND MONTH(orders.date)=MONTH(CURDATE())");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0;
    }
}
