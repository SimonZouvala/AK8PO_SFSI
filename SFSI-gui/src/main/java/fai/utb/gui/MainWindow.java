package fai.utb.gui;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.Group;
import fai.utb.db.entity.Subject;
import fai.utb.db.entity.WorkLabel;
import fai.utb.db.manager.*;
import fai.utb.gui.addFormular.*;
import fai.utb.gui.listModel.EmployeeListModel;
import fai.utb.gui.listModel.GroupListModel;
import fai.utb.gui.listModel.SubjectListModel;
import fai.utb.gui.listModel.WorkLabelListModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Šimon Zouvala
 */
public class MainWindow extends JFrame {

    private static final I18n I18N = new I18n(MainWindow.class);
    private JPanel mainPanel;
    private JButton groupButton;
    private JButton subjectButton;
    private JButton employeeButton;
    private JButton workLabelButton;
    private JButton addButton;
    private JButton removeButton;
    private JButton joinButton;
    private JTextArea showTextArea;
    private JList<String> selectionTable;
    private JButton generateButton;
    private JButton emptyWorkLabelButton;
    private JButton allButton;
    private JButton withoutStudentsButton;
    private JButton unJoinButton;
    private Group group;
    private Subject subject;
    private Employee employee;
    private WorkLabel workLabel;

    public MainWindow() {
        super("Hello World");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        createUIComponents();
    }

