package lk.ijse.pos.dao.custom.impl.util;

import lk.ijse.pos.db.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {
    public static <T>T execute(String sql,Object...params) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);

        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject((i+1),params[i]);
        }

        if (sql.startsWith("SELECT")||sql.startsWith("select")){
            ResultSet resultSet = preparedStatement.executeQuery();
            return (T)resultSet;
        }
        return (T)(Boolean)(preparedStatement.executeUpdate()>0);
    }
}
