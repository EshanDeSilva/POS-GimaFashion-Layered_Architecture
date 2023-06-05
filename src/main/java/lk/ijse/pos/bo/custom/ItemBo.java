package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBo;
import lk.ijse.pos.model.ItemDto;

import java.sql.SQLException;
import java.util.List;

public interface ItemBo extends SuperBo {
    String getId() throws SQLException, ClassNotFoundException;
    boolean saveItem(ItemDto dto) throws SQLException, ClassNotFoundException;
    boolean updateItem(ItemDto dto) throws SQLException, ClassNotFoundException;
    boolean addStock(String code, int qty) throws SQLException, ClassNotFoundException;
    List<ItemDto> findAllItems() throws SQLException, ClassNotFoundException;
    ItemDto findItem(String code) throws SQLException, ClassNotFoundException;
    boolean deleteItem(String code) throws SQLException, ClassNotFoundException;
    boolean updateQtyOfItem(ItemDto dto) throws SQLException, ClassNotFoundException;
    List<ItemDto> getItemsBySupplierId(String id) throws SQLException, ClassNotFoundException;

}
