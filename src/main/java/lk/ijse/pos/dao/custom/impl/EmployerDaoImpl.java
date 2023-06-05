package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.EmployerDao;
import lk.ijse.pos.entity.Employer;
import lk.ijse.pos.model.EmployerDto;
import lk.ijse.pos.dao.custom.impl.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployerDaoImpl implements EmployerDao {

    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM employer ORDER BY id DESC LIMIT 1");
        if (resultSet.next()){
            String lastId = resultSet.getString(1).split("[-]")[1];
            return String.format("EMP-%04d",Integer.valueOf(lastId)+1);
        }
        return "EMP-0001";
    }

    @Override
    public boolean save(Employer employer) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO employer VALUES (?,?,?,?,?,?,?,?,?)",
                employer.getId(),
                employer.getTitle(),
                employer.getName(),
                employer.getNic(),
                employer.getDob(),
                employer.getAddress(),
                employer.getBankAccountNo(),
                employer.getBankBranch(),
                employer.getContactNo());
    }

    @Override
    public boolean update(Employer employer) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE employer SET title=?,name=?,nic=?,dob=?,address=?,bankAccountNo=?," +
                        "bankBranch=?,contactNo=? WHERE id=?", employer.getTitle(),employer.getName(),employer.getNic(),employer.getDob(),
                employer.getAddress(),employer.getBankAccountNo(),employer.getBankBranch(),employer.getContactNo(),employer.getId());
    }

    @Override
    public boolean exists(Employer employer) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List<Employer> findAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM employer");
        List<Employer> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(new Employer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getString(9)
            ));
        }
        return list;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM employer WHERE id=?",id);
    }

    @Override
    public Employer find(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM employer WHERE id=?", id);
        while (resultSet.next()){
            return new Employer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getString(9)
            );
        }
        return new Employer();
    }

    @Override
    public Employer findByName(String name) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM employer WHERE name=?", name);
        while (resultSet.next()){
            return new Employer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getString(9)
            );
        }
        return new Employer();
    }

    @Override
    public String findName(String employerId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT name FROM employer WHERE id=?",employerId);
        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }
}
