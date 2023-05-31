package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.model.UserDto;

import java.sql.SQLException;

public interface UserDao extends CrudDao<UserDto,String> {
    String getUserType(String userName) throws SQLException, ClassNotFoundException;
    String getEmail(String userName) throws SQLException, ClassNotFoundException;
    Boolean updatePassword(String userName, String pswrd) throws SQLException, ClassNotFoundException;
}
