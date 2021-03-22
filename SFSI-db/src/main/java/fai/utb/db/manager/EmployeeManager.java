package fai.utb.db.manager;


import fai.utb.db.entity.Employee;

import java.util.List;

public interface EmployeeManager {


    void create(Employee employee);

    void remove(Employee employee);

    List<Employee> getAllEmployees();

    Employee getEmployee(Long id);

}
