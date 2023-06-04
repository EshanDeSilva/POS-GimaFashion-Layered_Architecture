package lk.ijse.pos.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.entity.Item;
import lk.ijse.pos.model.CategoryDto;
import lk.ijse.pos.model.ItemDto;
import lk.ijse.pos.model.SuppliesDto;
import lk.ijse.pos.model.tm.SuppliesTm;

import java.sql.SQLException;
import java.util.List;

public interface ItemDao extends CrudDao<ItemDto,String> {
    boolean addStock(String code, int qty) throws SQLException, ClassNotFoundException;
    boolean updateQty(ItemDto dto) throws SQLException, ClassNotFoundException;
    List<CategoryDto> findTypesByItem(String code) throws SQLException, ClassNotFoundException;
    List<Item> getItemsBySupplierId(String id) throws SQLException, ClassNotFoundException;
}
