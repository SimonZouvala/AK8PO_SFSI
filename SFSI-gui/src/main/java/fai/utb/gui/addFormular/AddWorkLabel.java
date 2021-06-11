package fai.utb.gui.addFormular;

import fai.utb.db.entity.WorkLabel;
import fai.utb.db.entity.entityEnum.Completion;
import fai.utb.db.entity.entityEnum.Language;
import fai.utb.db.entity.entityEnum.LessonType;
import fai.utb.db.exception.ValidationException;
import fai.utb.db.manager.WorkLabelManager;
import fai.utb.gui.I18n;
import fai.utb.gui.checkers.CheckAddWorkLabelResult;
import fai.utb.gui.listModel.WorkLabelListModel;

import javax.swing.*;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Form for create new {@link WorkLabel}
 *
 * @author Šimon Zouvala
 */
public class AddWorkLabel extends JFrame {

    private static final I18n I18N = new I18n(AddWorkLabel.class);
    private final WorkLabelManager workLabelManager;
    private final WorkLabelListModel workLabelListModel;
    private JPanel addWorkLabelPanel;
    private JButton saveButton;
    private JTextField nameTextField;
    private JComboBox<String> languageComboBox;
    private JTextField pointsTextField;
    private JTextField numberOfStudentsTestField;
    private JComboBox<String> lessonTypeComboBox;
    private JComboBox<String> completionComboBox;
    private JComboBox<String> numberOfWeeksComboBox;
    private JTextField numberOfHoursTextField;
    private Language language;
    private int numberOfWeeks;
    private Completion completion;
    private WorkLabel workLabel;
    private LessonType lessonType;

    /**
     * @param workLabelManager   current workLabel manager
     * @param workLabelListModel current workLabel list model
     */
    public AddWorkLabel(WorkLabelManager workLabelManager, WorkLabelListModel workLabelListModel) {
        this.workLabelManager = workLabelManager;
        this.workLabelListModel = workLabelListModel;
        createUIComponents();
    }

    private void createUIComponents() {
        completionComboBox.addItem("");
        completionComboBox.addItem("Zápočet");
        completionComboBox.addItem("Klasifikovaný zápočet");
        completionComboBox.addItem("Zkouška");

        languageComboBox.addItem("");
        languageComboBox.addItem("Čeština");
        languageComboBox.addItem("Angličtina");

        lessonTypeComboBox.addItem("");
        lessonTypeComboBox.addItem("Přednáška");
        lessonTypeComboBox.addItem("Seminář");
        lessonTypeComboBox.addItem("Cvičení");
        numberOfWeeksComboBox.setEnabled(false);
        numberOfHoursTextField.setEnabled(false);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setContentPane(addWorkLabelPanel);
        this.pack();

        numberOfWeeksComboBox.addItem("");

        for (int i = 1; i < 16; i++) {
            numberOfWeeksComboBox.addItem(String.valueOf(i));
        }

        languageComboBox.addActionListener(e -> {
            String selectedItem = Objects.requireNonNull(languageComboBox.getSelectedItem()).toString();

            switch (selectedItem) {
                case "Čeština" -> language = Language.CZ;
                case "Angličtina" -> language = Language.EN;
                default -> language = null;
            }
        });

        lessonTypeComboBox.addActionListener(e -> {
            String selectedItem = Objects.requireNonNull(lessonTypeComboBox.getSelectedItem()).toString();

            switch (selectedItem) {
                case "Přednáška" -> lessonType = LessonType.LECTURE;
                case "Seminář" -> lessonType = LessonType.SEMINAR;
                case "Cvičení" -> lessonType = LessonType.EXERCISE;
                default -> lessonType = null;
            }

            if (lessonType != null) {
                numberOfWeeksComboBox.setEnabled(true);
                numberOfHoursTextField.setEnabled(true);
                completionComboBox.setEnabled(false);
            } else {
                numberOfWeeksComboBox.setEnabled(false);
                numberOfHoursTextField.setEnabled(false);
                completionComboBox.setEnabled(true);
            }
        });

        completionComboBox.addActionListener(e -> {
            String selectedItem = Objects.requireNonNull(completionComboBox.getSelectedItem()).toString();

            switch (selectedItem) {
                case "Zápočet" -> completion = Completion.Z;
                case "Klasifikovaný zápočet" -> completion = Completion.KL;
                case "Zkouška" -> completion = Completion.ZK;
                default -> completion = null;
            }

            lessonTypeComboBox.setEnabled(completion == null);
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
            String name = nameTextField.getText();
            String points = pointsTextField.getText();
            String numberOfStudents = numberOfStudentsTestField.getText();
            String numberOfHours = numberOfHoursTextField.getText();
            ConfirmSwingWorker confirmSwingWorker = new ConfirmSwingWorker(
                    name, points, numberOfStudents, numberOfHours);
            confirmSwingWorker.execute();
        });
    }


