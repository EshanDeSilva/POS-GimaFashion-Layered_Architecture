package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.bo.BoFactory;
import lk.ijse.pos.bo.custom.CategoryBo;
import lk.ijse.pos.bo.custom.ItemBo;
import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.dao.custom.ItemDao;
import lk.ijse.pos.entity.Item;
import lk.ijse.pos.model.ItemDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemBoImpl implements ItemBo {
    ItemDao itemDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.ITEM);
    CategoryBo categoryBo = BoFactory.getInstance().getBoType(BoFactory.BoType.CATEGORY_BO);

    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        return itemDao.getId();
    }

    @Override
    public boolean saveItem(ItemDto dto) throws SQLException, ClassNotFoundException {
        return itemDao.save(new Item(
                dto.getCode(),
                dto.getSupplierId(),
                dto.getDescription(),
                Integer.parseInt(dto.getQty()),
                Double.parseDouble(dto.getSellingPrice()),
                Double.parseDouble(dto.getBuyingPrice()),
                dto.getCategoryDto().getId()
        ));
    }

    @Override
    public boolean updateItem(ItemDto dto) throws SQLException, ClassNotFoundException {
        return itemDao.update(new Item(
                dto.getCode(),
                dto.getSupplierId(),
                dto.getDescription(),
                Integer.parseInt(dto.getQty()),
                Double.parseDouble(dto.getSellingPrice()),
                Double.parseDouble(dto.getBuyingPrice()),
                dto.getCategoryDto().getId()
        ));
    }

    @Override
    public boolean addStock(String code, int qty) throws SQLException, ClassNotFoundException {
        return itemDao.addStock(code,qty);
    }

    @Override
    public List<ItemDto> findAllItems() throws SQLException, ClassNotFoundException {
        List<ItemDto> dtoList = new ArrayList<>();
        for (Item item:itemDao.findAll()) {
            dtoList.add(new ItemDto(
                    item.getItemCode(),
                    item.getSupplierId(),
                    item.getDescription(),
                    String.valueOf(item.getQtyOnHand()),
                    String.valueOf(item.getSellingPrice()),
                    String.valueOf(item.getBuyingPrice()),
                    categoryBo.findCategory(item.getCategoryId())
            ));
        }
        return dtoList;
    }

    @Override
    public ItemDto findItem(String code) throws SQLException, ClassNotFoundException {
        Item item = itemDao.find(code);
        return new ItemDto(
                item.getItemCode(),
                item.getSupplierId(),
                item.getDescription(),
                String.valueOf(item.getQtyOnHand()),
                String.valueOf(item.getSellingPrice()),
                String.valueOf(item.getBuyingPrice()),
                categoryBo.findCategory(item.getCategoryId())
        );
    }

    @Override
    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException {
        return itemDao.delete(code);
    }

    @Override
    public boolean updateQtyOfItem(ItemDto dto) throws SQLException, ClassNotFoundException {
        return itemDao.update(new Item(
                dto.getCode(),
                dto.getSupplierId(),
                dto.getDescription(),
                Integer.parseInt(dto.getQty()),
                Double.parseDouble(dto.getSellingPrice()),
                Double.parseDouble(dto.getBuyingPrice()),
                dto.getCategoryDto().getId()
        ));
    }

    @Override
    public List<ItemDto> getItemsBySupplierId(String id) throws SQLException, ClassNotFoundException {
        List<ItemDto> dtoList = new ArrayList<>();
        for (Item item:itemDao.getItemsBySupplierId(id)) {
            dtoList.add(new ItemDto(
                    item.getItemCode(),
                    item.getSupplierId(),
                    item.getDescription(),
                    String.valueOf(item.getQtyOnHand()),
                    String.valueOf(item.getSellingPrice()),
                    String.valueOf(item.getBuyingPrice()),
                    categoryBo.findCategory(item.getCategoryId())
            ));
        }
        return dtoList;
    }
}
