package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.SalesReturnDetailsDao;
import lk.ijse.pos.dao.custom.impl.util.CrudUtil;
import lk.ijse.pos.entity.SalesReturnDetails;

import java.sql.SQLException;
import java.util.List;

public class SalesReturnDetailsDaoImpl implements SalesReturnDetailsDao {
    @Override
    public List<SalesReturnDetails> findAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(SalesReturnDetails dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO salesReturnDetails VALUES (?,?,?,?,?,?)",
                dto.getReturnId(),dto.getItemCode(),
                dto.getQty(),dto.getDiscRate(),
                dto.getUnitPrice(),dto.getAmount());
    }

    @Override
    public boolean update(SalesReturnDetails dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exists(SalesReturnDetails salesReturnDetails) throws SQLException, ClassNotFoundException {
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
    public SalesReturnDetails find(String s) throws SQLException, ClassNotFoundException {
        return null;
    }
}
