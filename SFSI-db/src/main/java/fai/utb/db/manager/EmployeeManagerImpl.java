package fai.utb.db.manager;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.Group;
import fai.utb.db.entity.WorkLabel;
import fai.utb.db.entity.entityEnum.Language;
import fai.utb.db.exception.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
/**
 * @author Šimon Zouvala
 */
public class EmployeeManagerImpl extends EmployeeManager {

    private final Document document;
    private static final String MAIN_ELEMENT = "employee";


    public EmployeeManagerImpl() {
        this.document = getEmployeesDocument();

    }

    private Document getEmployeesDocument() {
        return getData(EMPLOYEES_XML);
    }

    @Override
    public void create(Employee employee) {
        validate(employee);
        Element employeeElement = getItemToXML(
                document,
                getEmployeeXmlDomList(),
                employee.getEmployeeItems(),
                MAIN_ELEMENT);
        create(document, employeeElement, EMPLOYEES_XML);
    }

    private void validate(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("group is null");
        }if (employee.getName() == null) {
            throw new ValidationException("Name is not set");
        }if (employee.getSurname() == null) {
            throw new ValidationException("Surname is not set");
        }
        if (employee.getPhone() == null) {
            throw new ValidationException("Phone is not set");
        }
        if (employee.getEmail() == null) {
            throw new ValidationException("Email is not set");
        }
        if (employee.getJobTime() <= 0.0 || employee.getJobTime() ==null) {
            throw new ValidationException("JobTime  is not set or is negative or zero");
        }
        if (employee.getIsEmployee() == null) {
            throw new ValidationException("Is Employee is not set");
        }
        if (isSameEmployeeInXml(employee)){
            throw new ValidationException(
                    "Employee "+ employee.getName() +" "+employee.getSurname() +" with email "+ employee.getEmail() +
                            " and phone number "+employee.getPhone() + "is already exist");

        }
    }

    private boolean isSameEmployeeInXml(Employee newEmployee) {
        List<Employee> employees = getAllEmployees();

        for (Employee oldEmployee: employees){
            if (oldEmployee.equals(newEmployee)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void remove(Employee employee) {
        remove(document, employee.getId(), MAIN_ELEMENT, EMPLOYEES_XML);
    }

    @Override
    public List<Employee> getAllEmployees() {
//        System.out.println("In XML are " + document.getDocumentElement().getTagName());
        NodeList nodeList = document.getElementsByTagName(MAIN_ELEMENT);
        List<Employee> employees = new ArrayList<>();

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;

                employees.add(getEmployeeFromXML(element));
            }
        }

        return employees;
    }

    @Override
    public Employee getEmployee(UUID id) {
//        System.out.println("In XML are " + document.getDocumentElement().getTagName());
        NodeList nodeList = document.getElementsByTagName(MAIN_ELEMENT);

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (UUID.fromString(element.getAttribute("id")) == id) {
                    return getEmployeeFromXML(element);
                }
            }
        }


        return null;
    }


    private Employee getEmployeeFromXML(Element element) {
        return new Employee(
                UUID.fromString(element.getAttribute("id")),
                element.getElementsByTagName("name").item(0).getTextContent(),
                element.getElementsByTagName("surname").item(0).getTextContent(),
                element.getElementsByTagName("phone").item(0).getTextContent(),
                element.getElementsByTagName("email").item(0).getTextContent(),
                Double.parseDouble(element.getElementsByTagName("jobtime").item(0).getTextContent()),
                getIsEmployee(element),
                Double.parseDouble(element.getElementsByTagName("workpoint").item(0).getTextContent()),
                Double.parseDouble(element.getElementsByTagName("workpoint_without_en").item(0).getTextContent()),
                getWorkLabels((Element) element.getElementsByTagName("worklabels").item(0)));

    }

    private boolean getIsEmployee(Element element) {
        return element.getElementsByTagName("isemployee").item(0).getTextContent().equals("true");
    }

    private List<WorkLabel> getWorkLabels(Element element) {
        List<WorkLabel> workLabels = new ArrayList<>();
        NodeList nodeList = element.getElementsByTagName("worklabel");

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element workLabel = (Element) node;
                workLabels.add(
                        new WorkLabelManagerImpl().getWorkLabel(UUID.fromString(workLabel.getAttribute("id"))));
            }
        }

        return workLabels;
    }

    private List<WorkLabel> getWorkLabels(Employee employee) {
        return new WorkLabelManagerImpl()
                .getAllWorkLabels()
                .stream()
                .filter(workLabel -> employee.getId().equals(workLabel.getEmployeeId()))
                .toList();
    }

    @Override
    public void setWorkPoints(Employee employee) {
        setElement(
                document,
                employee.getId(),
                "workpoint",
                String.valueOf(getNumberOfWorkPoint(employee)),
                EMPLOYEES_XML,
                MAIN_ELEMENT);
        setElement(
                document,
                employee.getId(),
                "workpoint_without_en",
                String.valueOf(getNumberOfWorkPointWithoutEn(employee)),
                EMPLOYEES_XML,
                MAIN_ELEMENT);

    }

    private double getNumberOfWorkPoint(Employee employee) {
        return calculateWorkPoints(getWorkLabels(employee));
    }

    private double calculateWorkPoints(List<WorkLabel> workLabels) {
        double workPoints = 0.0;

        for (WorkLabel workLabel : workLabels) {
            workPoints += workLabel.getPoints();
        }
        return workPoints;
    }


    private double getNumberOfWorkPointWithoutEn(Employee employee) {
        return calculateWorkPoints(getWorkLabels(employee)
                .stream()
                .filter(workLabel -> Language.CZ.equals(workLabel.getLanguage()))
                .toList());
    }

    private List<String> getEmployeeXmlDomList() {
        return Arrays.asList(
                "name",
                "surname",
                "phone",
                "email",
                "jobtime",
                "isemployee",
                "workpoint",
                "workpoint_without_en",
                "worklabels");

    }
}