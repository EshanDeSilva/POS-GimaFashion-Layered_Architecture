package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.model.CategoryDto;
import lk.ijse.pos.model.ItemDto;

import java.sql.SQLException;
import java.util.List;

public interface ItemDao extends CrudDao<ItemDto,String> {
    boolean addStock(String code, int qty) throws SQLException, ClassNotFoundException;
    boolean updateQty(ItemDto dto) throws SQLException, ClassNotFoundException;
    List<CategoryDto> findTypesByItem(String code) throws SQLException, ClassNotFoundException;
}
