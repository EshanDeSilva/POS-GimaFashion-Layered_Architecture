package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBo;
import lk.ijse.pos.model.EmployerDto;

import java.sql.SQLException;
import java.util.List;

public interface EmployerBo extends SuperBo {
    String getId() throws SQLException, ClassNotFoundException;
    boolean saveEmployer(EmployerDto dto) throws SQLException, ClassNotFoundException;
    boolean updateEmployer(EmployerDto dto) throws SQLException, ClassNotFoundException;
    List<EmployerDto> findAllEmployers() throws SQLException, ClassNotFoundException;
    boolean deleteEmployer(String id) throws SQLException, ClassNotFoundException;
    EmployerDto findEmployer(String id) throws SQLException, ClassNotFoundException;
    EmployerDto findEmployerByName(String name) throws SQLException, ClassNotFoundException;
    String findNameOfEmployer(String employerId) throws SQLException, ClassNotFoundException;
}
