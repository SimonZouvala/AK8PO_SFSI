package fai.utb.db.manager;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.Group;
import fai.utb.db.entity.Subject;
import fai.utb.db.entity.WorkLabel;

import java.util.List;
import java.util.UUID;
/**
 * This abstract class allows to manipulate with WorkLables.
 * @author Šimon Zouvala
 */
public abstract class WorkLabelManager extends BasicManager{
    /**
     * Depending on the number of students and the size of the classes for each subject,
     * they will generate {@link WorkLabel}s.
     */
    public abstract void generateWorkLabels();

    /**
     * Recalculation of {@link WorkLabel}s in case of loss or increase of classroom capacity.
     * @param subject where number of classroom capacity was changes
     */
    public abstract void generateWorkLabelsAfterUpgrade(Subject subject);

    /**
     * Recalculation of {@link WorkLabel}s in case of loss or increase of students in group.
     * @param group where number of students was changes
     */
    public abstract void generateWorkLabelsAfterUpgrade(Group group);

    /**
     * Create new {@link WorkLabel} and save to relevant XML file.
     * @param workLabel to save
     */
    public abstract void create(WorkLabel workLabel);

    /**
     * Add {@link Employee} to required {@link WorkLabel} and save changes to relevant XML file
     * @param employee to add
     * @param workLabel where will be employee added
     */
    public abstract void addEmployeeToWorkLabel(Employee employee, WorkLabel workLabel);

    /**
     * Remove {@link Employee} from required {@link WorkLabel} and save changes to relevant XML file
     * @param workLabel where will be employee removed
     */
    public abstract void removeEmployeeFromWorkLabel(WorkLabel workLabel);

    /**
     * Remove {@link WorkLabel} and save changes to relevant XML file.
     * @param workLabel to remove
     */
    public abstract void remove(WorkLabel workLabel);

    /**
     * Get required {@link WorkLabel} from relevant XML file.
     * @param id id of required work label
     * @return {@link WorkLabel}
     */
    public abstract WorkLabel getWorkLabel(UUID id);

    /**
     * Get all {@link WorkLabel}s from relevant XML file.
     * @return list of all {@link WorkLabel}s
     */
    public abstract List<WorkLabel> getAllWorkLabels();

    /**
     * Get all {@link WorkLabel}s from relevant XML file where is not {@link Employee}
     * @return list of {@link WorkLabel}s
     */
    public abstract List<WorkLabel> getWorkLabelsWithoutEmployee();

    /**
     * Get all {@link WorkLabel}s from relevant XML file where is zero students.
     * @return list of  {@link WorkLabel}s
     */
    public abstract List<WorkLabel> getWorkLabelsWithoutStudents();

}
