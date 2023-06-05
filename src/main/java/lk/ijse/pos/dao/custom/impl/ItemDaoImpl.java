package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.ItemDao;
import lk.ijse.pos.entity.Item;
import lk.ijse.pos.dao.custom.impl.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDaoImpl implements ItemDao {
    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item ORDER BY itemCode DESC LIMIT 1");
        if (resultSet.next()){
            String lastId = resultSet.getString(1).split("[-]")[1];
            return String.format("P-%05d",Integer.valueOf(lastId)+1);
        }
        return "P-00001";
    }

    @Override
    public boolean save(Item item) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO item VALUES (?,?,?,?,?,?,?)",
                item.getItemCode(),item.getSupplierId(),item.getDescription(),item.getQtyOnHand(),item.getSellingPrice(),
                item.getBuyingPrice(),item.getCategoryId());
    }

    @Override
    public boolean update(Item item) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE item SET supplierId=?,description=?,qtyOnHand=?,sellingPrice=?,buyingPrice=?," +
                        "categoryId=? WHERE itemCode=?", item.getSupplierId(), item.getDescription(),item.getQtyOnHand(),item.getSellingPrice(),
                item.getBuyingPrice(), item.getCategoryId(),item.getCategoryId());
    }

    @Override
    public boolean exists(Item item) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean addStock(String code, int qty) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE item SET qtyOnHand=? WHERE itemCode=?", String.valueOf(qty+find(code).getQtyOnHand()), code);
    }

    @Override
    public List<Item> findAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item ORDER BY itemCode");
        List<Item> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4),
                    resultSet.getDouble(5),
                    resultSet.getDouble(6),
                    resultSet.getString(7)
            ));
        }
        return list;
    }

    @Override
    public Item find(String code) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item WHERE itemCode=?",code);
        if (resultSet.next()){
            return new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4),
                    resultSet.getDouble(5),
                    resultSet.getDouble(6),
                    resultSet.getString(7)
                    );
        }
        return null;
    }

    @Override
    public boolean delete(String code) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM item WHERE itemCode=?",code);
    }

    @Override
    public boolean updateQty(Item item) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE item SET qtyOnHand=? WHERE itemCode=?",item.getQtyOnHand(),item.getItemCode());
    }

//    @Override
//    public List<CategoryDto> findTypesOfItem(String code) throws SQLException, ClassNotFoundException {
//        ResultSet resultSet = CrudUtil.execute("SELECT category.categoryId,category.size,category.gender,item.itemCode FROM category INNER JOIN item ON item.categoryId=category.categoryId WHERE item.itemCode=?",code);
//        List<CategoryDto> list = new ArrayList<>();
//        while (resultSet.next()){
//            list.add(new CategoryDto(
//                    resultSet.getString(1),
//                    resultSet.getString(2),
//                    resultSet.getString(3)
//            ));
//        }
//        return list;
//    }

    @Override
    public List<Item> getItemsBySupplierId(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item WHERE supplierId=?", id);
        List<Item> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4),
                    resultSet.getDouble(5),
                    resultSet.getDouble(6),
                    resultSet.getString(7)
            ));
        }
        return list;
    }
}
