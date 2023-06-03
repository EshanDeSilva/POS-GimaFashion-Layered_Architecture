package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBo;
import lk.ijse.pos.model.UserDto;

import java.sql.SQLException;

public interface UserBo extends SuperBo {
    boolean saveUser(UserDto user) throws SQLException, ClassNotFoundException;
    boolean existsUser(UserDto user) throws SQLException, ClassNotFoundException;
    String getUserType(String userName) throws SQLException, ClassNotFoundException;
    String getEmail(String userName) throws SQLException, ClassNotFoundException;
    boolean updateUserPassword(String userName, String pswrd) throws SQLException, ClassNotFoundException;
}
