package fai.utb.gui;

import fai.utb.db.manager.EmployeeManagerImpl;
import fai.utb.db.manager.GroupManagerImpl;
import fai.utb.db.manager.SubjectManagerImpl;
import fai.utb.db.manager.WorkLabelManagerImpl;

import java.awt.*;

/**
 * @author Šimon Zouvala
 */
public class Main {

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            new MainWindow(new GroupManagerImpl(), new SubjectManagerImpl(), new EmployeeManagerImpl(), new WorkLabelManagerImpl()).setVisible(true);
        });


    }
}
