package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.model.CategoryDto;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDao extends CrudDao<CategoryDto,String> {
    List<CategoryDto> findAllTypes() throws SQLException, ClassNotFoundException;
    List<CategoryDto> findAllSize(String type) throws SQLException, ClassNotFoundException;
    CategoryDto find(String type, String size) throws SQLException, ClassNotFoundException;

}
