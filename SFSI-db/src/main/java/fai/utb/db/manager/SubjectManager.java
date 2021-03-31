package fai.utb.db.manager;

import fai.utb.db.entity.Subject;

import java.util.List;

public abstract class SubjectManager  extends BasicManager{

    public abstract void create(Subject subject);

    public abstract void setSubjectCapacity(Subject subjectCapacity, int newCapacity);

    public abstract void remove(Subject subject);

    public abstract List<Subject> getAllSubject();

    public abstract Subject getSubjectByAcronym(String acronym);

    public abstract Subject getSubject(Long id);
}
