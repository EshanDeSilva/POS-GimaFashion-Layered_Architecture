package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.entity.User;

import java.sql.SQLException;

public interface UserDao extends CrudDao<User,String> {
    String getUserType(String userName) throws SQLException, ClassNotFoundException;
    String getEmail(String userName) throws SQLException, ClassNotFoundException;
    boolean updatePassword(String userName, String pswrd) throws SQLException, ClassNotFoundException;
}