    private class ConfirmSwingWorker extends SwingWorker<CheckAddWorkLabelResult, CheckAddWorkLabelResult> {
        private final String name;
        private final String points;
        private final String numberOfStudents;
        private final String numberOfHours;

        public ConfirmSwingWorker(String name, String points, String numberOfStudents, String numberOfHours) {
            this.name = name;
            this.points = points;
            this.numberOfStudents = numberOfStudents;
            this.numberOfHours = numberOfHours;
        }

        @Override
        protected CheckAddWorkLabelResult doInBackground() throws Exception {
            if (name == null || name.length() < 1) {
                return CheckAddWorkLabelResult.NAME_EMPTY;
            }
            if (language == null) {
                return CheckAddWorkLabelResult.LANGUAGE_NOT_SELECT;
            }
            if (points == null || points.length() < 1) {
                return CheckAddWorkLabelResult.POINTS_EMPTY;
            }
            if (numberOfStudents == null || numberOfStudents.length() < 1) {
                return CheckAddWorkLabelResult.NUMBER_OF_STUDENTS_EMPTY;
            }

            double points_int;

            try {
                points_int = Double.parseDouble(points);
            } catch (NumberFormatException e) {
                return CheckAddWorkLabelResult.POINTS_INVALID;
            }

            if (points_int <= 0.0) {
                return CheckAddWorkLabelResult.POINTS_NEGATIVE;
            }

            int numberOfStudents_int;

            try {
                numberOfStudents_int = Integer.parseInt(numberOfStudents);
            } catch (NumberFormatException e) {
                return CheckAddWorkLabelResult.NUMBER_OF_STUDENTS_INVALID;
            }

            if (numberOfStudents_int < 0) {
                return CheckAddWorkLabelResult.NUMBER_OF_STUDENTS_NEGATIVE;
            }

            if (lessonType == null && completion == null) {
                return CheckAddWorkLabelResult.LESSON_TYPE_AND_COMPLETION_NOT_SELECT;
            }
            if (completion == null) {
                if (numberOfWeeks == 0) {
                    return CheckAddWorkLabelResult.NUMBER_OF_WEEKS_CAPACITY_NOT_SELECT;
                }
                if (numberOfHours == null || numberOfHours.length() < 1) {
                    return CheckAddWorkLabelResult.NUMBER_OF_HOURS_EMPTY;
                }

                int numberOfHours_int;

                try {
                    numberOfHours_int = Integer.parseInt(numberOfHours);
                } catch (NumberFormatException e) {
                    return CheckAddWorkLabelResult.NUMBER_OF_HOURS_INVALID;
                }

                if (numberOfHours_int <= 0) {
                    return CheckAddWorkLabelResult.NUMBER_OF_HOURS_NEGATIVE;
                }

                try {
                    workLabel = new WorkLabel(name, language, points_int, numberOfStudents_int,
                            lessonType, null, numberOfWeeks, numberOfHours_int);
                    workLabelManager.create(workLabel);
                } catch (ValidationException e) {
                    return CheckAddWorkLabelResult.WORKLABEL_ALREADY_EXIST_Lesson;
                }

            } else {
                try {
                    workLabel = new WorkLabel(name, language, points_int, numberOfStudents_int,
                            null, completion, 0, 0);
                    workLabelManager.create(workLabel);
                } catch (ValidationException e) {
                    return CheckAddWorkLabelResult.WORKLABEL_ALREADY_EXIST;
                }
            }
            return CheckAddWorkLabelResult.WORKLABEL_ADD;
        }

        @Override
        protected void done() {
            CheckAddWorkLabelResult result = null;
            try {
                result = get();
            } catch (InterruptedException e) {
                throw new AssertionError("Interrupted", e);
            } catch (ExecutionException e) {
                JOptionPane.showMessageDialog(null, "ExecutionException");
            }

            if (result == CheckAddWorkLabelResult.WORKLABEL_ADD) {
                workLabelListModel.addWorkLabel(workLabel);
                setVisible(false);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, I18N.getString(Objects.requireNonNull(result)));
            }
        }
    }
}
