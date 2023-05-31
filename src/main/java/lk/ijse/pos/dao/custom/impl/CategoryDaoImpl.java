package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.CategoryDao;
import lk.ijse.pos.dao.custom.impl.util.CrudUtil;
import lk.ijse.pos.model.CategoryDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    @Override
    public List<CategoryDto> findAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM category");
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

    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM category ORDER BY categoryId DESC LIMIT 1");
        if (resultSet.next()){
            String lastId = resultSet.getString(1).split("[-]")[1];
            return String.format("CAT-%03d",Integer.valueOf(lastId)+1);
        }
        return "CAT-001";
    }

    @Override
    public boolean save(CategoryDto dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO category VALUES (?,?,?)",
                dto.getId(),dto.getSize(),dto.getType());
    }

    @Override
    public boolean update(CategoryDto dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exists(CategoryDto categoryDto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List<CategoryDto> findAllTypes() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM category GROUP BY 3");
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

    @Override
    public List<CategoryDto> findAllSize(String type) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM category WHERE gender=? GROUP BY 2",type);
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

    @Override
    public CategoryDto find(String type, String size) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM category WHERE gender=? AND size=?",type,size);
        if (resultSet.next()){
            return new CategoryDto(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
        }
        return new CategoryDto();
    }

    @Override
    public CategoryDto find(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM category WHERE categoryId=?",id);
        if (resultSet.next()){
            return new CategoryDto(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
        }
        return new CategoryDto();
    }
}
