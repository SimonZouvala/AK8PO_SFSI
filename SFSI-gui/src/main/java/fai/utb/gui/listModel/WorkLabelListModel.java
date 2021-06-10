package fai.utb.gui.listModel;

import fai.utb.db.entity.WorkLabel;

import javax.swing.*;
import java.util.List;

/**
 * @author Šimon Zouvala
 */
public class WorkLabelListModel extends AbstractListModel {

    private List<WorkLabel> workLabelList;

    public WorkLabelListModel(List<WorkLabel> workLabelList) {
        this.workLabelList = workLabelList;
    }

    @Override
    public int getSize() {
        return workLabelList.size();
    }

    @Override
    public Object getElementAt(int index) {
        return workLabelList.get(index).getName();
    }

    public void addWorkLabel(WorkLabel workLabel) {
        workLabelList.add(workLabel);
        int lastRow = workLabelList.size() - 1;
        fireIntervalAdded(workLabel, lastRow, lastRow);
    }

    public void deleteWorkLabel(WorkLabel workLabel) {
        workLabelList.remove(workLabel);
        int lastRow = workLabelList.size() - 1;
        if (lastRow < 0) {
            lastRow = 0;
        }
        fireIntervalAdded(workLabel, lastRow, lastRow);
    }

    public List<WorkLabel> getWorkLabelList(){return workLabelList;}
}
