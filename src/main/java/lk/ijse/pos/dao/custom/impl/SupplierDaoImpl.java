package lk.ijse.pos.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.pos.dao.custom.SupplierDao;
import lk.ijse.pos.dao.custom.impl.util.CrudUtil;
import lk.ijse.pos.entity.Supplier;
import lk.ijse.pos.model.SupplierDto;
import lk.ijse.pos.model.tm.SuppliesTm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoImpl implements SupplierDao {

    @Override
    public boolean save(Supplier dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO supplier VALUES (?,?,?,?,?)",
                dto.getSupplierId(),
                dto.getTitle(),
                dto.getSupplierName(),
                dto.getCompany(),
                dto.getContact());
    }

    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM supplier ORDER BY supplierId DESC LIMIT 1");
        if (resultSet.next()){
            String lastId = resultSet.getString(1).split("[-]")[1];
            return String.format("SUP-%04d",Integer.valueOf(lastId)+1);
        }
        return "SUP-0001";
    }

    @Override
    public List<Supplier> findAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM supplier");
        List<Supplier> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(new Supplier(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return list;
    }

    @Override
    public boolean delete(String supplierId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM supplier WHERE supplierId=?",supplierId);
    }

    @Override
    public boolean update(Supplier dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE supplier SET title=?,supplierName=?,company=?,contact=? WHERE supplierId=?",
                dto.getTitle(),dto.getSupplierName(),dto.getCompany(),dto.getContact(),dto.getSupplierId());
    }

    @Override
    public boolean exists(Supplier supplierDto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public Supplier find(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM supplier WHERE supplierId=?", id);
        while (resultSet.next()){
            return new Supplier(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
                    );
        }
        return null;
    }

    @Override
    public Supplier findByName(String name) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM supplier WHERE supplierName=?", name);
        while (resultSet.next()){
            return new Supplier(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
        }
        return null;
    }

}
