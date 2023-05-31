package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.dao.custom.ItemDao;
import lk.ijse.pos.model.CategoryDto;
import lk.ijse.pos.model.ItemDto;
import lk.ijse.pos.dao.custom.impl.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDaoImpl implements ItemDao {
    CategoryDaoImpl categoryDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.CATEGORY);

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
    public boolean save(ItemDto dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO item VALUES (?,?,?,?,?,?,?)",
                dto.getCode(),dto.getSupplierId(),dto.getDescription(),dto.getQty(),dto.getSellingPrice(),
                dto.getBuyingPrice(),dto.getCategoryDto().getId());
    }

    @Override
    public boolean update(ItemDto dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE item SET supplierId=?,description=?,qtyOnHand=?,sellingPrice=?,buyingPrice=?," +
                        "categoryId=? WHERE itemCode=?", dto.getSupplierId(), dto.getDescription(),dto.getQty(),dto.getSellingPrice(),
                dto.getBuyingPrice(), dto.getCategoryDto().getId(),dto.getCode());
    }

    @Override
    public boolean exists(ItemDto itemDto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean addStock(String code, int qty) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE item SET qtyOnHand=? WHERE itemCode=?", String.valueOf(qty+Integer.parseInt(find(code).getQty())), code);
    }

    @Override
    public List<ItemDto> findAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item ORDER BY itemCode");
        List<ItemDto> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(new ItemDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    String.valueOf(resultSet.getInt(4)),
                    String.valueOf(resultSet.getDouble(5)),
                    String.valueOf(resultSet.getDouble(6)),
                    categoryDao.find(resultSet.getString(7))
            ));
        }
        return list;
    }

    @Override
    public ItemDto find(String code) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item WHERE itemCode=?",code);
        if (resultSet.next()){
            return new ItemDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    String.valueOf(resultSet.getInt(4)),
                    String.valueOf(resultSet.getDouble(5)),
                    String.valueOf(resultSet.getDouble(6)),
                    categoryDao.find(resultSet.getString(7))
                    );
        }
        return new ItemDto();
    }

    @Override
    public boolean delete(String code) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM item WHERE itemCode=?",code);
    }

    @Override
    public boolean updateQty(ItemDto dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE item SET qtyOnHand=? WHERE itemCode=?",dto.getQty(),dto.getCode());
    }

    @Override
    public List<CategoryDto> findTypesByItem(String code) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT category.categoryId,category.size,category.gender,item.itemCode FROM category INNER JOIN item ON item.categoryId=category.categoryId WHERE item.itemCode=?",code);
        List<CategoryDto> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(new CategoryDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            ));
        }
        return list;
    }
}
