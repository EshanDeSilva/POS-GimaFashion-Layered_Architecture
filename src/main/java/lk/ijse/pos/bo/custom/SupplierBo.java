package lk.ijse.pos.bo.custom;

import javafx.collections.ObservableList;
import lk.ijse.pos.bo.SuperBo;
import lk.ijse.pos.model.SupplierDto;
import lk.ijse.pos.model.SuppliesDto;

import java.sql.SQLException;
import java.util.List;

public interface SupplierBo extends SuperBo {
    String getId() throws SQLException, ClassNotFoundException;
    boolean saveSupplier(SupplierDto dto) throws SQLException, ClassNotFoundException;
    List<SupplierDto> findAllSuppliers() throws SQLException, ClassNotFoundException;
    boolean deleteSupplier(String supplierId) throws SQLException, ClassNotFoundException;
    boolean updateSupplier(SupplierDto dto) throws SQLException, ClassNotFoundException;
    SupplierDto findSupplier(String id) throws SQLException, ClassNotFoundException;
    SupplierDto findSupplierByName(String name) throws SQLException, ClassNotFoundException;
    ObservableList<SuppliesDto> getItemsBySupplierId(String id) throws SQLException, ClassNotFoundException;
}
