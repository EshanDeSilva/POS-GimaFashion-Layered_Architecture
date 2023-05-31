package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.model.SupplierInvoiceDto;

import java.sql.SQLException;

public interface SupplierInvoiceDao extends CrudDao<SupplierInvoiceDto,String> {
    boolean addStock(SupplierInvoiceDto dto) throws SQLException;
}
