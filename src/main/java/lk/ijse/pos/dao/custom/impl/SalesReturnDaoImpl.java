package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.SalesReturnDao;
import lk.ijse.pos.dao.custom.impl.util.CrudUtil;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.model.SalesReturnDto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SalesReturnDaoImpl implements SalesReturnDao {
    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT returnId FROM salesReturn ORDER BY returnId DESC LIMIT 1");
        if (resultSet.next()){
            String lastId = resultSet.getString(1).split("[-]")[1];
            return String.format("R-%08d",Integer.valueOf(lastId)+1);
        }
        return "R-00000001";
    }

    @Override
    public SalesReturnDto find(String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<SalesReturnDto> findAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(SalesReturnDto dto) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            Boolean isSaved = CrudUtil.execute("INSERT INTO salesReturn VALUES (?,?,?,?)",dto.getReturnId(),
                    dto.getOrderId(),dto.getDate(),dto.getTotal());
            if (isSaved){
                Boolean allDetailSaved = true;
                for (int i = 0; i < dto.getDto().size(); i++) {
                    Boolean detailSaved = CrudUtil.execute("INSERT INTO salesReturnDetails VALUES (?,?,?,?,?,?)",
                            dto.getDto().get(i).getReturnId(),dto.getDto().get(i).getItemCode(),
                            dto.getDto().get(i).getQty(),dto.getDto().get(i).getDiscRate(),
                            dto.getDto().get(i).getUnitPrice(),dto.getDto().get(i).getAmount());
                    if (!detailSaved){
                        allDetailSaved = false;
                    }
                }
                if (allDetailSaved){
                    connection.commit();
                    return true;
                }
            }
            return false;
        }catch (SQLException | ClassNotFoundException e){
            connection.rollback();
            e.printStackTrace();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public boolean update(SalesReturnDto dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exists(SalesReturnDto salesReturnDto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public double getDailyReturnTotal() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT total FROM salesReturn WHERE date = CURDATE()");
        double total = 0;
        while (resultSet.next()){
            total += resultSet.getDouble(1);
        }
        return total;
    }
}
