package fai.utb.db.manager;


import fai.utb.db.entity.Employee;

import java.util.List;

public abstract class EmployeeManager extends BasicManager{


    public abstract void create(Employee employee);

    public abstract void remove(Employee employee);

    public abstract List<Employee> getAllEmployees();

    public abstract Employee getEmployee(Long id);

}
