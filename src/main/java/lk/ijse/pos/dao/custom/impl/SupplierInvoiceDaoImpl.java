package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.SupplierInvoiceDao;
import lk.ijse.pos.dao.custom.impl.util.CrudUtil;
import lk.ijse.pos.entity.SupplierInvoice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SupplierInvoiceDaoImpl implements SupplierInvoiceDao {
    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM supplierinvoice ORDER BY invoiceId DESC LIMIT 1");
        if (resultSet.next()){
            String lastId = resultSet.getString(1).split("[-]")[1];
            return String.format("INV-%05d",Integer.valueOf(lastId)+1);
        }
        return "INV-00001";
    }

    @Override
    public SupplierInvoice find(String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<SupplierInvoice> findAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(SupplierInvoice supplierInvoice) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO supplierinvoice VALUES (?,?,?,?,?)",
                supplierInvoice.getInvoiceId(), supplierInvoice.getSupplierId(), supplierInvoice.getItemCode(), supplierInvoice.getDate(), supplierInvoice.getQty());
    }

    @Override
    public boolean update(SupplierInvoice supplierInvoice) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exists(SupplierInvoice supplierInvoice) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean addStock(SupplierInvoice supplierInvoice) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO supplierinvoice VALUES (?,?,?,?,?)",
                supplierInvoice.getInvoiceId(), supplierInvoice.getSupplierId(), supplierInvoice.getItemCode(), supplierInvoice.getDate(), supplierInvoice.getQty());
    }

}
