package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBo;
import lk.ijse.pos.model.SalesReturnDto;

import java.sql.SQLException;

public interface SalesReturnBo extends SuperBo {
    String getId() throws SQLException, ClassNotFoundException;
    boolean saveSalesReturn(SalesReturnDto dto) throws SQLException;
    double getDailySalesReturnTotal() throws SQLException, ClassNotFoundException;
}
