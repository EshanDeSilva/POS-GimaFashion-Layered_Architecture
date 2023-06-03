package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.entity.SupplierInvoice;
import lk.ijse.pos.model.SupplierInvoiceDto;

import java.sql.SQLException;

public interface SupplierInvoiceDao extends CrudDao<SupplierInvoice,String> {
    boolean addStock(SupplierInvoice dto) throws SQLException, ClassNotFoundException;
}
