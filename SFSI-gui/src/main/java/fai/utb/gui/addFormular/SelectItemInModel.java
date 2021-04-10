package fai.utb.gui.addFormular;

import fai.utb.db.entity.Group;
import fai.utb.db.manager.GroupManagerImpl;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class SelectItemInModel extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<Object> choicesComboBox;
    private Group choicesGroup;


    public SelectItemInModel(List<Group> groups) {
        createUIComponents();
        addItemsToComboBox(groups);
        this.setContentPane(contentPane);
        this.pack();
        this.setModal(true);
    }

    private void createUIComponents() {

        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


    }


    public SelectItemInModel() {
        createUIComponents();
//        addItemsToComboBox(groups);
        this.setContentPane(contentPane);
        this.pack();
        this.setModal(true);

    }

    public Group getChoicesGroup() {
        return choicesGroup;
    }

    private void addItemsToComboBox(List<Group> groups) {
        for (Group group : groups) {
            choicesComboBox.addItem(group);
        }
    }


    private void onOK() {
        choicesGroup = (Group) choicesComboBox.getSelectedItem();
        System.out.println(choicesGroup);

//        setVisible(true);
//        return result;


        dispose();

    }

    private void onCancel() {
        choicesGroup =null;
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        SelectItemInModel dialog = new SelectItemInModel( new GroupManagerImpl().getAllGroup());
        dialog.setVisible(true);
        System.exit(0);
    }
}
