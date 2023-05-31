package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.model.OrderDetailsDto;
import lk.ijse.pos.dao.custom.impl.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsDaoImpl {
    public static Boolean save(OrderDetailsDto dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO orderdetails VALUES (?,?,?,?,?,?)",
                dto.getOrderId(),dto.getItemCode(),dto.getOrderQty(),dto.getUnitPrice(),dto.getTotalProfit(),dto.getDiscRate());
    }

    public static double getDailyGentsSaleCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM category INNER JOIN item ON item.categoryId=category.categoryId " +
                "INNER JOIN orderDetails ON item.itemCode=orderDetails.itemCode INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE orders.date=CURDATE() && category.gender=?", "Gents");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0.0;
    }

    public static double getDailyLadiesSaleCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM category INNER JOIN item ON item.categoryId=category.categoryId INNER JOIN orderDetails ON item.itemCode=orderDetails.itemCode INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE orders.date=CURDATE() && category.gender=?","Ladies");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0.0;
    }

    public static double getDailyKidsSaleCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM category INNER JOIN item ON item.categoryId=category.categoryId " +
                "INNER JOIN orderDetails ON item.itemCode=orderDetails.itemCode INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE orders.date=CURDATE() && category.gender=?","Kids");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0.0;
    }

    public static double getDailyOtherSaleCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM category INNER JOIN item ON item.categoryId=category.categoryId " +
                "INNER JOIN orderDetails ON item.itemCode=orderDetails.itemCode INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE orders.date=CURDATE() && " +
                "category.gender!=? && category.gender!=? && category.gender!=?","Ladies","Gents","Kids");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0.0;
    }

    public static List<OrderDetailsDto> getAll(String id) throws SQLException, ClassNotFoundException {
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

    public static double getMonthlySalesCount(String month) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM orderDetails INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE MONTHNAME(orders.date)=?",month);
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0;
    }
    public static int getMonthlySalesCount() throws SQLException, ClassNotFoundException {
            ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM orderDetails INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE MONTH(orders.date)=MONTH(CURDATE())");
            if (resultSet.next()){
                return resultSet.getInt(1);
            }
            return 0;
        }

    public static int getDailySalesCount(int date) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM orderDetails INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE DAY(orders.date)=?",date);
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static double getAnnualIncome() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.totalProfit) FROM orderDetails INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE YEAR(orders.date)=YEAR(CURDATE())");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0;
    }

    public static int getAnnualSalesCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM orderDetails INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE YEAR(orders.date)=YEAR(CURDATE())");
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static double getMonthlyIncome() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.totalProfit) FROM orderDetails INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE MONTH(orders.date)=MONTH(CURDATE())");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0;
    }

    public static int getDailySalesCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.orderQty) FROM orderDetails INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE DAY(orders.date)=DAY(CURDATE())");
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static double getDailyIncome() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(orderDetails.totalProfit) FROM orderDetails INNER JOIN orders ON orders.orderId=orderDetails.orderId WHERE DAY(orders.date)=DAY(CURDATE())");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0;
    }
}
