package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.PaymentDao;
import lk.ijse.pos.dao.custom.impl.util.CrudUtil;
import lk.ijse.pos.entity.Payment;
import lk.ijse.pos.model.PaymentDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDaoImpl implements PaymentDao {
    @Override
    public List<Payment> findAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Payment payment) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Payment payment) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exists(Payment payment) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT paymentId FROM payment ORDER BY paymentId DESC LIMIT 1");
        if (resultSet.next()){
            int num = Integer.valueOf(resultSet.getString(1).split("-")[1]);
            return String.format("PMT-%08d",++num);
        }
        return "PMT-00000001";
    }

    @Override
    public Payment find(String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(List<Payment> list) throws SQLException, ClassNotFoundException {
        boolean isSaved = true;
        for (Payment payment:list) {
            if (CrudUtil.execute("INSERT INTO payment VALUES (?,?,?,?,?,?)",
                    payment.getPaymentId(),payment.getCash(),payment.isPayByCash(),payment.getBalance(),payment.getDate(),
                    payment.getOrderId())){

            }else{
                isSaved = false;
            }
        }
        return isSaved;
    }

    @Override
    public List<Payment> getPayments(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM payment WHERE orderId=?",id);
        List<Payment> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(new Payment(
                    resultSet.getString(1),
                    resultSet.getDouble(2),
                    resultSet.getBoolean(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            ));
        }
        return list;
    }
}
