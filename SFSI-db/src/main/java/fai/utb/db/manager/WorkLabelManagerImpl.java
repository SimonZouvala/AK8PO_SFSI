package fai.utb.db.manager;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.WorkLabel;
import org.w3c.dom.Document;

public class WorkLabelManagerImpl implements WorkLabelManager {

    private Document document;

    public WorkLabelManagerImpl(Document document) {
        this.document = document;
    }

    @Override
    public void generateWorkLabels() {

    }

    @Override
    public void generateWorkLabelsAfterUpgrade() {

    }

    @Override
    public void createWorkLabel(WorkLabel workLabel) {

    }

    @Override
    public void addEmployeeToWorkLabel(Employee employee, WorkLabel workLabel) {

    }

    @Override
    public void removeEmployeeFromWorkLabel(Employee employee, WorkLabel workLabel) {

    }

    @Override
    public WorkLabel getWorkLabel(Long id) {
        return null;
    }
}
