package fai.utb.db.manager;

import fai.utb.db.entity.Group;
import fai.utb.db.entity.Subject;

import java.util.List;
import java.util.UUID;
/**
 * @author Šimon Zouvala
 */
public abstract class SubjectManager  extends BasicManager{

    public abstract void create(Subject subject);

    public abstract void setSubjectCapacity(Subject subject, int newCapacity);

    public abstract void remove(Subject subject);

    public abstract List<Subject> getAllSubject();

    public abstract Subject getSubjectByAcronym(String acronym);

    public abstract Subject getSubject(UUID id);

    public abstract void removeGroupFromSubjects(Group group);
}
