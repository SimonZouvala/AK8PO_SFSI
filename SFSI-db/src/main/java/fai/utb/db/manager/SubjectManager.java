package fai.utb.db.manager;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.Group;
import fai.utb.db.entity.Subject;

import java.util.List;
import java.util.UUID;
/**
 * This abstract class allows to manipulate with Subjects.
 * @author Šimon Zouvala
 */
public abstract class SubjectManager  extends BasicManager{

    /**
     * Create new {@link Employee} and save to relevant XML file.
     * @param subject to save
     */
    public abstract void create(Subject subject);

    /**
     * Changes number of  classroom capacity and change work labels based on this.
     * @param subject where number of classroom capacity was changes
     * @param newCapacity value to change
     */
    public abstract void setSubjectCapacity(Subject subject, int newCapacity);

    /**
     * Remove {@link Subject} and save changes to relevant XML file.
     * @param subject to remove
     */
    public abstract void remove(Subject subject);

    /**
     * Get all {@link Subject}s from relevant XML file.
     * @return list of all {@link Subject}s
     */
    public abstract List<Subject> getAllSubject();

    /**
     * Get required {@link Subject} from relevant XML file.
     * @param id id of required subject
     * @return {@link Subject}
     */
    public abstract Subject getSubject(UUID id);

    /**
     * Remove {@link Group} from required {@link Subject}
     * @param group to remove
     */
    public abstract void removeGroupFromSubjects(Group group);
}
