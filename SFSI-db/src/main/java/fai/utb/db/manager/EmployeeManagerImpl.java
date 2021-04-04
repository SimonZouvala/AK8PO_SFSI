package fai.utb.db.manager;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.WorkLabel;
import fai.utb.db.entity.entityEnum.Language;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class EmployeeManagerImpl extends EmployeeManager {

    private final Document document;
    private static final String MAIN_ELEMENT = "employee";


    public EmployeeManagerImpl() {
        this.document = getEmployeesDocument();

    }

    @Override
    public void create(Employee employee) {
        Element employeeElement = getItemToXML(
                document,
                getEmployeeXmlDomList(),
                employee.getEmployeeItems(),
                employee.getId(),
                MAIN_ELEMENT);
        create(document, employeeElement, EMPLOYEES_XML);
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
                Integer.parseInt(element.getElementsByTagName("workpoint").item(0).getTextContent()),
                Integer.parseInt(element.getElementsByTagName("workpoint_without_en").item(0).getTextContent()),
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