package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.entity.Supplier;
import lk.ijse.pos.model.SupplierDto;

import java.sql.SQLException;

public interface SupplierDao extends CrudDao<Supplier,String> {
    Supplier findByName(String name) throws SQLException, ClassNotFoundException;
//    ObservableList<SuppliesTm> getItems(String id) throws SQLException, ClassNotFoundException;
}
