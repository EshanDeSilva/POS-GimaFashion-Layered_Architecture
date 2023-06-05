package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.SalesReturnDao;
import lk.ijse.pos.dao.custom.impl.util.CrudUtil;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.entity.SalesReturn;
import lk.ijse.pos.model.SalesReturnDto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SalesReturnDaoImpl implements SalesReturnDao {
    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT returnId FROM salesReturn ORDER BY returnId DESC LIMIT 1");
        if (resultSet.next()){
            String lastId = resultSet.getString(1).split("[-]")[1];
            return String.format("R-%08d",Integer.valueOf(lastId)+1);
        }
        return "R-00000001";
    }

    @Override
    public SalesReturn find(String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<SalesReturn> findAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(SalesReturn dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO salesReturn VALUES (?,?,?,?)",dto.getReturnId(),
                dto.getOrderId(),dto.getDate(),dto.getTotal());
    }

    @Override
    public boolean update(SalesReturn dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exists(SalesReturn salesReturnDto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public double getDailyReturnTotal() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT total FROM salesReturn WHERE date = CURDATE()");
        double total = 0;
        while (resultSet.next()){
            total += resultSet.getDouble(1);
        }
        return total;
    }
}
