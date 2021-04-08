package fai.utb.gui.addFormular;

import fai.utb.db.entity.Employee;
import fai.utb.db.exception.ValidationException;
import fai.utb.db.manager.EmployeeManager;
import fai.utb.gui.I18n;
import fai.utb.gui.checkers.CheckAddEmployeeResult;
import fai.utb.gui.listModel.EmployeeListModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Šimon Zouvala
 */
public class AddEmployee extends JFrame {

    private static final I18n I18N = new I18n(AddEmployee.class);

    private EmployeeManager employeeManager;
    private EmployeeListModel employeeListModel;
    private JTextField nameTextField;
    private JTextField surnameTextField;
    private JTextField phoneTextField;
    private JTextField emailTextField;
    private JComboBox<String> jobTimeComboBox;
    private JComboBox<String> isEmployeeComboBox;
    private JButton saveButton;
    private JPanel addEmployeePanel;
    private boolean isEmployee = true;
    private double jobTime = 0.0;

    public AddEmployee(EmployeeManager employeeManager, EmployeeListModel employeeListModel) {

        this.employeeManager = employeeManager;
        this.employeeListModel = employeeListModel;
        createUIComponents();
        jobTimeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = Objects.requireNonNull(jobTimeComboBox.getSelectedItem()).toString();
                if (!selectedItem.equals("")) {
                    jobTime = Double.parseDouble(selectedItem);
                } else {
                    jobTime = 0.0;
                }

            }
        });
        isEmployeeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = Objects.requireNonNull(isEmployeeComboBox.getSelectedItem()).toString();
                if (selectedItem.equals("ANO")) {
                    isEmployee = true;
                } else if (selectedItem.equals("NE")) {
                    isEmployee = false;
                }
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                String surname = surnameTextField.getText();
                String phone = phoneTextField.getText();
                String email = emailTextField.getText();
                ConfirmSwingWorker confirmSwingWorker = new ConfirmSwingWorker(name, surname, phone, email);
                confirmSwingWorker.execute();
            }
        });
    }

    private void createUIComponents() {

        isEmployeeComboBox.addItem("ANO");
        isEmployeeComboBox.addItem("NE");

        jobTimeComboBox.addItem("");
        for (int i = 1; i < 11; i++) {
            double number = (double) i / 10;
            jobTimeComboBox.addItem(String.valueOf(number));
        }

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setContentPane(addEmployeePanel);
        this.pack();
    }

    private class ConfirmSwingWorker extends SwingWorker<CheckAddEmployeeResult, CheckAddEmployeeResult> {


        private String name;
        private String surname;
        private String phone;
        private String email;
        private Employee employee = null;

        public ConfirmSwingWorker(String name, String surname, String phone, String email) {

            this.name = name;
            this.surname = surname;
            this.phone = phone;
            this.email = email;
        }

        @Override
        protected CheckAddEmployeeResult doInBackground() throws Exception {
            if (name == null || name.length() <1){
                return CheckAddEmployeeResult.NAME_EMPTY;
            }
            if (surname == null || surname.length() <1){
                return CheckAddEmployeeResult.SURNAME_EMPTY;
            }
            if (phone == null || phone.length() <1){
                return CheckAddEmployeeResult.PHONE_EMPTY;
            }
            if (email == null || email.length() <1){
                return CheckAddEmployeeResult.EMAIL_EMPTY;
            }
            if (jobTime == 0.0){
                return CheckAddEmployeeResult.JOB_TIME_NOT_SELECT;
            }if (!isPhoneValid(phone)){
                return CheckAddEmployeeResult.PHONE_INVALID;
            }
            if (!isEmailValid(email) ){
                return CheckAddEmployeeResult.EMAIL_INVALID;
            }
            try {
                employee = new Employee(name,surname, phone, email,jobTime, isEmployee);
                employeeManager.create(employee);
            }catch (ValidationException e){
                return CheckAddEmployeeResult.EMPLOYEE_ALREADY_EXIST;
            }

            return CheckAddEmployeeResult.EMPLOYEE_ADD;
        }

        @Override
        protected void done() {
            CheckAddEmployeeResult result = null;
            try {
                result = get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }if (result == CheckAddEmployeeResult.EMPLOYEE_ADD){
                employeeListModel.addEmployee(employee);
                setVisible(false);
                dispose();
            }
            else {
                JOptionPane.showMessageDialog(null, I18N.getString(Objects.requireNonNull(result)));
            }

        }
    }

    private static boolean isEmailValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean isPhoneValid(String s)
    {

        Pattern p = Pattern.compile("[+]?[0-9]?[0-9]?[0-9]?[0-9]{9}");

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

}
