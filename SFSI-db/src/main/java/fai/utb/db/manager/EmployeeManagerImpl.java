package fai.utb.db.manager;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.WorkLabel;
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
 * Implementation of abstract class {@link EmployeeManager}
 *
 * @author Šimon Zouvala
 */
public class EmployeeManagerImpl extends EmployeeManager {

    private final Document document;
    private static final String MAIN_ELEMENT = "employee";

    /**
     * Constructor for get current DOM of Employees.xml.
     */
    public EmployeeManagerImpl() {
        this.document = getEmployeesDocument();
    }

    /**
     * Constructor for get current DOM of relevant XML file.
     */
    public EmployeeManagerImpl(String xml) {
        this.document = getData(xml);
    }

    private Document getEmployeesDocument() {
        return getData(EMPLOYEES_XML);
    }

    @Override
    public void create(Employee employee) {
        validate(employee);
        if (isSameEmployeeInXml(employee)) {
            throw new ValidationException(
                    "Employee " + employee.getName() + " " + employee.getSurname() + " with email "
                            + employee.getEmail() + " and phone number " + employee.getPhone() + "is already exist");
        }

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
        }
        if (employee.getName() == null) {
            throw new ValidationException("Name is not set");
        }
        if (employee.getSurname() == null) {
            throw new ValidationException("Surname is not set");
        }
        if (employee.getPhone() == null) {
            throw new ValidationException("Phone is not set");
        }
        if (employee.getEmail() == null) {
            throw new ValidationException("Email is not set");
        }
        if (employee.getJobTime() <= 0.0 || employee.getJobTime() == null) {
            throw new ValidationException("JobTime  is not set or is negative or zero");
        }
        if (employee.getIsEmployee() == null) {
            throw new ValidationException("Is Employee is not set");
        }

    }

    private boolean isSameEmployeeInXml(Employee newEmployee) {
        List<Employee> employees = getAllEmployees();

        for (Employee oldEmployee : employees) {
            if (oldEmployee.equals(newEmployee)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void remove(Employee employee) {
        validate(employee);
        remove(document, employee.getId(), MAIN_ELEMENT, EMPLOYEES_XML);
    }

    @Override
    public List<Employee> getAllEmployees() {
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
        if (id == null) {
            throw new IllegalArgumentException("Employee id is null");
        }
        NodeList nodeList = document.getElementsByTagName(MAIN_ELEMENT);

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (UUID.fromString(element.getAttribute("id")).equals(id)) {
                    return getEmployeeFromXML(element);
                }
            }
        }
        throw new IllegalArgumentException("Employee not exist");
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

    /**
     * Add {@link WorkLabel} to required {@link Employee}, set work points and all save to xml file.
     *
     * @param employee  where will be save {@link WorkLabel}
     * @param workLabel save to {@link Employee}
     */
    public void addWorkLabelToEmployee(Employee employee, WorkLabel workLabel) {
        validate(employee);

        NodeList nodeList = document.getElementsByTagName(MAIN_ELEMENT);

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                if (UUID.fromString(element.getAttribute("id")).equals(employee.getId())) {
                    Element workLabelsElement = (Element) element.getElementsByTagName("worklabels").item(0);
                    Element newElement = document.createElement("worklabel");
                    newElement.setAttribute("id", workLabel.getId().toString());
                    workLabelsElement.appendChild(newElement);
                    break;
                }
            }
        }
        document.getDocumentElement().normalize();
        saveChangesToXML(document, EMPLOYEES_XML);
        setWorkPoints(getEmployee(employee.getId()));
    }

    /**
     * Remove required {@link WorkLabel} from {@Employee}, set work points and save all changes to xml file
     *
     * @param employee  where will be remove {@link WorkLabel}
     * @param workLabel remove from {@link Employee}
     */
    public void removeWorkLabelToEmployee(Employee employee, WorkLabel workLabel) {
        NodeList nodeList = document.getElementsByTagName(MAIN_ELEMENT);

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                if (UUID.fromString(element.getAttribute("id")).equals(employee.getId())) {
                    Node workLabels = element.getElementsByTagName("worklabels").item(0);
                    NodeList nodeListWorkLabels = element.getElementsByTagName("worklabel");
                    for (int i = 0; i < nodeListWorkLabels.getLength(); i++) {
                        Node workLabelNode = nodeListWorkLabels.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element workLabelElement = (Element) workLabelNode;
                            if (UUID.fromString(workLabelElement.getAttribute("id")).equals(workLabel.getId())) {
                                workLabels.removeChild(workLabelElement);
                                break;
                            }
                        }
                    }
                }
            }
            document.getDocumentElement().normalize();
            saveChangesToXML(document, EMPLOYEES_XML);
            setWorkPoints(getEmployee(employee.getId()));
        }
    }


    private void setWorkPoints(Employee employee) {
        setElement(
                document,
                employee.getId(),
                "workpoint",
                String.valueOf(employee.getWorkPoint()),
                EMPLOYEES_XML,
                MAIN_ELEMENT);

        setElement(
                document,
                employee.getId(),
                "workpoint_without_en",
                String.valueOf(employee.getWorkPointWithoutEN()),
                EMPLOYEES_XML,
                MAIN_ELEMENT);

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