package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.bo.custom.EmployerBo;
import lk.ijse.pos.dao.DaoFactory;
import lk.ijse.pos.dao.custom.EmployerDao;
import lk.ijse.pos.entity.Employer;
import lk.ijse.pos.model.EmployerDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployerBoImpl implements EmployerBo {
    EmployerDao employerDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.EMPLOYER);

    @Override
    public String getId() throws SQLException, ClassNotFoundException {
        return employerDao.getId();
    }

    @Override
    public boolean saveEmployer(EmployerDto dto) throws SQLException, ClassNotFoundException {
        return employerDao.save(new Employer(
                dto.getId(),
                dto.getTitle(),
                dto.getName(),
                dto.getNic(),
                dto.getDob(),
                dto.getAddress(),
                dto.getBankAcc(),
                dto.getBankBranch(),
                dto.getContact()
        ));
    }

    @Override
    public boolean updateEmployer(EmployerDto dto) throws SQLException, ClassNotFoundException {
        return employerDao.update(new Employer(
                dto.getId(),
                dto.getTitle(),
                dto.getName(),
                dto.getNic(),
                dto.getDob(),
                dto.getAddress(),
                dto.getBankAcc(),
                dto.getBankBranch(),
                dto.getContact()
        ));
    }

    @Override
    public List<EmployerDto> findAllEmployers() throws SQLException, ClassNotFoundException {
        List<EmployerDto> dtoList = new ArrayList<>();
        for (Employer employer:employerDao.findAll()) {
            dtoList.add(new EmployerDto(
                    employer.getId(),
                    employer.getTitle(),
                    employer.getName(),
                    employer.getNic(),
                    employer.getDob(),
                    employer.getAddress(),
                    employer.getBankAccountNo(),
                    employer.getBankBranch(),
                    employer.getContactNo()
            ));
        }
        return dtoList;
    }

    @Override
    public boolean deleteEmployer(String id) throws SQLException, ClassNotFoundException {
        return employerDao.delete(id);
    }

    @Override
    public EmployerDto findEmployer(String id) throws SQLException, ClassNotFoundException {
        Employer employer = employerDao.find(id);
        return new EmployerDto(
                employer.getId(),
                employer.getTitle(),
                employer.getName(),
                employer.getNic(),
                employer.getDob(),
                employer.getAddress(),
                employer.getBankAccountNo(),
                employer.getBankBranch(),
                employer.getContactNo()
        );
    }

    @Override
    public EmployerDto findEmployerByName(String name) throws SQLException, ClassNotFoundException {
        Employer employer = employerDao.findByName(name);
        return new EmployerDto(
                employer.getId(),
                employer.getTitle(),
                employer.getName(),
                employer.getNic(),
                employer.getDob(),
                employer.getAddress(),
                employer.getBankAccountNo(),
                employer.getBankBranch(),
                employer.getContactNo()
        );
    }

    @Override
    public String findNameOfEmployer(String employerId) throws SQLException, ClassNotFoundException {
        return employerDao.findName(employerId);
    }
}
