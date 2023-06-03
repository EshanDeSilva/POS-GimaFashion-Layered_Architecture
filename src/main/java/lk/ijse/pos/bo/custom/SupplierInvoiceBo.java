package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBo;
import lk.ijse.pos.model.SupplierInvoiceDto;

import java.sql.SQLException;

public interface SupplierInvoiceBo extends SuperBo {
    String getId() throws SQLException, ClassNotFoundException;
    boolean saveSupplierInvoice(SupplierInvoiceDto dto) throws SQLException;
    boolean addInvoiceStock(SupplierInvoiceDto dto) throws SQLException;

}
