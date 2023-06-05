package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.entity.SupplierInvoice;

import java.sql.SQLException;

public interface SupplierInvoiceDao extends CrudDao<SupplierInvoice,String> {
    boolean addStock(SupplierInvoice supplierInvoice) throws SQLException, ClassNotFoundException;
}
