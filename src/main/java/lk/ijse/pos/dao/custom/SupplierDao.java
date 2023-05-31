package lk.ijse.pos.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.model.SupplierDto;
import lk.ijse.pos.model.tm.SuppliesTm;

import java.sql.SQLException;

public interface SupplierDao extends CrudDao<SupplierDto,String> {
    SupplierDto findByName(String name) throws SQLException, ClassNotFoundException;
    ObservableList<SuppliesTm> getItems(String id) throws SQLException, ClassNotFoundException;
}
