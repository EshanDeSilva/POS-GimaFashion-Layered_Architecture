package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.CategoryDao;
import lk.ijse.pos.dao.custom.impl.util.CrudUtil;
import lk.ijse.pos.entity.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    @Override
    public List<Category> findAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM category");
        List<Category> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(new Category(
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
    public boolean save(Category category) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO category VALUES (?,?,?)",
                category.getCategoryId(),category.getSize(),category.getGender());
    }

    @Override
    public boolean update(Category category) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exists(Category category) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List<Category> findAllTypes() throws SQLException, ClassNotFoundException {
        CrudUtil.execute("SET sql_mode = (SELECT REPLACE(@@sql_mode, 'ONLY_FULL_GROUP_BY', ''))");
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM category GROUP BY 3");
        List<Category> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(new Category(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            ));
        }
        return list;
    }

    @Override
    public List<Category> findAllSize(String type) throws SQLException, ClassNotFoundException {
        CrudUtil.execute("SET sql_mode = (SELECT REPLACE(@@sql_mode, 'ONLY_FULL_GROUP_BY', ''))");
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM category WHERE gender=? GROUP BY 2",type);
        List<Category> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(new Category(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            ));
        }
        return list;
    }

    @Override
    public Category find(String type, String size) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM category WHERE gender=? AND size=?",type,size);
        if (resultSet.next()){
            return new Category(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
        }
        return new Category();
    }

    @Override
    public Category find(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM category WHERE categoryId=?",id);
        if (resultSet.next()){
            return new Category(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
        }
        return new Category();
    }
}
