package fai.utb.gui.addFormular;

import fai.utb.db.entity.Group;
import fai.utb.db.entity.entityEnum.Degree;
import fai.utb.db.entity.entityEnum.FormOfStudy;
import fai.utb.db.entity.entityEnum.Language;
import fai.utb.db.entity.entityEnum.Semester;
import fai.utb.db.exception.ValidationException;
import fai.utb.db.manager.GroupManager;
import fai.utb.gui.I18n;
import fai.utb.gui.checkers.CheckAddGroupResult;
import fai.utb.gui.listModel.GroupListModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * @author Šimon Zouvala
 */
public class AddGroup extends JFrame {

    private static final I18n I18N = new I18n(AddGroup.class);

    private GroupManager groupManager;
    private GroupListModel groupListModel;
    private JPanel addGroupPanel;
    private JComboBox<String> degreeComboBox;
    private JComboBox<String> formOfStudyComboBox;
    private JComboBox<String> languageComboBox;
    private JComboBox<String> semesterComboBox;
    private JTextField fieldOfStudyTextField;
    private JTextField quantityTextField;
    private JComboBox<String> gradeComboBox;
    private JButton saveButton;
    private JLabel test;
    private Degree degree;
    private FormOfStudy formOfStudy;
    private Semester semester;
    private Language language;
    private int grade;

    public AddGroup(GroupManager groupManager, GroupListModel groupListModel) throws HeadlessException {
        super();
        this.groupManager = groupManager;
        this.groupListModel = groupListModel;
        createUIComponents();


        degreeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                JComboBox cb = (JComboBox)e.getSource();
//                String degreeName = (String) cb.getI();
                String selectedItem = Objects.requireNonNull(degreeComboBox.getSelectedItem()).toString();
                switch (selectedItem) {
                    case "Bakalářský" -> degree = Degree.BC;
                    case "Magisterský" -> degree = Degree.MGR;
                    case "Doktorský" -> degree = Degree.DC;
                    default -> degree = null;
                }


                test.setText(degree.toString());
//                System.out.println(degreeComboBox);
//                System.out.println(degreeString);

            }
        });

        formOfStudyComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = Objects.requireNonNull(formOfStudyComboBox.getSelectedItem()).toString();
                switch (selectedItem) {
                    case "Prezenční" -> formOfStudy = FormOfStudy.P;
                    case "Kombinované" -> formOfStudy = FormOfStudy.K;
                    default -> formOfStudy = null;
                }


            }
        });
        gradeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = gradeComboBox.getSelectedItem().toString();
                if (!selectedItem.equals("")) {
                    grade = Integer.parseInt(selectedItem);
                } else {
                    grade = 0;
                }


            }
        });
        semesterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = Objects.requireNonNull(semesterComboBox.getSelectedItem()).toString();
                switch (selectedItem) {
                    case "Zimní" -> semester = Semester.ZS;
                    case "Letní" -> semester = Semester.LS;
                    default -> semester = null;
                }

            }
        });
        languageComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = Objects.requireNonNull(languageComboBox.getSelectedItem()).toString();
                switch (selectedItem) {
                    case "Čeština" -> language = Language.CZ;
                    case "Angličtina" -> language = Language.EN;
                    default -> language = null;
                }

            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(quantityTextField.getText());
                String fieldOfStudy = fieldOfStudyTextField.getText();
                String quantity = quantityTextField.getText();
                ConfirmSwingWorker confirmSwingWorker = new ConfirmSwingWorker(fieldOfStudy, quantity);
                confirmSwingWorker.execute();
//                test.setText(degreeComboBox.getSelectedItem().toString());
            }
        });
    }

    private class ConfirmSwingWorker extends SwingWorker<CheckAddGroupResult, CheckAddGroupResult> {

        private final String fieldOdStudy;
        private final String quantity;
        private Group group = null;

        public ConfirmSwingWorker(String fieldOfStudy, String quantity) {
            this.fieldOdStudy = fieldOfStudy;
            this.quantity = quantity;
        }

        @Override
        protected CheckAddGroupResult doInBackground() throws Exception {
            if (fieldOdStudy == null || fieldOdStudy.length() < 1) {
                return CheckAddGroupResult.FIELD_OF_STUDY_EMPTY;
            }
            if (degree == null) {
                return CheckAddGroupResult.DEGREE_NOT_SELECT;
            }
            if (formOfStudy == null) {
                return CheckAddGroupResult.FORM_OF_STUDY_NOT_SELECT;
            }
            if (grade < 1) {
                return CheckAddGroupResult.GRADE_NOT_SELECT;
            }
            if (semester == null) {
                return CheckAddGroupResult.SEMESTER_NOT_SELECT;
            }
            if (quantity == null || quantity.length() < 1) {
                return CheckAddGroupResult.QUANTITY_EMPTY;
            }
            int quantity_int;
            try {
                quantity_int = Integer.parseInt(quantity);
            } catch (NumberFormatException e) {
                return CheckAddGroupResult.QUANTITY_INVALID;
            }
            if (quantity_int <= 0) {
                return CheckAddGroupResult.QUANTITY_NEGATIVE;
            }
            if (language == null) {
                return CheckAddGroupResult.LANGUAGE_NOT_SELECT;
            }
            try {
                group = new Group(degree, fieldOdStudy, formOfStudy, semester, grade, quantity_int, language);
                groupManager.create(group);
            } catch (ValidationException e) {
                return CheckAddGroupResult.GROUP_ALREADY_EXIST;
            }
            return CheckAddGroupResult.GROUP_ADD;
        }

        @Override
        protected void done() {
            CheckAddGroupResult result = null;
            try {

                result = get();
            } catch (InterruptedException e) {
                throw new AssertionError("Interrupted", e);
            } catch (ExecutionException e) {
                JOptionPane.showMessageDialog(null, "ExecutionException");
            }
            if (result == CheckAddGroupResult.GROUP_ADD) {
                groupListModel.addGroup(group);
                setVisible(false);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, I18N.getString(Objects.requireNonNull(result)));
            }
//            super.done();
        }
    }

    private void createUIComponents() {
        degreeComboBox.addItem("");
        degreeComboBox.addItem("Bakalářský");
        degreeComboBox.addItem("Magisterský");
        degreeComboBox.addItem("Doktorský");

        formOfStudyComboBox.addItem("");
        formOfStudyComboBox.addItem("Prezenční");
        formOfStudyComboBox.addItem("Kombinované");

        languageComboBox.addItem("");
        languageComboBox.addItem("Čeština");
        languageComboBox.addItem("Angličtina");

        semesterComboBox.addItem("");
        semesterComboBox.addItem("Zimní");
        semesterComboBox.addItem("Letní");

        gradeComboBox.addItem("");
        for (int i = 1; i < 6; i++) {
            gradeComboBox.addItem(String.valueOf(i));
        }

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setContentPane(addGroupPanel);
        this.pack();

    }



}
