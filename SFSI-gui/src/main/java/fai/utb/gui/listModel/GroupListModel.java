package fai.utb.gui.listModel;

import fai.utb.db.entity.Group;

import javax.swing.*;
import java.util.List;
/**
 * @author Šimon Zouvala
 */
public class GroupListModel extends AbstractListModel {


    private final List<Group> groupsList;

    public GroupListModel(List<Group> groupsList) {
        this.groupsList = groupsList;
    }

    @Override
    public int getSize() {
        return groupsList.size();
    }

    @Override
    public Object getElementAt(int index) {
        return groupsList.get(index).getFieldOfStudy();
    }

    public void addGroup(Group group) {
        groupsList.add(group);
        int lastRow = groupsList.size() - 1;
        fireIntervalAdded(group, lastRow, lastRow);
    }

    public void deleteGroup(Group group) {
        groupsList.remove(group);
        int lastRow = groupsList.size() - 1;
        if (lastRow < 0) {
            lastRow = 0;
        }
        fireIntervalAdded(group, lastRow, lastRow);
    }
}
