package fai.utb.db.manager;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.Group;
import fai.utb.db.entity.Subject;
import fai.utb.db.entity.WorkLabel;

import java.util.List;
import java.util.UUID;
/**
 * @author Šimon Zouvala
 */
public abstract class WorkLabelManager extends BasicManager{

    public abstract void generateWorkLabels();

    public abstract void generateWorkLabelsAfterUpgrade(Subject subject);

    public abstract void generateWorkLabelsAfterUpgrade(Group group);

    public abstract void create(WorkLabel workLabel);

    public abstract void addEmployeeToWorkLabel(Employee employee, WorkLabel workLabel);

    public abstract void removeEmployeeFromWorkLabel(WorkLabel workLabel);

    public abstract void remove(WorkLabel workLabel);

    public abstract WorkLabel getWorkLabel(UUID id);

    public abstract List<WorkLabel> getAllWorkLabels();

    public abstract List<WorkLabel> getWorkLabelsWithoutEmployee();

    public abstract List<WorkLabel> getWorkLabelsWithoutStudents();

}
