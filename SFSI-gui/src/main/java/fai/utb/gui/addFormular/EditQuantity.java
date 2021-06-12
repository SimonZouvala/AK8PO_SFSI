package fai.utb.gui.addFormular;

import fai.utb.db.exception.ValidationException;
import fai.utb.db.manager.GroupManagerImpl;
import fai.utb.db.manager.SubjectManagerImpl;
import fai.utb.gui.I18n;
import fai.utb.gui.checkers.CheckEditNumber;
import fai.utb.gui.listModel.GroupListModel;
import fai.utb.gui.listModel.SubjectListModel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Form for set number of students in group or capacity classroom in subject
 * @author Šimon Zouvala
 */
public class EditQuantity extends JDialog {

    private static final I18n I18N = new I18n(EditQuantity.class);
    private final GroupListModel groupListModel;
    private final SubjectListModel subjectListModel;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField quantityTextField;
    private JLabel label;
    private int numberToSet;
    private final int index;

    /**
     *
     * @param model relevant list model (Subject or Group list model)
     * @param index position of relevant object (subject or group) in list model
     */
    public EditQuantity(JList<String> model, int index) {
        this.index = index;

        if (model.getModel() instanceof GroupListModel) {
            this.groupListModel = (GroupListModel) model.getModel();
            numberToSet = groupListModel.getGroupsList().get(index).getQuantity();
            this.subjectListModel = null;
            label.setText(I18N.getString("NumberOfStudents"));

        } else {
            subjectListModel = (SubjectListModel) model.getModel();
            numberToSet = subjectListModel.getSubjectList().get(index).getClassroomCapacity();
            this.groupListModel = null;
            label.setText(I18N.getString("SubjectClassroomCapacity"));
        }

        quantityTextField.setText(String.valueOf(numberToSet));

        this.setContentPane(contentPane);
        this.setModal(true);
        this.getRootPane().setDefaultButton(buttonOK);
        this.pack();
        createUIComponents();
    }

    private void createUIComponents() {
        buttonCancel.addActionListener(e -> onCancel());
        buttonOK.addActionListener(e -> onOK());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private void onOK() {
        ConfirmSwingWorker confirmSwingWorker = new ConfirmSwingWorker(quantityTextField.getText());
        confirmSwingWorker.execute();
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private class ConfirmSwingWorker extends SwingWorker<CheckEditNumber, CheckEditNumber> {

        private final String quantity;

        public ConfirmSwingWorker(String quantity) {
            this.quantity = quantity;
        }

        @Override
        protected CheckEditNumber doInBackground() throws Exception {
            if (quantity == null || quantity.length() < 1) {
                return CheckEditNumber.QUANTITY_UNIVERSE_EMPTY;
            }

            int quantity_int;

            try {
                quantity_int = Integer.parseInt(quantity);
            } catch (NumberFormatException e) {
                return CheckEditNumber.QUANTITY_UNIVERSE_INVALID;
            }

            if (quantity_int <= 0) {
                return CheckEditNumber.QUANTITY_UNIVERSE_NEGATIVE;
            }

            try {
                numberToSet = quantity_int;
                if (subjectListModel == null) {
                    new GroupManagerImpl().setQuantity(groupListModel.getGroupsList().get(index),
                            numberToSet);
                } else if (groupListModel == null) {
                    new SubjectManagerImpl().setSubjectCapacity(subjectListModel.getSubjectList().get(index),
                            numberToSet);
                }
            } catch (ValidationException e) {
                return CheckEditNumber.ERROR_WITH_NUMBER;
            }

            return CheckEditNumber.OK;
        }

        @Override
        protected void done() {
            CheckEditNumber result = null;

            try {
                result = get();
            } catch (InterruptedException e) {
                throw new AssertionError("Interrupted", e);
            } catch (ExecutionException e) {
                JOptionPane.showMessageDialog(null, "ExecutionException-");
            }

            if (result != CheckEditNumber.OK) {
                JOptionPane.showMessageDialog(null, I18N.getString(Objects.requireNonNull(result)));
            }
        }
    }
}
