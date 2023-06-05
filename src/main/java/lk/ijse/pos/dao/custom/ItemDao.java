package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.entity.Item;

import java.sql.SQLException;
import java.util.List;

public interface ItemDao extends CrudDao<Item,String> {
    boolean addStock(String code, int qty) throws SQLException, ClassNotFoundException;
    boolean updateQty(Item dto) throws SQLException, ClassNotFoundException;
    List<Item> getItemsBySupplierId(String id) throws SQLException, ClassNotFoundException;
}
