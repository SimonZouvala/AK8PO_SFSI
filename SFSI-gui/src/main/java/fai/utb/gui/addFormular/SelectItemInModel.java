package fai.utb.gui.addFormular;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.Group;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class SelectItemInModel extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<Object> choicesComboBox;
    private Object choicesObject;

    public SelectItemInModel(List<?> objects) {
        createUIComponents();
        addItemsToComboBox(objects);
        this.setContentPane(contentPane);
        this.pack();
        this.setModal(true);
    }

    private void createUIComponents() {
        getRootPane().setDefaultButton(buttonOK);
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public Object getChoicesObject() {
        return choicesObject;
    }

    private void addItemsToComboBox(List<?> objects) {
        if (objects.get(0) instanceof Employee) {
            List<Employee> employeeList = (List<Employee>) objects;
            for (Employee employee : employeeList) {
                choicesComboBox.addItem(employee);
            }
        } else if (objects.get(0) instanceof Group) {
            List<Group> groupLis = (List<Group>) objects;
            for (Group group : groupLis) {
                choicesComboBox.addItem(group);
            }
        }
    }

    private void onOK() {
        choicesObject = choicesComboBox.getSelectedItem();
        System.out.println(choicesObject);
        dispose();
    }

    private void onCancel() {
        choicesObject = null;
        dispose();
    }
}
