package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.impl.util.CrudUtil;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.model.SupplierInvoiceDto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierInvoiceDaoImpl {
    public static String getId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM supplierinvoice ORDER BY invoiceId DESC LIMIT 1");
        if (resultSet.next()){
            String lastId = resultSet.getString(1).split("[-]")[1];
            return String.format("INV-%05d",Integer.valueOf(lastId)+1);
        }
        return "INV-00001";
    }

    public static Boolean save(SupplierInvoiceDto dto) throws SQLException {
        Connection connection=null;
        try{
            connection= DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean itemSaved = ItemDaoImpl.save(dto.getDto());
            if(itemSaved) {
                Boolean invoiceSaved = CrudUtil.execute("INSERT INTO supplierinvoice VALUES (?,?,?,?,?)",
                        dto.getInvoiceId(), dto.getSupplierId(), dto.getItemCode(), dto.getDate(), dto.getQty());
                if (invoiceSaved) {
                    connection.commit();
                    return true;
                }
            }
            return false;
        }catch (SQLException | ClassNotFoundException er) {
            connection.rollback();
            return false;
        } finally {
            System.out.println("finally");
            connection.setAutoCommit(true);
        }
    }

    public static Boolean addStock(SupplierInvoiceDto dto) throws SQLException {
        Connection connection=null;
        try{
            connection= DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean isUpdated = ItemDaoImpl.updateQty(dto.getDto());
            if(isUpdated) {
                Boolean invoiceSaved = CrudUtil.execute("INSERT INTO supplierinvoice VALUES (?,?,?,?,?)",
                        dto.getInvoiceId(), dto.getSupplierId(), dto.getItemCode(), dto.getDate(), dto.getQty());
                if (invoiceSaved) {
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
            System.out.println("finally");
            connection.setAutoCommit(true);
        }
    }

}
