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
    public boolean save(Employer dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO employer VALUES (?,?,?,?,?,?,?,?,?)",
                dto.getId(),
                dto.getTitle(),
                dto.getName(),
                dto.getNic(),
                dto.getDob(),
                dto.getAddress(),
                dto.getBankAccountNo(),
                dto.getBankBranch(),
                dto.getContactNo());
    }

    @Override
    public boolean update(Employer dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE employer SET title=?,name=?,nic=?,dob=?,address=?,bankAccountNo=?," +
                        "bankBranch=?,contactNo=? WHERE id=?", dto.getTitle(),dto.getName(),dto.getNic(),dto.getDob(),
                dto.getAddress(),dto.getBankAccountNo(),dto.getBankBranch(),dto.getContactNo(),dto.getId());
    }

    @Override
    public boolean exists(Employer employerDto) throws SQLException, ClassNotFoundException {
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
