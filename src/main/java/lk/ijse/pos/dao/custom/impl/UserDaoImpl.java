package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.UserDao;
import lk.ijse.pos.entity.User;
import lk.ijse.pos.model.UserDto;
import lk.ijse.pos.dao.custom.impl.util.CrudUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public List<User> findAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(User user) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO user VALUES (?,?,?,?)",user.getUsername(),BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()),user.getUserEmail(),user.getUserType());
    }

    @Override
    public boolean update(User dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exists(User user) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM user");
        while (resultSet.next()){
            if (resultSet.getString(1).equals(user.getUsername()) && BCrypt.checkpw(user.getPassword(),resultSet.getString(2))){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public User find(String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public String getUserType(String userName) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT userType FROM user WHERE username = ?",userName);
        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return "Default";
    }

    @Override
    public String getEmail(String userName) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT userEmail FROM user WHERE username = ?",userName);
        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return "hasindueshandesilva@gmail.com";
    }

    @Override
    public boolean updatePassword(String userName, String pswrd) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE user SET password = ? WHERE username = ?",BCrypt.hashpw(pswrd,BCrypt.gensalt()),userName);
    }
}
