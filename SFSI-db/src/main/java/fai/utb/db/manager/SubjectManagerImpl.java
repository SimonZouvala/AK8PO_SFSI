package fai.utb.db.manager;

import fai.utb.db.entity.Subject;
import org.w3c.dom.Document;

import java.util.List;

public class SubjectManagerImpl implements SubjectManager {

    private Document document;

    public SubjectManagerImpl(Document document) {
        this.document = document;
    }

    @Override
    public void create(Subject subject) {

    }

    @Override
    public void setSubjectCapacity(Subject subjectCapacity, int newCapacity) {

    }

    @Override
    public void remove(Subject subject) {

    }

    @Override
    public List<Subject> getAllSubject() {
        return null;
    }

    @Override
    public Subject getSubjectByAcronym(String acronym) {
        return null;
    }

    @Override
    public Subject getSubject(Long id) {
        return null;
    }
}
