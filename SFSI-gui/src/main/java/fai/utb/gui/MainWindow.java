package fai.utb.gui;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.Group;
import fai.utb.db.entity.Subject;
import fai.utb.db.entity.WorkLabel;
import fai.utb.db.manager.EmployeeManager;
import fai.utb.db.manager.GroupManager;
import fai.utb.db.manager.SubjectManager;
import fai.utb.db.manager.WorkLabelManager;
import fai.utb.gui.addFormular.AddEmployee;
import fai.utb.gui.addFormular.AddGroup;
import fai.utb.gui.addFormular.AddSubject;
import fai.utb.gui.addFormular.AddWorkLabel;
import fai.utb.gui.listModel.EmployeeListModel;
import fai.utb.gui.listModel.GroupListModel;
import fai.utb.gui.listModel.SubjectListModel;
import fai.utb.gui.listModel.WorkLabelListModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JList<String> selectionTabel;
    private JButton generateButton;
    private JButton emptyWorkLabelButton;
    private JButton allButton;
    private JButton withoutStudentsButton;
    private final GroupManager groupManager;
    private Group group;
    private final EmployeeManager employeeManager;
    private final WorkLabelManager workLabelManager;
    private final SubjectManager subjectManager;
    private Subject subject;
    private Employee employee;
    private WorkLabel workLabel;

    public MainWindow(GroupManager groupManager, SubjectManager subjectManager, EmployeeManager employeeManager, WorkLabelManager workLabelManager) {
        super("Hello World");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.groupManager = groupManager;
        this.subjectManager = subjectManager;
        this.employeeManager = employeeManager;
        this.workLabelManager = workLabelManager;
        createUIComponents();

    }


    private void createUIComponents() {
        showTextArea.setText("");
        selectionTabel.setVisible(false);
        joinButton.setVisible(false);
        addButton.setVisible(false);
        removeButton.setVisible(false);
        generateButton.setVisible(false);
        emptyWorkLabelButton.setVisible(false);
        allButton.setVisible(false);
        withoutStudentsButton.setVisible(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTextArea.setText("");
                RemoveSwingWorker removeSwingWorker;
                int indexOfValue = selectionTabel.getSelectedIndex();
                if ((selectionTabel.getModel() instanceof GroupListModel)) {
                    removeSwingWorker = new RemoveSwingWorker(groupManager, indexOfValue);
                } else if ((selectionTabel.getModel() instanceof SubjectListModel)) {
                    removeSwingWorker = new RemoveSwingWorker(subjectManager, indexOfValue);
                } else if ((selectionTabel.getModel() instanceof EmployeeListModel)) {
                    removeSwingWorker = new RemoveSwingWorker(employeeManager, indexOfValue);
                } else
                    removeSwingWorker = new RemoveSwingWorker(workLabelManager, indexOfValue);

                removeSwingWorker.execute();

            }
        });
        subjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SubjectListSwingWorker subjectListSwingWorker = new SubjectListSwingWorker();
                subjectListSwingWorker.execute();

                showTextArea.setText("");
                addButton.setVisible(true);
                removeButton.setVisible(true);
                selectionTabel.setVisible(true);
                joinButton.setVisible(false);
                removeButton.setEnabled(false);
                generateButton.setVisible(false);
                emptyWorkLabelButton.setVisible(false);
                allButton.setVisible(false);
                withoutStudentsButton.setVisible(false);

            }
        });
        employeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EmployeeListSwingWorker employeeListSwingWorker = new EmployeeListSwingWorker();
                employeeListSwingWorker.execute();

                showTextArea.setText("");
                addButton.setVisible(true);
                removeButton.setVisible(true);
                selectionTabel.setVisible(true);
                joinButton.setVisible(false);
                removeButton.setEnabled(false);
                generateButton.setVisible(false);
                emptyWorkLabelButton.setVisible(false);
                allButton.setVisible(false);
                withoutStudentsButton.setVisible(false);

            }
        });
        workLabelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkLabelListSwingWorker workLabelListSwingWorker = new WorkLabelListSwingWorker();
                workLabelListSwingWorker.execute();

                showTextArea.setText("");
                addButton.setVisible(true);
                removeButton.setVisible(true);
                selectionTabel.setVisible(true);
                joinButton.setVisible(true);
                joinButton.setEnabled(false);
                removeButton.setEnabled(false);
                generateButton.setVisible(true);
                emptyWorkLabelButton.setVisible(true);
                allButton.setVisible(true);
                withoutStudentsButton.setVisible(true);
            }
        });

        groupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GroupListSwingWorker groupListSwingWorker = new GroupListSwingWorker();
                groupListSwingWorker.execute();

                showTextArea.setText("");
                addButton.setVisible(true);
                removeButton.setVisible(true);
                selectionTabel.setVisible(true);
                joinButton.setVisible(false);
                removeButton.setEnabled(false);
                generateButton.setVisible(false);
                emptyWorkLabelButton.setVisible(false);
                allButton.setVisible(false);
                withoutStudentsButton.setVisible(false);
            }
        });


        selectionTabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showTextArea.setText("");
                PrintSwingWorker printSwingWorker;
                int indexOfValue = selectionTabel.getSelectedIndex();
                if ((selectionTabel.getModel() instanceof GroupListModel)) {
                    printSwingWorker = new PrintSwingWorker(groupManager, indexOfValue);
                } else if ((selectionTabel.getModel() instanceof SubjectListModel)) {
                    printSwingWorker = new PrintSwingWorker(subjectManager, indexOfValue);
                } else if ((selectionTabel.getModel() instanceof EmployeeListModel)) {
                    printSwingWorker = new PrintSwingWorker(employeeManager, indexOfValue);
                } else
                    printSwingWorker = new PrintSwingWorker(workLabelManager, indexOfValue);

                printSwingWorker.execute();
            }
        });


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectionTabel.getModel() instanceof GroupListModel) {
                    AddGroup newGroup = new AddGroup(groupManager, (GroupListModel) selectionTabel.getModel());
                    newGroup.setVisible(true);
                }
                if (selectionTabel.getModel() instanceof SubjectListModel) {
                    AddSubject newSubject = new AddSubject(subjectManager, (SubjectListModel) selectionTabel.getModel());
                    newSubject.setVisible(true);
                }
                if (selectionTabel.getModel() instanceof EmployeeListModel) {
                    AddEmployee newEmployee = new AddEmployee(employeeManager, (EmployeeListModel) selectionTabel.getModel());
                    newEmployee.setVisible(true);
                }
                if (selectionTabel.getModel() instanceof WorkLabelListModel) {
                    AddWorkLabel newWorkLabel = new AddWorkLabel(workLabelManager, (WorkLabelListModel) selectionTabel.getModel());
                    newWorkLabel.setVisible(true);
                }
            }
        });
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkLabelListSwingWorker workLabelListSwingWorker = new WorkLabelListSwingWorker(true);
                workLabelListSwingWorker.execute();
                showTextArea.setText("");
                joinButton.setEnabled(false);
                removeButton.setEnabled(false);
            }
        });
        allButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        emptyWorkLabelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        withoutStudentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
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
                    joinButton.setEnabled(true);
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
                    GroupListModel model = (GroupListModel) selectionTabel.getModel();
                    model.deleteGroup(group);
                } else if (subject == get()) {
                    SubjectListModel model = (SubjectListModel) selectionTabel.getModel();
                    model.deleteSubject(subject);
                } else if (employee == get()) {
                    EmployeeListModel model = (EmployeeListModel) selectionTabel.getModel();
                    model.deleteEmployee(employee);
                } else if (workLabel == get()) {
                    WorkLabelListModel model = (WorkLabelListModel) selectionTabel.getModel();
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
            List<Group> groupList = groupManager.getAllGroup();
            System.out.println(groupList.toString());
            return groupList;
        }

        @Override
        protected void done() {
            try {
                selectionTabel.setModel((ListModel<String>) new GroupListModel(get()));
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
            List<Subject> subjectList = subjectManager.getAllSubject();
            System.out.println(subjectList.toString());
            return subjectList;
        }

        @Override
        protected void done() {
            try {
                selectionTabel.setModel((ListModel<String>) new SubjectListModel(get()));
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
            List<Employee> employeeList = employeeManager.getAllEmployees();
            System.out.println(employeeList.toString());
            return employeeList;
        }

        @Override
        protected void done() {
            try {
                selectionTabel.setModel((ListModel<String>) new EmployeeListModel(get()));
            } catch (InterruptedException ex) {
                throw new AssertionError("Interrupted", ex);
            } catch (ExecutionException ex) {
//                log.error("Execution exception in GuestListSwingWorker");
                JOptionPane.showMessageDialog(null, I18N.getString("EmployeeListShow"));
            }
        }
    }


    private class WorkLabelListSwingWorker extends SwingWorker<List<WorkLabel>, List<WorkLabel>> {

        private boolean generate = false;

        public WorkLabelListSwingWorker() {
        }
        public WorkLabelListSwingWorker(boolean generate) {
            this.generate = generate;
        }

        @Override
        protected List<WorkLabel> doInBackground() throws Exception {
            if (generate){
                workLabelManager.generateWorkLabels();
            }
            List<WorkLabel> workLabelList = workLabelManager.getAllWorkLabels();
            System.out.println(workLabelList.toString());
            return workLabelList;
        }

        @Override
        protected void done() {
            try {
                selectionTabel.setModel((ListModel<String>) new WorkLabelListModel(get()));
            } catch (InterruptedException ex) {
                throw new AssertionError("Interrupted", ex);
            } catch (ExecutionException ex) {
//                log.error("Execution exception in GuestListSwingWorker");
                JOptionPane.showMessageDialog(null, I18N.getString("WorkLabelListShow"));
            }
        }
    }

}
