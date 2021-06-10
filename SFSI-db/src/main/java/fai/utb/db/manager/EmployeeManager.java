package fai.utb.db.manager;


import fai.utb.db.entity.Employee;

import java.util.List;
import java.util.UUID;

/**
 * This abstract class allows to manipulate with Employees.
 *
 * @author Šimon Zouvala
 */
public abstract class EmployeeManager extends BasicManager {

    /**
     * Create new {@link Employee} and save to relevant XML file.
     *
     * @param employee to save
     */
    public abstract void create(Employee employee);

    /**
     * Remove {@link Employee} and save changes to relevant XML file.
     *
     * @param employee to remove
     */
    public abstract void remove(Employee employee);

    /**
     * Get all {@link Employee}s from relevant XML file.
     *
     * @return list of all {@link Employee}s
     */
    public abstract List<Employee> getAllEmployees();

    /**
     * Get required {@link Employee} from relevant XML file.
     *
     * @param id of required employee
     * @return {@link Employee}
     */
    public abstract Employee getEmployee(UUID id);
}
