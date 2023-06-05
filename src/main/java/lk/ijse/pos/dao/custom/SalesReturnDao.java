package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.entity.SalesReturn;
import lk.ijse.pos.model.SalesReturnDto;

import java.sql.SQLException;

public interface SalesReturnDao extends CrudDao<SalesReturn,String> {
    double getDailyReturnTotal() throws SQLException, ClassNotFoundException;
}
