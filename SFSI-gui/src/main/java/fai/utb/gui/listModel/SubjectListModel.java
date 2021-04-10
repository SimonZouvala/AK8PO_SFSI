package fai.utb.gui.listModel;

import fai.utb.db.entity.Subject;

import javax.swing.*;
import java.util.List;

/**
 * @author Šimon Zouvala
 */
public class SubjectListModel extends AbstractListModel {
    private List<Subject> subjectList;

    public SubjectListModel(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    @Override
    public int getSize() {
        return subjectList.size();
    }

    @Override
    public Object getElementAt(int index) {
        return subjectList.get(index).getAcronym().toUpperCase();
    }

    public void addSubject(Subject subject) {
        subjectList.add(subject);
        int lastRow = subjectList.size() - 1;
        fireIntervalAdded(subject, lastRow, lastRow);
    }

    public void deleteSubject(Subject subject) {
        subjectList.remove(subject);
        int lastRow = subjectList.size() - 1;
        if (lastRow < 0) {
            lastRow = 0;
        }
        fireIntervalAdded(subject, lastRow, lastRow);
    }
}
