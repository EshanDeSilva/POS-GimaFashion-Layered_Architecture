package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDao;
import lk.ijse.pos.entity.Employer;
import lk.ijse.pos.model.EmployerDto;

import java.sql.SQLException;

public interface EmployerDao extends CrudDao<Employer,String> {
    Employer findByName(String name) throws SQLException, ClassNotFoundException;
    String findName(String employerId) throws SQLException, ClassNotFoundException;
}
