package fai.utb.gui.listModel;

import fai.utb.db.entity.Employee;

import javax.swing.*;
import java.util.List;

/**
 * List model handles employee data to be displayed in the table.
 *
 * @author Šimon Zouvala
 */
public class EmployeeListModel extends AbstractListModel {


    private final List<Employee> employeeList;

    public EmployeeListModel(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @Override
    public int getSize() {
        return employeeList.size();
    }

    @Override
    public Object getElementAt(int index) {
        return employeeList.get(index).getName().toUpperCase() +" "+ employeeList.get(index).getSurname().toUpperCase();
    }

    /**
     * Add new employee to list model
     * @param employee to create
     */
    public void addEmployee(Employee employee){
        employeeList.add(employee);
        int lastRow = employeeList.size()-1;
        fireIntervalAdded(employee,lastRow,lastRow);
    }

    /**
     * Remove employee to list model
     * @param employee to remove
     */
    public void deleteEmployee(Employee employee){
        employeeList.remove(employee);
        int lastRow = employeeList.size()-1;
        if (lastRow <0){
            lastRow =0;
        }
        fireIntervalRemoved(employee,lastRow,lastRow);

    }

    /**
     *
     * @return all {@link Employee}s from list model
     */
    public List<Employee> getEmployeesList(){
        return employeeList;
    }
}
