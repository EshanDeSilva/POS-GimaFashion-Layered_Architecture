package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.bo.custom.SupplierInvoiceBo;
import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.dao.custom.impl.ItemDaoImpl;
import lk.ijse.pos.dao.custom.impl.SupplierInvoiceDaoImpl;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.entity.SupplierInvoice;
import lk.ijse.pos.model.SupplierInvoiceDto;

import java.sql.Connection;
import java.sql.SQLException;

public class SupplierInvoiceBoImpl implements SupplierInvoiceBo {

    SupplierInvoiceDaoImpl supplierInvoiceDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.SUPPLIER_INVOICE);
    ItemDaoImpl itemDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.ITEM);

    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        return supplierInvoiceDao.getId();
    }

    @Override
    public boolean saveSupplierInvoice(SupplierInvoiceDto dto) throws SQLException {
        Connection connection=null;
        try{
            connection= DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean itemSaved = itemDao.save(dto.getDto());
            if(itemSaved) {

                if (supplierInvoiceDao.save(new SupplierInvoice(dto.getInvoiceId(),dto.getSupplierId(),dto.getItemCode(),dto.getDate(),dto.getQty()))) {
                    connection.commit();
                    return true;
                }
            }
            return false;
        }catch (SQLException | ClassNotFoundException er) {
            connection.rollback();
            return false;
        } finally {
//            System.out.println("finally");
            connection.setAutoCommit(true);
        }
    }

    @Override
    public boolean addInvoiceStock(SupplierInvoiceDto dto) throws SQLException {
        Connection connection=null;
        try{
            connection= DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean isUpdated = itemDao.updateQty(dto.getDto());
            if(isUpdated) {

                if (supplierInvoiceDao.addStock(new SupplierInvoice(dto.getInvoiceId(),dto.getSupplierId(),dto.getItemCode(),dto.getDate(),dto.getQty()))) {
                    connection.commit();
                    return true;
                }
            }
            return false;
        }catch (SQLException | ClassNotFoundException er) {
            System.out.println(er.getMessage());
            connection.rollback();
            return false;
        } finally {
//            System.out.println("finally");
            connection.setAutoCommit(true);
        }
    }
}
