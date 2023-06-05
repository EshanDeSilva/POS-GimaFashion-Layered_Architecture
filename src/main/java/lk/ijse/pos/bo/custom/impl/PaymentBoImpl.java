package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.bo.custom.PaymentBo;
import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.dao.custom.PaymentDao;
import lk.ijse.pos.entity.Payment;
import lk.ijse.pos.model.PaymentDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentBoImpl implements PaymentBo {
    PaymentDao paymentDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.PAYMENT);

    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        return paymentDao.getId();
    }

    @Override
    public boolean savePayment(List<PaymentDto> dtos) throws SQLException, ClassNotFoundException {
        List<Payment> list = new ArrayList<>();
        for (PaymentDto dto:dtos) {
            list.add(new Payment(dto.getPaymentId(),dto.getCash(),dto.isPayByCash(),dto.getBalance(),dto.getDate(),dto.getOrderId()));
        }
        return paymentDao.save(list);
    }

    @Override
    public List<PaymentDto> getPayments(String id) throws SQLException, ClassNotFoundException {
        List<PaymentDto> dtoList = new ArrayList<>();
        for (Payment entity:paymentDao.getPayments(id)) {
            dtoList.add(new PaymentDto(entity.getPaymentId(),
                    entity.getCash(),entity.isPayByCash(),entity.getBalance(),entity.getDate(),entity.getOrderId()));
        }

        return dtoList;
    }
}
