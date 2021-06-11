package fai.utb.gui.addFormular;

import fai.utb.db.entity.Group;
import fai.utb.db.entity.Subject;
import fai.utb.db.entity.entityEnum.Completion;
import fai.utb.db.entity.entityEnum.Language;
import fai.utb.db.exception.ValidationException;
import fai.utb.db.manager.GroupManagerImpl;
import fai.utb.db.manager.SubjectManager;
import fai.utb.gui.I18n;
import fai.utb.gui.checkers.CheckAddSubjectResult;
import fai.utb.gui.listModel.GroupListModel;
import fai.utb.gui.listModel.SubjectListModel;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Form for create new {@link Subject}
 *
 * @author Šimon Zouvala
 */
public class AddSubject extends JFrame {

    private static final I18n I18N = new I18n(AddSubject.class);
    private final SubjectManager subjectManager;
    private final SubjectListModel subjectListModel;
    private JPanel addSubjectPanel;
    private JTextField acronymTextField;
    private JTextField nameOfSubjectTextField;
    private JTextField teacherTextField;
    private JTextField lectureCapacityTextField;
    private JTextField seminarCapacityTextField;
    private JTextField exerciseCapacityTextField;
    private JComboBox<String> numberOfWeeksComboBox;
    private JComboBox<String> completionComboBox;
    private JComboBox<String> languageComboBox;
    private JTextField classroomCapacityTextField;
    private JButton saveButton;
    private JButton removeButton;
    private JButton addButton;
    private JTextArea groupsTextArea;
    private Language language;
    private Completion completion;
    private Subject subject;
    private final GroupListModel groupListModel;
    private List<Group> availableGroups;
    private int numberOfWeeks;

    /**
     * @param subjectManager   current subject manager
     * @param subjectListModel current subject list model
     */
    public AddSubject(SubjectManager subjectManager, SubjectListModel subjectListModel) {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.subjectManager = subjectManager;
        this.subjectListModel = subjectListModel;
        groupListModel = new GroupListModel();
        createUIComponents();

        completionComboBox.addActionListener(e -> {
            String selectedItem = Objects.requireNonNull(completionComboBox.getSelectedItem()).toString();

            switch (selectedItem) {
                case "Zápočet" -> completion = Completion.Z;
                case "Klasifikovaný zápočet" -> completion = Completion.KL;
                case "Zkouška" -> completion = Completion.ZK;
                default -> completion = null;
            }
        });

        languageComboBox.addActionListener(e -> {
            String selectedItem = Objects.requireNonNull(languageComboBox.getSelectedItem()).toString();

            switch (selectedItem) {
                case "Čeština" -> language = Language.CZ;
                case "Angličtina" -> language = Language.EN;
                default -> language = null;
            }
        });

        numberOfWeeksComboBox.addActionListener(e -> {
            String selectedItem = Objects.requireNonNull(numberOfWeeksComboBox.getSelectedItem()).toString();

            if (!selectedItem.equals("")) {
                numberOfWeeks = Integer.parseInt(selectedItem);
            } else {
                numberOfWeeks = 0;
            }
        });

        saveButton.addActionListener(e -> {
            String acronym = acronymTextField.getText();
            String name = nameOfSubjectTextField.getText();
            String teacher = teacherTextField.getText();
            String lectureCapacity = lectureCapacityTextField.getText();
            String seminarCapacity = seminarCapacityTextField.getText();
            String exerciseCapacity = exerciseCapacityTextField.getText();
            String classroomCapacity = classroomCapacityTextField.getText();

            ConfirmSwingWorker confirmSwingWorker = new ConfirmSwingWorker(acronym, name, teacher,
                    lectureCapacity, seminarCapacity, exerciseCapacity, classroomCapacity);
            confirmSwingWorker.execute();
        });

        addButton.addActionListener(e -> {
            availableGroups = getAvailableGroups();

            if (availableGroups.size() != 0) {
                SelectItemInModel dialog = new SelectItemInModel(availableGroups);
                dialog.setVisible(true);
                System.out.println(dialog.getChoicesObject());
                Group group = (Group) dialog.getChoicesObject();

                if (group != null) {
                    groupListModel.addGroup(group);
                    groupsTextArea.setText(groupListModel.toString());
                }
            } else {
                JOptionPane.showMessageDialog(null, I18N.getString("AllGroupsJoined"));
            }

            removeButton.setEnabled(groupListModel.getSize() != 0);
        });

        removeButton.addActionListener(e -> {
            SelectItemInModel dialog = new SelectItemInModel(groupListModel.getGroupsList());
            dialog.setVisible(true);
            Group group = (Group) dialog.getChoicesObject();

            if (group != null) {
                groupListModel.deleteGroup(group);
                groupsTextArea.setText(groupListModel.toString());
            }

            if (groupListModel.getSize() == 0) {
                removeButton.setEnabled(false);
            }
        });
    }

