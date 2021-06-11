package fai.utb.gui;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.Group;
import fai.utb.db.entity.Subject;
import fai.utb.db.entity.WorkLabel;
import fai.utb.db.exception.ValidationException;
import fai.utb.db.manager.EmployeeManagerImpl;
import fai.utb.db.manager.GroupManagerImpl;
import fai.utb.db.manager.SubjectManagerImpl;
import fai.utb.db.manager.WorkLabelManagerImpl;
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
 * Main class for GUI. All interactions within the application are implemented here
 *
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
        super("Software, pro tajemníka ústavu:");
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
            Employee employee = (Employee) dialog.getChoicesObject();

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
            int indexOfValue = selectionTable.getSelectedIndex();
            RemoveSwingWorker removeSwingWorker = new RemoveSwingWorker(indexOfValue);
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
                int indexOfValue = selectionTable.getSelectedIndex();
                PrintSwingWorker printSwingWorker = new PrintSwingWorker(indexOfValue);
                printSwingWorker.execute();
            }
        });

        addButton.addActionListener(e -> {
            if (selectionTable.getModel() instanceof GroupListModel) {
                AddGroup newGroup = new AddGroup(new GroupManagerImpl(),
                        (GroupListModel) selectionTable.getModel());
                newGroup.setVisible(true);
            }
            if (selectionTable.getModel() instanceof SubjectListModel) {
                AddSubject newSubject = new AddSubject(new SubjectManagerImpl(),
                        (SubjectListModel) selectionTable.getModel());
                newSubject.setVisible(true);
            }
            if (selectionTable.getModel() instanceof EmployeeListModel) {
                AddEmployee newEmployee = new AddEmployee(new EmployeeManagerImpl(),
                        (EmployeeListModel) selectionTable.getModel());
                newEmployee.setVisible(true);
            }
            if (selectionTable.getModel() instanceof WorkLabelListModel) {
                AddWorkLabel newWorkLabel = new AddWorkLabel(new WorkLabelManagerImpl(),
                        (WorkLabelListModel) selectionTable.getModel());
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
        editButton.addActionListener(e -> {
            showTextArea.setText("");
            joinButton.setEnabled(false);

            EditQuantity editQuantity = new EditQuantity(selectionTable, selectionTable.getSelectedIndex());
            editQuantity.setVisible(true);
            selectionTable.clearSelection();
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

        /**
         * Print relevant item from relevant list model
         *
         * @param index of item in list model
         */
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
                employee = ((EmployeeListModel) selectionTable.getModel()).getEmployeesList().get(index);
                return employee;
            } else {
                workLabel = ((WorkLabelListModel) selectionTable.getModel()).getWorkLabelList().get(index);
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
            } catch (ExecutionException | InterruptedException e) {
                throw new ValidationException("Problem to show list model", e);
            }
        }
    }

    private class RemoveSwingWorker extends SwingWorker<Object, Void> {

        private final int index;

        /**
         * Remove relevant item from relevant list model
         *
         * @param index of item in list model
         */
        public RemoveSwingWorker(int index) {
            this.index = index;
        }

        @Override
        protected Object doInBackground() throws Exception {
            if (selectionTable.getModel() instanceof GroupListModel) {
                group = ((GroupListModel) selectionTable.getModel()).getGroupsList().get(index);
                new GroupManagerImpl().remove(group);
                return group;
            }
            if (selectionTable.getModel() instanceof SubjectListModel) {
                subject = ((SubjectListModel) selectionTable.getModel()).getSubjectList().get(index);
                new SubjectManagerImpl().remove(subject);
                return subject;
            }
            if (selectionTable.getModel() instanceof EmployeeListModel) {
                employee = ((EmployeeListModel) selectionTable.getModel()).getEmployeesList().get(index);
                new EmployeeManagerImpl().remove(employee);
                return employee;
            } else {
                workLabel = ((WorkLabelListModel) selectionTable.getModel()).getWorkLabelList().get(index);
                new WorkLabelManagerImpl().remove(workLabel);
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
                    unJoinButton.setEnabled(false);
                    joinButton.setEnabled(false);
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

        /**
         * Print all Groups in selection table.
         */
        public GroupListSwingWorker() {
        }

        @Override
        protected List<Group> doInBackground() throws Exception {
            return new GroupManagerImpl().getAllGroup();
        }

        @Override
        protected void done() {
            try {
                selectionTable.setModel(new GroupListModel(get()));
            } catch (InterruptedException ex) {
                throw new AssertionError("Interrupted", ex);
            } catch (ExecutionException ex) {
                JOptionPane.showMessageDialog(null, I18N.getString("GroupListShow"));
            }
        }
    }

    private class SubjectListSwingWorker extends SwingWorker<List<Subject>, List<Subject>> {

        /**
         * Print all Subjects in selection table.
         */
        public SubjectListSwingWorker() {
        }

        @Override
        protected List<Subject> doInBackground() throws Exception {
            return new SubjectManagerImpl().getAllSubject();
        }

        @Override
        protected void done() {
            try {
                selectionTable.setModel(new SubjectListModel(get()));
            } catch (InterruptedException ex) {
                throw new AssertionError("Interrupted", ex);
            } catch (ExecutionException ex) {
                JOptionPane.showMessageDialog(null, I18N.getString("SubjectListShow"));
            }
        }
    }

    private class EmployeeListSwingWorker extends SwingWorker<List<Employee>, List<Employee>> {

        /**
         * Print all Employees in selection table.
         */
        public EmployeeListSwingWorker() {
        }

        @Override
        protected List<Employee> doInBackground() throws Exception {
            return new EmployeeManagerImpl().getAllEmployees();
        }

        @Override
        protected void done() {
            try {
                selectionTable.setModel(new EmployeeListModel(get()));
            } catch (InterruptedException ex) {
                throw new AssertionError("Interrupted", ex);
            } catch (ExecutionException ex) {
                JOptionPane.showMessageDialog(null, I18N.getString("EmployeeListShow"));
            }
        }
    }


    private class WorkLabelListSwingWorker extends SwingWorker<List<WorkLabel>, List<WorkLabel>> {
        private final int number;

        /**
         * Print all WorkLabels  in selection table or generate new work labels.
         * Number indicates the specification of the work labels
         * <p>
         * 1 = generate work labels and get all
         * 2 = get all work labels without employee
         * 3 = get all work labels without students
         *
         * @param number indicates the specification of the work labels
         */
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
                selectionTable.setModel(new WorkLabelListModel(get()));
            } catch (InterruptedException ex) {
                throw new AssertionError("Interrupted", ex);
            } catch (ExecutionException ex) {
                JOptionPane.showMessageDialog(null, I18N.getString("WorkLabelListShow"));
            }
        }
    }
}
