package fai.utb.gui.listModel;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.Group;

import javax.swing.*;
import java.util.List;

/**
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
        return employeeList.get(index).getName() +" "+ employeeList.get(index).getSurname();
    }

    public void addEmployee(Employee employee){
        employeeList.add(employee);
        int lastRow = employeeList.size()-1;
        fireIntervalAdded(employee,lastRow,lastRow);
    }

    public void deleteEmployee(Employee employee){
        employeeList.remove(employee);
        int lastRow = employeeList.size()-1;
        if (lastRow <0){
            lastRow =0;
        }
        fireIntervalRemoved(employee,lastRow,lastRow);

    }
}
