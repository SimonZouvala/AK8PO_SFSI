package fai.utb.db.manager;


import fai.utb.db.entity.Employee;
import fai.utb.db.entity.Group;

import java.util.List;
import java.util.UUID;
/**
 * @author Šimon Zouvala
 */
public abstract class EmployeeManager extends BasicManager{


    public abstract void create(Employee employee);

    public abstract void remove(Employee employee);

    public abstract List<Employee> getAllEmployees();

    public abstract Employee getEmployee(UUID id);

    public abstract void setWorkPoints(Employee employee);

}
