package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBo;
import lk.ijse.pos.model.CategoryDto;

import java.sql.SQLException;
import java.util.List;

public interface CategoryBo extends SuperBo {
    String getId() throws SQLException, ClassNotFoundException;
    List<CategoryDto> findAllCategories() throws SQLException, ClassNotFoundException;
    boolean saveCategory(CategoryDto dto) throws SQLException, ClassNotFoundException;
    List<CategoryDto> findAllTypesOfCategories() throws SQLException, ClassNotFoundException;
    List<CategoryDto> findAllSizesOfCategories(String type) throws SQLException, ClassNotFoundException;
    CategoryDto findCategory(String type, String size) throws SQLException, ClassNotFoundException;
    CategoryDto findCategory(String categoryId) throws SQLException, ClassNotFoundException;
}
