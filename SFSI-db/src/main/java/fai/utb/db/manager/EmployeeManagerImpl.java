package fai.utb.db.manager;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.WorkLabel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class EmployeeManagerImpl implements EmployeeManager {

    private final Document document;
    private final WorkLabelManagerImpl workLabelManager;

    public EmployeeManagerImpl(Document document) {
        this.document = document;
        workLabelManager = new WorkLabelManagerImpl(document);
    }

    @Override
    public void create(Employee employee) {

    }

    @Override
    public void remove(Employee employee) {

    }

    @Override
    public List<Employee> getAllEmployees() {
        System.out.println("In XML are " + document.getDocumentElement().getTagName());
        NodeList nodeList = document.getElementsByTagName("employee");
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
    public Employee getEmployee(Long id) {
        System.out.println("In XML are " + document.getDocumentElement().getTagName());
        NodeList nodeList = document.getElementsByTagName("employee");

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (Long.parseLong(element.getAttribute("id")) == id) {
                    return getEmployeeFromXML(element);
                }
            }
        }


        return null;
    }


    private Employee getEmployeeFromXML(Element element) {
        return new Employee(Long.parseLong(element.getAttribute("id")),
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
        return element.getElementsByTagName("isemployee").item(0).getTextContent() == "true";
    }

    private List<WorkLabel> getWorkLabels(Element element) {
        List<WorkLabel> workLabels = new ArrayList<>();
        NodeList nodeList = element.getElementsByTagName("worklabel");

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element workLabel = (Element) node;
                workLabels.add(workLabelManager.getWorkLabel(Long.parseLong(workLabel.getAttribute("id"))));
            }
        }

        return workLabels;
    }


}