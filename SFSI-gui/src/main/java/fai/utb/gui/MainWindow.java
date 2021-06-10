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
    private JList<String> selectionTable;
    private JButton generateButton;
    private JButton emptyWorkLabelButton;
    private JButton allButton;
    private JButton withoutStudentsButton;
    private JButton unJoinButton;
    private JButton editButton;
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
        editButton.setVisible(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();

        joinButton.addActionListener(e -> {
            showTextArea.setText("");

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
            selectionTable.clearSelection();
            WorkLabelListSwingWorker workLabelListSwingWorker = new WorkLabelListSwingWorker(0);
            workLabelListSwingWorker.execute();
        });

        unJoinButton.addActionListener(e -> {
            showTextArea.setText("");

            unJoinButton.setEnabled(false);
            new WorkLabelManagerImpl().removeEmployeeFromWorkLabel(
                    new WorkLabelManagerImpl().getAllWorkLabels().get(selectionTable.getSelectedIndex()));
            selectionTable.clearSelection();
            WorkLabelListSwingWorker workLabelListSwingWorker = new WorkLabelListSwingWorker(0);
            workLabelListSwingWorker.execute();
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
            editButton.setVisible(true);
            editButton.setEnabled(false);
        });

        employeeButton.addActionListener(e -> {
            prepareButtons();
            EmployeeListSwingWorker employeeListSwingWorker = new EmployeeListSwingWorker();
            employeeListSwingWorker.execute();
            editButton.setVisible(false);
        });

        groupButton.addActionListener(e -> {
            prepareButtons();
            GroupListSwingWorker groupListSwingWorker = new GroupListSwingWorker();
            groupListSwingWorker.execute();
            editButton.setVisible(true);
            editButton.setEnabled(false);
        });

        showAllWorkLabels(workLabelButton);

        selectionTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showTextArea.setText("");
                PrintSwingWorker printSwingWorker;
                int indexOfValue = selectionTable.getSelectedIndex();
                printSwingWorker = new PrintSwingWorker(indexOfValue);
//                if ((selectionTable.getModel() instanceof GroupListModel)) {
//                    printSwingWorker = new PrintSwingWorker(new GroupManagerImpl(), indexOfValue);
//                } else if ((selectionTable.getModel() instanceof SubjectListModel)) {
//
//                } else if ((selectionTable.getModel() instanceof EmployeeListModel)) {
//                    printSwingWorker = new PrintSwingWorker(new EmployeeManagerImpl(), indexOfValue);
//                } else
//                    printSwingWorker = new PrintSwingWorker(new WorkLabelManagerImpl(), indexOfValue);

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
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//
//                joinButton.setEnabled(false);
//                SelectItemInModel dialog = new SelectItemInModel(new EmployeeManagerImpl().getAllEmployees());
//                dialog.setVisible(true);
//                System.out.println(dialog.getChoicesObject());
//                Employee employee = (Employee) dialog.getChoicesObject();

                showTextArea.setText("");
                joinButton.setEnabled(false);

                EditQuantity editQuantity = new EditQuantity(selectionTable, selectionTable.getSelectedIndex());
                editQuantity.setVisible(true);
                selectionTable.clearSelection();
//                if ((selectionTable.getModel() instanceof GroupListModel)) {
//
//                    ConfirmSwingWorker confirmSwingWorker = new ConfirmSwingWorker(new GroupManagerImpl(), editQuantity.getNumberToSet());
//                    confirmSwingWorker.execute();
//                } else if ((selectionTable.getModel() instanceof SubjectListModel)) {
//                    Subject subject = ((SubjectListModel) selectionTable.getModel()).getSubjectList().get(selectionTable.getSelectedIndex());
//                    System.out.println(subject);
//                    System.out.println(new SubjectManagerImpl().getAllSubject().get(selectionTable.getSelectedIndex()));
//
//                    ConfirmSwingWorker confirmSwingWorker = new ConfirmSwingWorker(new SubjectManagerImpl(), editQuantity.getNumberToSet());
//                    confirmSwingWorker.execute();
//                }
//                System.out.println(editQuantity.getNumberToSet());

            }
        });

    }

    private void prepareButtonsWorkLabel() {
        showTextArea.setText("");
        joinButton.setEnabled(false);
        removeButton.setEnabled(false);
        selectionTable.clearSelection();
        editButton.setVisible(false);
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

        private final int index;

        public PrintSwingWorker(int index) {
            this.index = index;
        }


        @Override
        protected Object doInBackground() throws Exception {
            if (selectionTable.getModel() instanceof GroupListModel) {
                group = ((GroupListModel) selectionTable.getModel()).getGroupsList().get(index);
                return group;
            }
            if (selectionTable.getModel() instanceof SubjectListModel) {
                subject = ((SubjectListModel) selectionTable.getModel()).getSubjectList().get(index);
                return subject;
            }
            if (selectionTable.getModel() instanceof EmployeeListModel) {
                employee = ((EmployeeListModel)selectionTable.getModel()).getEmployeesList().get(index);
                return employee;
            } else {
                workLabel = ((WorkLabelListModel)selectionTable.getModel()).getWorkLabelList().get(index);
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
                if (subject == get() || group == get()) {
                    editButton.setEnabled(true);
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
            System.out.println("Number:  " + number);
            if (number == 2) {
                return new WorkLabelManagerImpl().getWorkLabelsWithoutEmployee();
            }
            if (number == 3) {
                return new WorkLabelManagerImpl().getWorkLabelsWithoutStudents();
            }
            if (number == 1) {
                System.out.println("Number 1");
                new WorkLabelManagerImpl().generateWorkLabels();

                System.out.println(new WorkLabelManagerImpl().getAllWorkLabels());
            }

            return new WorkLabelManagerImpl().getAllWorkLabels();
        }

        @Override
        protected void done() {
            try {
                System.out.println("Před");

                selectionTable.setModel((ListModel<String>) new WorkLabelListModel(get()));
                System.out.println("po");
            } catch (InterruptedException ex) {
                throw new AssertionError("Interrupted", ex);
            } catch (ExecutionException ex) {
//                log.error("Execution exception in GuestListSwingWorker");
                JOptionPane.showMessageDialog(null, I18N.getString("WorkLabelListShow"));
            }
        }
    }
//
//    private class ConfirmSwingWorker extends SwingWorker<CheckEditNumber, CheckEditNumber> {
//
//        private final SubjectManagerImpl subjectManager;
//        private final String quantity;
//        private final GroupManagerImpl groupManager;
//
//        public ConfirmSwingWorker(SubjectManagerImpl subjectManager, String quantity) {
//            this.subjectManager = subjectManager;
//            this.quantity = quantity;
//            this.groupManager = null;
//        }
//
//        public ConfirmSwingWorker(GroupManagerImpl groupManager, String quantity) {
//            this.groupManager = groupManager;
//            this.quantity = quantity;
//            this.subjectManager = null;
//        }
//
//        @Override
//        protected CheckEditNumber doInBackground() throws Exception {
//
//            if (quantity == null || quantity.length() < 1) {
//                return CheckEditNumber.QUANTITY_UNIVERSE_EMPTY;
//            }
//            int quantity_int;
//            try {
//                quantity_int = Integer.parseInt(quantity);
//            } catch (NumberFormatException e) {
//                return CheckEditNumber.QUANTITY_UNIVERSE_INVALID;
//            }
//            if (quantity_int <= 0) {
//                return CheckEditNumber.QUANTITY_UNIVERSE_NEGATIVE;
//            }
//            try {
//                System.out.println("Dělám neco");
//                System.out.println(selectionTable.getSelectedIndex());
//                if (groupManager != null) {
//                    Group setGroup = groupManager.getAllGroup().get(selectionTable.getSelectedIndex());
//                    groupManager.setQuantity(setGroup, quantity_int);
//                    System.out.println("Dělám neco1");
//                } else if (subjectManager != null) {
//                    System.out.println("Dělám neco2");
//                    Subject setSeubject = subjectManager.getAllSubject().get(selectionTable.getSelectedIndex());
//                    System.out.println(setSeubject);
//
//
//                    subjectManager.setSubjectCapacity(setSeubject, quantity_int);
//                    System.out.println("Dělám neco-po vykonani");
//                }
//                return CheckEditNumber.OK;
//            } catch (ValidationException e) {
//                return CheckEditNumber.ERROR_WITH_NUMBER;
//            }
//
//
//        }
//
//        @Override
//        protected void done() {
//            CheckEditNumber result = null;
//            try {
//                System.out.println("Dělám neco3");
//                result = get();
//                System.out.println("Dělám neco4");
//                selectionTable.clearSelection();
//            } catch (InterruptedException e) {
//                throw new AssertionError("Interrupted", e);
//            } catch (ExecutionException e) {
//                JOptionPane.showMessageDialog(null, "ExecutionException-");
//            }
//            if (result == CheckEditNumber.OK) {
//                System.out.println("Set number is ok");
//            } else {
//                System.out.println(result);
//                JOptionPane.showMessageDialog(null, I18N.getString(Objects.requireNonNull(result)));
//            }
//        }
//    }

}
