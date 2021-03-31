package fai.utb.db.manager;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.WorkLabel;
import fai.utb.db.reader.DataReaderXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeeManagerImpl extends EmployeeManager {

    private final Document document;
    private final WorkLabelManagerImpl workLabelManager;

    public EmployeeManagerImpl(Document document) {
        this.document = document;
        workLabelManager = new WorkLabelManagerImpl(document);
    }

    @Override
    public void create(Employee employee) {
        Element root = document.getDocumentElement();
        Element employeeElement = getEmployeeNodeToXML(employee);
        root.appendChild(employeeElement);

        saveChangesToXML(document,"Employees.xml");



    }

    @Override
    public void remove(Employee employee) {
        remove(document,employee.getId(),"employee", "Employees.xml");
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

    private Element getEmployeeNodeToXML(Employee employee) {
        List<String> xmlDom = Arrays.asList(
                "name",
                "surname",
                "phone",
                "email",
                "jobtime",
                "isemployee",
                "workpoint",
                "workpoint_without_en",
                "worklabels");
        List<String> employeeItems = employee.getEmployeeItems();
        Element employeeElement = document.createElement("employee");
        employeeElement.setAttribute("id", employee.getId().toString());


        for (int index = 0; index < xmlDom.size(); index++) {
            Element newElement = document.createElement(xmlDom.get(index));

            if (index == xmlDom.size() - 1) {
                if (employee.getWorkLabels().size() > 0) {

                    for (WorkLabel workLabel : employee.getWorkLabels()) {
                        Element label = document.createElement("worklabel");
                        label.setAttribute("id", workLabel.getId().toString());
                        newElement.appendChild(label);
                    }
                    employeeElement.appendChild(newElement);
                }
            }else {
                newElement.appendChild(document.createTextNode(employeeItems.get(index)));
                employeeElement.appendChild(newElement);
            }
        }

//
//        Element newElement = document.createElement("name");
//        newElement.appendChild(document.createTextNode(employee.getName()));
//
//        employeeElement.appendChild(newElement);
//
//        newElement = document.createElement("surname");
//        newElement.appendChild(document.createTextNode(employee.getSurname()));
//
//        employeeElement.appendChild(newElement);
//
//        newElement = document.createElement("phone");
//        newElement.appendChild(document.createTextNode(employee.getPhone()));
//
//        employeeElement.appendChild(newElement);
//
//        newElement = document.createElement("email");
//        newElement.appendChild(document.createTextNode(employee.getEmail()));
//
//        employeeElement.appendChild(newElement);
//
//        newElement = document.createElement("jobtime");
//        newElement.appendChild(document.createTextNode(employee.getJobTime().toString()));
//
//        employeeElement.appendChild(newElement);
//
//        newElement = document.createElement("isemployee");
//        newElement.appendChild(document.createTextNode(employee.getIsEmployee().toString()));
//
//        employeeElement.appendChild(newElement);
//
//        newElement = document.createElement("workpoint");
//        newElement.appendChild(document.createTextNode(String.valueOf(employee.getWorkPoint())));
//
//        employeeElement.appendChild(newElement);
//
//        newElement = document.createElement("workpoint_without_en");
//        newElement.appendChild(document.createTextNode(String.valueOf(employee.getWorkPointWithoutEN())));
//
//        employeeElement.appendChild(newElement);
//
//        if (employee.getWorkLabels().size() > 0) {
//            newElement = document.createElement("worklabels");
//
//            for (WorkLabel workLabel : employee.getWorkLabels()) {
//                Element label = document.createElement("worklabel");
//                label.setAttribute("id", workLabel.getId().toString());
//                newElement.appendChild(label);
//            }
//
//            employeeElement.appendChild(newElement);
//        }
        return employeeElement;
    }


}