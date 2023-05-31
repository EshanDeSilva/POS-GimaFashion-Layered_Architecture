package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.model.SalesReturnDto;

import java.sql.SQLException;

public interface SalesReturnDao extends CrudDao<SalesReturnDto,String> {
    double getDailyReturnTotal() throws SQLException, ClassNotFoundException;
}
