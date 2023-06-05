package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.bo.custom.CategoryBo;
import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.dao.custom.CategoryDao;
import lk.ijse.pos.entity.Category;
import lk.ijse.pos.model.CategoryDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryBoImpl implements CategoryBo {
    CategoryDao categoryDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.CATEGORY);

    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        return categoryDao.getId();
    }

    @Override
    public List<CategoryDto> findAllCategories() throws SQLException, ClassNotFoundException {
        List<CategoryDto> dtoList = new ArrayList<>();
        for (Category category:categoryDao.findAll()) {
            dtoList.add(new CategoryDto(
                    category.getCategoryId(),
                    category.getSize(),
                    category.getGender()
            ));
        }
        return dtoList;
    }

    @Override
    public boolean saveCategory(CategoryDto dto) throws SQLException, ClassNotFoundException {
        return categoryDao.save(new Category(dto.getId(),dto.getSize(),dto.getType()));
    }

    @Override
    public List<CategoryDto> findAllTypesOfCategories() throws SQLException, ClassNotFoundException {
        List<CategoryDto> dtoList = new ArrayList<>();
        for (Category category:categoryDao.findAllTypes()) {
            dtoList.add(new CategoryDto(
                    category.getCategoryId(),
                    category.getSize(),
                    category.getGender()
            ));
        }
        return dtoList;
    }

    @Override
    public List<CategoryDto> findAllSizesOfCategories(String type) throws SQLException, ClassNotFoundException {
        List<CategoryDto> dtoList = new ArrayList<>();
        for (Category category:categoryDao.findAllSize(type)) {
            dtoList.add(new CategoryDto(
                    category.getCategoryId(),
                    category.getSize(),
                    category.getGender()
            ));
        }
        return dtoList;
    }

    @Override
    public CategoryDto findCategory(String type, String size) throws SQLException, ClassNotFoundException {
        Category category = categoryDao.find(type, size);
        return new CategoryDto(
                category.getCategoryId(),
                category.getSize(),
                category.getGender()
        );
    }

    @Override
    public CategoryDto findCategory(String categoryId) throws SQLException, ClassNotFoundException {
        Category category = categoryDao.find(categoryId);
        return new CategoryDto(
                category.getCategoryId(),
                category.getSize(),
                category.getGender()
        );
    }
}
