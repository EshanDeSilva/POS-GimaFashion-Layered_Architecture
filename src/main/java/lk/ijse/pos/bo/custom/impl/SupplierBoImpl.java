package lk.ijse.pos.bo.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.pos.bo.custom.SupplierBo;
import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.dao.custom.ItemDao;
import lk.ijse.pos.dao.custom.SupplierDao;
import lk.ijse.pos.entity.Item;
import lk.ijse.pos.entity.Supplier;
import lk.ijse.pos.model.SupplierDto;
import lk.ijse.pos.model.SuppliesDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierBoImpl implements SupplierBo {

    SupplierDao supplierDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.SUPPLIER);
    ItemDao itemDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.ITEM);

    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        return supplierDao.getId();
    }

    @Override
    public boolean saveSupplier(SupplierDto dto) throws SQLException, ClassNotFoundException {
        return supplierDao.save(new Supplier(dto.getSupplierId(),dto.getTitle(),dto.getName(),dto.getCompany(),dto.getContact()));
    }

    @Override
    public List<SupplierDto> findAllSuppliers() throws SQLException, ClassNotFoundException {
        List<SupplierDto> dtoList = new ArrayList<>();
        for (Supplier supplier:supplierDao.findAll()) {
            dtoList.add(new SupplierDto(
                    supplier.getSupplierId(),
                    supplier.getTitle(),
                    supplier.getSupplierName(),
                    supplier.getCompany(),
                    supplier.getContact()
            ));
        }
        return dtoList;
    }

    @Override
    public boolean deleteSupplier(String supplierId) throws SQLException, ClassNotFoundException {
        return supplierDao.delete(supplierId);
    }

    @Override
    public boolean updateSupplier(SupplierDto dto) throws SQLException, ClassNotFoundException {
        return supplierDao.update(new Supplier(dto.getSupplierId(),dto.getTitle(),dto.getName(),dto.getCompany(),dto.getContact()));
    }

    @Override
    public SupplierDto findSupplier(String id) throws SQLException, ClassNotFoundException {
        Supplier supplier = supplierDao.find(id);
        return new SupplierDto(supplier.getSupplierId(),supplier.getTitle(),supplier.getSupplierName(),supplier.getCompany(),supplier.getContact());
    }

    @Override
    public SupplierDto findSupplierByName(String name) throws SQLException, ClassNotFoundException {
        Supplier supplier = supplierDao.findByName(name);
        return new SupplierDto(supplier.getSupplierId(),supplier.getTitle(),supplier.getSupplierName(),supplier.getCompany(),supplier.getContact());
    }

    @Override
    public ObservableList<SuppliesDto> getItemsBySupplierId(String id) throws SQLException, ClassNotFoundException {
        ObservableList<SuppliesDto> list = FXCollections.observableArrayList();
        for (Item item:itemDao.getItemsBySupplierId(id)) {
            list.add(new SuppliesDto(
                    item.getItemCode(),item.getDescription(),item.getQtyOnHand()
            ));
        }
        return list;
    }
}
