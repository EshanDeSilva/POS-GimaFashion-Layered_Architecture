package lk.ijse.pos.dao;

import java.sql.SQLException;
import java.util.List;

public interface CrudDao<T,ID> extends SuperDao{
    List<T> findAll() throws SQLException, ClassNotFoundException;

    boolean save(T dto) throws SQLException, ClassNotFoundException;

    boolean update(T dto) throws SQLException, ClassNotFoundException;

    boolean exists(T t) throws SQLException, ClassNotFoundException;

    boolean delete(ID id) throws SQLException, ClassNotFoundException;

    ID getId() throws SQLException, ClassNotFoundException;

    T find(ID id) throws SQLException, ClassNotFoundException;
}
