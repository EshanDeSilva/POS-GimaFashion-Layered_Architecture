package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.bo.custom.UserBo;
import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.dao.custom.UserDao;
import lk.ijse.pos.dao.custom.impl.UserDaoImpl;
import lk.ijse.pos.entity.User;
import lk.ijse.pos.model.UserDto;

import java.sql.SQLException;

public class UserBoImpl implements UserBo {
    UserDao userDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.USER);

    @Override
    public boolean saveUser(UserDto user) throws SQLException, ClassNotFoundException {
        return userDao.save(new User(
                user.getUsername(),user.getPassword(),user.getUserEmail(),user.getUserType()
        ));
    }

    @Override
    public boolean existsUser(UserDto user) throws SQLException, ClassNotFoundException {
        return userDao.exists(new User(
                user.getUsername(),user.getPassword(),user.getUserEmail(),user.getUserType()
        ));
    }

    @Override
    public String getUserType(String userName) throws SQLException, ClassNotFoundException {
        return userDao.getUserType(userName);
    }

    @Override
    public String getEmail(String userName) throws SQLException, ClassNotFoundException {
        return userDao.getEmail(userName);
    }

    @Override
    public boolean updateUserPassword(String userName, String pswrd) throws SQLException, ClassNotFoundException {
        return userDao.updatePassword(userName,pswrd);
    }
}
