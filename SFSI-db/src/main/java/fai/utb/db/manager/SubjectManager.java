package fai.utb.db.manager;

import fai.utb.db.entity.Subject;

import java.util.List;

public interface SubjectManager {

    void create(Subject subject);

    void setSubjectCapacity(Subject subjectCapacity, int newCapacity);

    void remove(Subject subject);

    List<Subject> getAllSubject();

    Subject getSubjectByAcronym(String acronym);

    Subject getSubject(Long id);
}
