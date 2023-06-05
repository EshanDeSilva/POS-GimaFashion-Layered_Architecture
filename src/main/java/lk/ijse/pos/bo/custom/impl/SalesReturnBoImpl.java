package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.bo.custom.SalesReturnBo;
import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.dao.custom.SalesReturnDao;
import lk.ijse.pos.dao.custom.SalesReturnDetailsDao;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.entity.SalesReturn;
import lk.ijse.pos.entity.SalesReturnDetails;
import lk.ijse.pos.model.SalesReturnDto;

import java.sql.Connection;
import java.sql.SQLException;

public class SalesReturnBoImpl implements SalesReturnBo {
    SalesReturnDao salesReturnDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.SALES_RETURN);
    SalesReturnDetailsDao salesReturnDetailsDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.SALES_RETURN_DETAILS);

    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        return salesReturnDao.getId();
    }

    @Override
    public boolean saveSalesReturn(SalesReturnDto dto) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean isSaved = salesReturnDao.save(new SalesReturn(dto.getReturnId(),dto.getOrderId(),dto.getDate(),dto.getTotal()));
            if (isSaved){
                for (int i = 0; i < dto.getDto().size(); i++) {
                    boolean isDetailSaved = salesReturnDetailsDao.save(new SalesReturnDetails(
                            dto.getDto().get(i).getReturnId(),dto.getDto().get(i).getItemCode(),dto.getDto().get(i).getQty(),
                            dto.getDto().get(i).getDiscRate(),dto.getDto().get(i).getUnitPrice(),dto.getDto().get(i).getAmount()
                    ));
                    if (!isDetailSaved){
                        return false;
                    }
                }
                connection.commit();
                return true;
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
    public double getDailySalesReturnTotal() throws SQLException, ClassNotFoundException {
        return salesReturnDao.getDailyReturnTotal();
    }
}
