package fai.utb.db.manager;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.WorkLabel;

public interface WorkLabelManager {

    void generateWorkLabels();

    void generateWorkLabelsAfterUpgrade();

    void createWorkLabel(WorkLabel workLabel);

    void addEmployeeToWorkLabel(Employee employee, WorkLabel workLabel);

    void removeEmployeeFromWorkLabel(Employee employee, WorkLabel workLabel);

    WorkLabel getWorkLabel(Long id);

}
