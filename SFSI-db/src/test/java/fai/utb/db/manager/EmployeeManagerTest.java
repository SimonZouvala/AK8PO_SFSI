package fai.utb.db.manager;

import fai.utb.db.entity.Employee;
import fai.utb.db.exception.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Šimon Zouvala
 */
public class EmployeeManagerTest {


    private EmployeeManager employeeManager;


    @Before
    public void setUp() {
        employeeManager = new EmployeeManagerImpl("xml/Employees.xml");
        Document document = employeeManager.getData("xml/Employees.xml");
        NodeList nodeList = document.getElementsByTagName("employees");
        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);
            document.removeChild(node);
        }
        Element element = document.createElement("employees");
        document.appendChild(element);

        employeeManager.saveChangesToXML(document, "xml/Employees.xml");
    }


    private Employee employeeOne() {
        return new Employee(
                "Zero",
                "One",
                "+000 000 000 001",
                "one@utb.cz",
                0.1,
                true);
    }

    private Employee employeeTwo() {
        return new Employee(
                "One",
                "One",
                "+000 000 000 011",
                "oneone@utb.cz",
                1.0,
                true);
    }


    @Test
    public void testCreate() {
        employeeManager = new EmployeeManagerImpl("xml/Employees.xml");
        employeeManager.create(employeeOne());

        assertThat(employeeManager.getAllEmployees().size()).isNotZero();
    }

    @Test(expected = ValidationException.class)
    public void testCreateSameEmployee() {
        employeeManager = new EmployeeManagerImpl("xml/Employees.xml");
        employeeManager.create(employeeOne());
        employeeManager.create(employeeOne());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullEmployee() {
        employeeManager = new EmployeeManagerImpl("xml/Employees.xml");
        employeeManager.create(null);
    }

    @Test
    public void testRemove() {
        employeeManager = new EmployeeManagerImpl("xml/Employees.xml");
        employeeManager.create(employeeOne());
        List<Employee> employeeList = employeeManager.getAllEmployees();
        employeeManager.remove(employeeList.get(0));
        assertThat(employeeManager.getAllEmployees().size()).isZero();
    }

    @Test
    public void testRemoveEmployeeThatNotExist() {
        employeeManager = new EmployeeManagerImpl("xml/Employees.xml");
        employeeManager.create(employeeOne());
        employeeManager.create(employeeTwo());
        List<Employee> employeeList = employeeManager.getAllEmployees();
        employeeManager.remove(employeeList.get(0));
        employeeManager.remove(employeeList.get(0));
        assertThat(employeeManager.getAllEmployees().size()).isEqualTo(employeeList.size() - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullEmployee() {
        employeeManager = new EmployeeManagerImpl("xml/Employees.xml");
        employeeManager.remove(null);
    }


    @Test
    public void testGetAllEmployees() {
        employeeManager = new EmployeeManagerImpl("xml/Employees.xml");
        assertThat(employeeManager.getAllEmployees()).isEmpty();
        employeeManager.create(employeeOne());
        employeeManager.create(employeeTwo());

        assertThat(employeeManager.getAllEmployees().size()).isEqualTo(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNullEmployee() {
        employeeManager = new EmployeeManagerImpl("xml/Employees.xml");
        employeeManager.getEmployee(null);
    }

    @Test
    public void testGetEmployee() {
        employeeManager = new EmployeeManagerImpl("xml/Employees.xml");

        employeeManager.create(employeeOne());
        employeeManager.create(employeeTwo());

        List<Employee> employeeList = employeeManager.getAllEmployees();

        assertThat(employeeManager.getEmployee(employeeList.get(0).getId()).getName())
                .isEqualTo(employeeOne().getName());

        assertThat(employeeManager.getEmployee(employeeList.get(1).getId()).getName())
                .isEqualTo(employeeTwo().getName());
    }
}