    private void createUIComponents() {
        showTextArea.setText("");
        selectionTable.setVisible(false);
        joinButton.setVisible(false);
        addButton.setVisible(false);
        removeButton.setVisible(false);
        generateButton.setVisible(false);
        emptyWorkLabelButton.setVisible(false);
        allButton.setVisible(false);
        withoutStudentsButton.setVisible(false);
        unJoinButton.setVisible(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();

        joinButton.addActionListener(e -> {
            showTextArea.setText("");
            selectionTable.clearSelection();
            joinButton.setEnabled(false);
            SelectItemInModel dialog = new SelectItemInModel(new EmployeeManagerImpl().getAllEmployees());
            dialog.setVisible(true);
            System.out.println(dialog.getChoicesObject());
            Employee employee = (Employee) dialog.getChoicesObject();
            System.out.println(employee);
            if (employee != null) {
                new WorkLabelManagerImpl().addEmployeeToWorkLabel(
                        employee, new WorkLabelManagerImpl().getAllWorkLabels().get(selectionTable.getSelectedIndex()));
            }
        });

        unJoinButton.addActionListener(e -> {
            showTextArea.setText("");
            selectionTable.clearSelection();
            unJoinButton.setEnabled(false);
            new WorkLabelManagerImpl().removeEmployeeFromWorkLabel(
                    new WorkLabelManagerImpl().getAllWorkLabels().get(selectionTable.getSelectedIndex()));
        });

        removeButton.addActionListener(e -> {
            showTextArea.setText("");
            RemoveSwingWorker removeSwingWorker;
            int indexOfValue = selectionTable.getSelectedIndex();
            if ((selectionTable.getModel() instanceof GroupListModel)) {
                removeSwingWorker = new RemoveSwingWorker(new GroupManagerImpl(), indexOfValue);
            } else if ((selectionTable.getModel() instanceof SubjectListModel)) {
                removeSwingWorker = new RemoveSwingWorker(new SubjectManagerImpl(), indexOfValue);
            } else if ((selectionTable.getModel() instanceof EmployeeListModel)) {
                removeSwingWorker = new RemoveSwingWorker(new EmployeeManagerImpl(), indexOfValue);
            } else
                removeSwingWorker = new RemoveSwingWorker(new WorkLabelManagerImpl(), indexOfValue);

            removeSwingWorker.execute();
        });

        subjectButton.addActionListener(e -> {
            prepareButtons();
            SubjectListSwingWorker subjectListSwingWorker = new SubjectListSwingWorker();
            subjectListSwingWorker.execute();
        });

        employeeButton.addActionListener(e -> {
            prepareButtons();
            EmployeeListSwingWorker employeeListSwingWorker = new EmployeeListSwingWorker();
            employeeListSwingWorker.execute();
        });

        groupButton.addActionListener(e -> {
            prepareButtons();
            GroupListSwingWorker groupListSwingWorker = new GroupListSwingWorker();
            groupListSwingWorker.execute();
        });

        showAllWorkLabels(workLabelButton);

        selectionTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showTextArea.setText("");
                PrintSwingWorker printSwingWorker;
                int indexOfValue = selectionTable.getSelectedIndex();
                if ((selectionTable.getModel() instanceof GroupListModel)) {
                    printSwingWorker = new PrintSwingWorker(new GroupManagerImpl(), indexOfValue);
                } else if ((selectionTable.getModel() instanceof SubjectListModel)) {
                    printSwingWorker = new PrintSwingWorker(new SubjectManagerImpl(), indexOfValue);
                } else if ((selectionTable.getModel() instanceof EmployeeListModel)) {
                    printSwingWorker = new PrintSwingWorker(new EmployeeManagerImpl(), indexOfValue);
                } else
                    printSwingWorker = new PrintSwingWorker(new WorkLabelManagerImpl(), indexOfValue);

                printSwingWorker.execute();
            }
        });

        addButton.addActionListener(e -> {
            if (selectionTable.getModel() instanceof GroupListModel) {
                AddGroup newGroup = new AddGroup(new GroupManagerImpl(), (GroupListModel) selectionTable.getModel());
                newGroup.setVisible(true);
            }
            if (selectionTable.getModel() instanceof SubjectListModel) {
                AddSubject newSubject = new AddSubject(new SubjectManagerImpl(), (SubjectListModel) selectionTable.getModel());
                newSubject.setVisible(true);
            }
            if (selectionTable.getModel() instanceof EmployeeListModel) {
                AddEmployee newEmployee = new AddEmployee(new EmployeeManagerImpl(), (EmployeeListModel) selectionTable.getModel());
                newEmployee.setVisible(true);
            }
            if (selectionTable.getModel() instanceof WorkLabelListModel) {
                AddWorkLabel newWorkLabel = new AddWorkLabel(new WorkLabelManagerImpl(), (WorkLabelListModel) selectionTable.getModel());
                newWorkLabel.setVisible(true);
            }
        });

        generateButton.addActionListener(e -> {
            prepareButtonsWorkLabel();
            WorkLabelListSwingWorker workLabelListSwingWorker = new WorkLabelListSwingWorker(1);
            workLabelListSwingWorker.execute();
        });

        showAllWorkLabels(allButton);
        emptyWorkLabelButton.addActionListener(e -> {
            prepareButtonsWorkLabel();
            WorkLabelListSwingWorker workLabelListSwingWorker = new WorkLabelListSwingWorker(2);
            workLabelListSwingWorker.execute();
        });

        withoutStudentsButton.addActionListener(e -> {
            prepareButtonsWorkLabel();
            WorkLabelListSwingWorker workLabelListSwingWorker = new WorkLabelListSwingWorker(3);
            workLabelListSwingWorker.execute();
        });
    }

    private void prepareButtonsWorkLabel() {
        showTextArea.setText("");
        joinButton.setEnabled(false);
        removeButton.setEnabled(false);
        selectionTable.clearSelection();
    }

    private void prepareButtons() {
        showTextArea.setText("");
        addButton.setVisible(true);
        removeButton.setVisible(true);
        selectionTable.setVisible(true);
        joinButton.setVisible(false);
        removeButton.setEnabled(false);
        generateButton.setVisible(false);
        emptyWorkLabelButton.setVisible(false);
        allButton.setVisible(false);
        withoutStudentsButton.setVisible(false);
        unJoinButton.setVisible(false);
    }

    private void showAllWorkLabels(JButton button) {
        button.addActionListener(e -> {
            WorkLabelListSwingWorker workLabelListSwingWorker = new WorkLabelListSwingWorker(0);
            workLabelListSwingWorker.execute();

            showTextArea.setText("");
            addButton.setVisible(true);
            removeButton.setVisible(true);
            selectionTable.setVisible(true);
            joinButton.setVisible(true);
            joinButton.setEnabled(false);
            removeButton.setEnabled(false);
            generateButton.setVisible(true);
            emptyWorkLabelButton.setVisible(true);
            allButton.setVisible(true);
            withoutStudentsButton.setVisible(true);
            unJoinButton.setVisible(true);
            unJoinButton.setEnabled(false);
        });
    }

    private class PrintSwingWorker extends SwingWorker<Object, Object> {

        private final GroupManager groupManager;
        private final EmployeeManager employeeManager;
        private final SubjectManager subjectManager;
        private final WorkLabelManager workLabelManager;
        private final int index;

        public PrintSwingWorker(GroupManager groupManager, int index) {
            this.groupManager = groupManager;
            this.index = index;
            this.employeeManager = null;
            this.subjectManager = null;
            this.workLabelManager = null;
        }

        public PrintSwingWorker(EmployeeManager employeeManager, int index) {
            this.employeeManager = employeeManager;
            this.index = index;
            this.subjectManager = null;
            this.workLabelManager = null;
            this.groupManager = null;
        }

        public PrintSwingWorker(WorkLabelManager workLabelManager, int index) {
            this.workLabelManager = workLabelManager;
            this.index = index;
            this.employeeManager = null;
            this.subjectManager = null;
            this.groupManager = null;
        }

        public PrintSwingWorker(SubjectManager subjectManager, int index) {
            this.subjectManager = subjectManager;
            this.index = index;
            this.workLabelManager = null;
            this.groupManager = null;
            this.employeeManager = null;
        }

        @Override
        protected Object doInBackground() throws Exception {
            if (groupManager != null) {
                group = groupManager.getAllGroup().get(index);
                return group;
            }
            if (subjectManager != null) {
                subject = subjectManager.getAllSubject().get(index);
                return subject;
            }
            if (employeeManager != null) {
                employee = employeeManager.getAllEmployees().get(index);
                return employee;
            } else {
                workLabel = workLabelManager.getAllWorkLabels().get(index);
                return workLabel;

            }
        }


        @Override
        protected void done() {
            try {
                showTextArea.append(get().toString());
                if (workLabel == get()) {
                    if (workLabel.getEmployeeId() == null) {
                        joinButton.setEnabled(true);
                        unJoinButton.setEnabled(false);
                    } else {
                        unJoinButton.setEnabled(true);
                        joinButton.setEnabled(false);
                    }

                } else {
                    joinButton.setEnabled(false);
                }
                removeButton.setEnabled(true);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class RemoveSwingWorker extends SwingWorker<Object, Void> {

        private final GroupManager groupManager;
        private final EmployeeManager employeeManager;
        private final SubjectManager subjectManager;
        private final WorkLabelManager workLabelManager;
        private final int index;

        public RemoveSwingWorker(GroupManager groupManager, int index) {
            this.groupManager = groupManager;
            this.index = index;
            this.employeeManager = null;
            this.subjectManager = null;
            this.workLabelManager = null;
        }

        public RemoveSwingWorker(EmployeeManager employeeManager, int index) {
            this.employeeManager = employeeManager;
            this.index = index;
            this.subjectManager = null;
            this.workLabelManager = null;
            this.groupManager = null;
        }

        public RemoveSwingWorker(WorkLabelManager workLabelManager, int index) {
            this.workLabelManager = workLabelManager;
            this.index = index;
            this.employeeManager = null;
            this.subjectManager = null;
            this.groupManager = null;
        }

        public RemoveSwingWorker(SubjectManager subjectManager, int index) {
            this.subjectManager = subjectManager;
            this.index = index;
            this.workLabelManager = null;
            this.groupManager = null;
            this.employeeManager = null;
        }

        @Override
        protected Object doInBackground() throws Exception {
            if (groupManager != null) {
                group = groupManager.getAllGroup().get(index);
                groupManager.remove(group);
                return group;
            }
            if (subjectManager != null) {
                subject = subjectManager.getAllSubject().get(index);
                subjectManager.remove(subject);
                return subject;
            }
            if (employeeManager != null) {
                employee = employeeManager.getAllEmployees().get(index);
                employeeManager.remove(employee);
                return employee;
            } else {
                workLabel = workLabelManager.getAllWorkLabels().get(index);
                workLabelManager.remove(workLabel);
                return workLabel;

            }
        }

        @Override
        protected void done() {

            try {
                if (group == get()) {
                    GroupListModel model = (GroupListModel) selectionTable.getModel();
                    model.deleteGroup(group);
                } else if (subject == get()) {
                    SubjectListModel model = (SubjectListModel) selectionTable.getModel();
                    model.deleteSubject(subject);
                } else if (employee == get()) {
                    EmployeeListModel model = (EmployeeListModel) selectionTable.getModel();
                    model.deleteEmployee(employee);
                } else if (workLabel == get()) {
                    WorkLabelListModel model = (WorkLabelListModel) selectionTable.getModel();
                    model.deleteWorkLabel(workLabel);
                }
            } catch (
                    InterruptedException ex) {
                throw new AssertionError("Interrupted", ex);
            } catch (ExecutionException ex) {

                JOptionPane.showMessageDialog(null, "Error with remove room.");
            }
            removeButton.setEnabled(false);
            showTextArea.setText("");
        }
    }


    private class GroupListSwingWorker extends SwingWorker<List<Group>, List<Group>> {

        public GroupListSwingWorker() {
        }

        @Override
        protected List<Group> doInBackground() throws Exception {
            List<Group> groupList = new GroupManagerImpl().getAllGroup();
            System.out.println(groupList.toString());
            return groupList;
        }

        @Override
        protected void done() {
            try {
                selectionTable.setModel((ListModel<String>) new GroupListModel(get()));
            } catch (InterruptedException ex) {
                throw new AssertionError("Interrupted", ex);
            } catch (ExecutionException ex) {
//                log.error("Execution exception in GuestListSwingWorker");
                JOptionPane.showMessageDialog(null, I18N.getString("GroupListShow"));
            }
        }
    }

    private class SubjectListSwingWorker extends SwingWorker<List<Subject>, List<Subject>> {

        public SubjectListSwingWorker() {
        }

        @Override
        protected List<Subject> doInBackground() throws Exception {
            List<Subject> subjectList = new SubjectManagerImpl().getAllSubject();
            System.out.println(subjectList.toString());
            return subjectList;
        }

        @Override
        protected void done() {
            try {
                selectionTable.setModel((ListModel<String>) new SubjectListModel(get()));
            } catch (InterruptedException ex) {
                throw new AssertionError("Interrupted", ex);
            } catch (ExecutionException ex) {
//                log.error("Execution exception in GuestListSwingWorker");
                JOptionPane.showMessageDialog(null, I18N.getString("SubjectListShow"));
            }//"Error with show subject."
        }
    }

    private class EmployeeListSwingWorker extends SwingWorker<List<Employee>, List<Employee>> {

        public EmployeeListSwingWorker() {
        }

        @Override
        protected List<Employee> doInBackground() throws Exception {
            List<Employee> employeeList = new EmployeeManagerImpl().getAllEmployees();
            System.out.println(employeeList.toString());
            return employeeList;
        }

        @Override
        protected void done() {
            try {
                selectionTable.setModel((ListModel<String>) new EmployeeListModel(get()));
            } catch (InterruptedException ex) {
                throw new AssertionError("Interrupted", ex);
            } catch (ExecutionException ex) {
//                log.error("Execution exception in GuestListSwingWorker");
                JOptionPane.showMessageDialog(null, I18N.getString("EmployeeListShow"));
            }
        }
    }


    private class WorkLabelListSwingWorker extends SwingWorker<List<WorkLabel>, List<WorkLabel>> {
        private final int number;

        public WorkLabelListSwingWorker(int number) {
            this.number = number;
        }

        @Override
        protected List<WorkLabel> doInBackground() throws Exception {
            if (number == 2) {
                return new WorkLabelManagerImpl().getWorkLabelsWithoutEmployee();
            }
            if (number == 3) {
                return new WorkLabelManagerImpl().getWorkLabelsWithoutStudents();
            }
            if (number == 1) {
                new WorkLabelManagerImpl().generateWorkLabels();
            }
            return new WorkLabelManagerImpl().getAllWorkLabels();
        }

        @Override
        protected void done() {
            try {
                selectionTable.setModel((ListModel<String>) new WorkLabelListModel(get()));
            } catch (InterruptedException ex) {
                throw new AssertionError("Interrupted", ex);
            } catch (ExecutionException ex) {
//                log.error("Execution exception in GuestListSwingWorker");
                JOptionPane.showMessageDialog(null, I18N.getString("WorkLabelListShow"));
            }
        }
    }

}
