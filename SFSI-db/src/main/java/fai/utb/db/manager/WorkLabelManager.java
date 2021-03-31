package fai.utb.db.manager;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.WorkLabel;

public abstract class WorkLabelManager extends BasicManager{

    public abstract void generateWorkLabels();

    public abstract void generateWorkLabelsAfterUpgrade();

    public abstract void createWorkLabel(WorkLabel workLabel);

    public abstract void addEmployeeToWorkLabel(Employee employee, WorkLabel workLabel);

    public abstract void removeEmployeeFromWorkLabel(Employee employee, WorkLabel workLabel);

    public abstract void removeWorkLabels(WorkLabel workLabel);

    public abstract WorkLabel getWorkLabel(Long id);

}
