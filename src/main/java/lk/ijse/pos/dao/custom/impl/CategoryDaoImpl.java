package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.impl.util.CrudUtil;
import lk.ijse.pos.model.CategoryDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl {
    public static List<CategoryDto> findAll() throws SQLException, ClassNotFoundException {
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

    public static String getId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM category ORDER BY categoryId DESC LIMIT 1");
        if (resultSet.next()){
            String lastId = resultSet.getString(1).split("[-]")[1];
            return String.format("CAT-%03d",Integer.valueOf(lastId)+1);
        }
        return "CAT-001";
    }

    public static Boolean save(CategoryDto dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO category VALUES (?,?,?)",
                dto.getId(),dto.getSize(),dto.getType());
    }

    public static List<CategoryDto> findAllTypes() throws SQLException, ClassNotFoundException {
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

    public static List<CategoryDto> findAllSize(String type) throws SQLException, ClassNotFoundException {
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

    public static CategoryDto find(String type, String size) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM category WHERE gender=? AND size=?",type,size);
        if (resultSet.next()){
            return new CategoryDto(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
        }
        return new CategoryDto();
    }

    public static CategoryDto find(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM category WHERE categoryId=?",id);
        if (resultSet.next()){
            return new CategoryDto(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
        }
        return new CategoryDto();
    }
}
