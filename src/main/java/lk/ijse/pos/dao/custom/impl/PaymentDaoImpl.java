package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.PaymentDao;
import lk.ijse.pos.dao.custom.impl.util.CrudUtil;
import lk.ijse.pos.model.PaymentDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDaoImpl implements PaymentDao {
    @Override
    public List<PaymentDto> findAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(PaymentDto dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(PaymentDto dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exists(PaymentDto paymentDto) throws SQLException, ClassNotFoundException {
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
    public PaymentDto find(String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(List<PaymentDto> dtos) throws SQLException, ClassNotFoundException {
        boolean isSaved = true;
        for (PaymentDto dto:dtos) {
            if (CrudUtil.execute("INSERT INTO payment VALUES (?,?,?,?,?,?)",
                    dto.getPaymentId(),dto.getCash(),dto.isPayByCash(),dto.getBalance(),dto.getDate(),
                    dto.getOrderId())){

            }else{
                isSaved = false;
            }
        }
        return isSaved;
    }

    @Override
    public List<PaymentDto> getPayments(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM payment WHERE orderId=?",id);
        List<PaymentDto> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(new PaymentDto(
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
