package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.entity.Supplier;

import java.sql.SQLException;

public interface SupplierDao extends CrudDao<Supplier,String> {
    Supplier findByName(String name) throws SQLException, ClassNotFoundException;
}
