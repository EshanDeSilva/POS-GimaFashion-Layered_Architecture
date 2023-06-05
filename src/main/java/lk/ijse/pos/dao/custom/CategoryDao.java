package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.entity.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDao extends CrudDao<Category,String> {
    List<Category> findAllTypes() throws SQLException, ClassNotFoundException;
    List<Category> findAllSize(String type) throws SQLException, ClassNotFoundException;
    Category find(String type, String size) throws SQLException, ClassNotFoundException;
}
