package fai.utb.gui.addFormular;

import fai.utb.db.manager.WorkLabelManager;
import fai.utb.gui.listModel.WorkLabelListModel;

import javax.swing.*;

/**
 * @author Šimon Zouvala
 */
public class AddWorkLabel extends JFrame {
    private WorkLabelManager workLabelManager;
    private WorkLabelListModel workLabelListModel;

    public AddWorkLabel(WorkLabelManager workLabelManager, WorkLabelListModel workLabelListModel) {

        this.workLabelManager = workLabelManager;
        this.workLabelListModel = workLabelListModel;
    }
}
