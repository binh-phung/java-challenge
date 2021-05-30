package jp.co.axa.apidemo.services;

import javassist.NotFoundException;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> retrieveEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees;
    }

    public Employee getEmployee(Long employeeId) {
        Optional<Employee> optEmp = employeeRepository.findById(employeeId);
        return optEmp.orElse(null);
    }

    public void addEmployee(Employee employee){
        employee.setId(null);
        this.employeeRepository.save(employee);
    }

    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public boolean isExisting(Long employeeId) {
        return this.employeeRepository.existsById(employeeId);
    }
}