    private void createUIComponents() {
        languageComboBox.addItem("");
        languageComboBox.addItem("Čeština");
        languageComboBox.addItem("Angličtina");

        completionComboBox.addItem("");
        completionComboBox.addItem("Zápočet");
        completionComboBox.addItem("Klasifikovaný zápočet");
        completionComboBox.addItem("Zkouška");
        numberOfWeeksComboBox.addItem("");

        for (int i = 1; i < 16; i++) {
            numberOfWeeksComboBox.addItem(String.valueOf(i));
        }

        removeButton.setEnabled(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setContentPane(addSubjectPanel);
        this.pack();
    }

    private class ConfirmSwingWorker extends SwingWorker<CheckAddSubjectResult, CheckAddSubjectResult> {

        private final String acronym;
        private final String name;
        private final String teacher;
        private final String lectureCapacity;
        private final String seminarCapacity;
        private final String exerciseCapacity;
        private final String classroomCapacity;

        public ConfirmSwingWorker(String acronym, String name, String teacher, String lectureCapacity,
                                  String seminarCapacity, String exerciseCapacity, String classroomCapacity) {
            this.acronym = acronym;
            this.name = name;
            this.teacher = teacher;
            this.lectureCapacity = lectureCapacity;
            this.seminarCapacity = seminarCapacity;
            this.exerciseCapacity = exerciseCapacity;
            this.classroomCapacity = classroomCapacity;
        }

        @Override
        protected CheckAddSubjectResult doInBackground() throws Exception {
            if (acronym == null || acronym.length() < 1) {
                return CheckAddSubjectResult.ACRONYM_EMPTY;
            }
            if (name == null || name.length() < 1) {
                return CheckAddSubjectResult.NAME_EMPTY;
            }
            if (teacher == null || teacher.length() < 1) {
                return CheckAddSubjectResult.TEACHER_EMPTY;
            }
            if (lectureCapacity == null || lectureCapacity.length() < 1) {
                return CheckAddSubjectResult.LECTURE_CAPACITY_EMPTY;
            }
            if (seminarCapacity == null || seminarCapacity.length() < 1) {
                return CheckAddSubjectResult.SEMINAR_CAPACITY_EMPTY;
            }
            if (exerciseCapacity == null || exerciseCapacity.length() < 1) {
                return CheckAddSubjectResult.EXERCISE_CAPACITY_EMPTY;
            }
            if (numberOfWeeks == 0) {
                return CheckAddSubjectResult.NUMBER_OF_WEEKS_CAPACITY_NOT_SELECT;
            }
            if (completion == null) {
                return CheckAddSubjectResult.COMPLETION_NOT_SELECT;
            }
            if (classroomCapacity == null || classroomCapacity.length() < 1) {
                return CheckAddSubjectResult.CLASSROOM_CAPACITY_EMPTY;
            }
            if (language == null) {
                return CheckAddSubjectResult.LANGUAGE_NOT_SELECT;
            }
            if (groupListModel.getSize() == 0) {
                return CheckAddSubjectResult.GROUP_LIST_NOT_SELECT;
            }

            int lecture_cap;

            try {
                lecture_cap = Integer.parseInt(lectureCapacity);
            } catch (NumberFormatException e) {
                return CheckAddSubjectResult.LECTURE_CAPACITY_INVALID;
            }

            if (lecture_cap < 0) {
                return CheckAddSubjectResult.LECTURE_CAPACITY_NEGATIVE;
            }

            int seminar_cap;

            try {
                seminar_cap = Integer.parseInt(seminarCapacity);
            } catch (NumberFormatException e) {
                return CheckAddSubjectResult.SEMINAR_CAPACITY_INVALID;
            }

            if (seminar_cap < 0) {
                return CheckAddSubjectResult.SEMINAR_CAPACITY_NEGATIVE;
            }

            int exercise_cap;

            try {
                exercise_cap = Integer.parseInt(exerciseCapacity);
            } catch (NumberFormatException e) {
                return CheckAddSubjectResult.EXERCISE_CAPACITY_INVALID;
            }

            if (exercise_cap < 0) {
                return CheckAddSubjectResult.EXERCISE_CAPACITY_NEGATIVE;
            }

            int classroom_cap;

            try {
                classroom_cap = Integer.parseInt(classroomCapacity);
            } catch (NumberFormatException e) {
                return CheckAddSubjectResult.CLASSROOM_CAPACITY_INVALID;
            }

            if (classroom_cap < 0) {
                return CheckAddSubjectResult.CLASSROOM_CAPACITY_NEGATIVE;
            }

            try {
                subject = new Subject(acronym, name, teacher, lecture_cap, seminar_cap, exercise_cap,
                        numberOfWeeks, completion, classroom_cap, language, groupListModel.getGroupsList());
                subjectManager.create(subject);
            } catch (ValidationException e) {
                return CheckAddSubjectResult.SUBJECT_ALREADY_EXIST;
            }
            return CheckAddSubjectResult.SUBJECT_ADD;
        }


        @Override
        protected void done() {
            CheckAddSubjectResult result = null;
            try {
                result = get();
            } catch (InterruptedException e) {
                throw new AssertionError("Interrupted", e);
            } catch (ExecutionException e) {
                JOptionPane.showMessageDialog(null, "ExecutionException");
            }
            if (result == CheckAddSubjectResult.SUBJECT_ADD) {
                subjectListModel.addSubject(subject);
                setVisible(false);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, I18N.getString(Objects.requireNonNull(result)));
            }
        }
    }

    private List<Group> getAvailableGroups() {
        List<Group> groupList = new GroupManagerImpl().getAllGroup();

        if (groupListModel == null || groupListModel.getSize() == 0) {
            return groupList;
        }
        for (Group group : groupListModel.getGroupsList()) {
            groupList.remove(group);
        }
        System.out.println(groupList.toString());
        return groupList;
    }

}




