package fai.utb.db.manager;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.Group;
import fai.utb.db.entity.Subject;
import fai.utb.db.entity.WorkLabel;
import fai.utb.db.entity.entityEnum.*;
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
public class WorkLabelManagerTest {


    private GroupManager groupManager;
    private SubjectManager subjectManager;
    private WorkLabelManager workLabelManager;
    private EmployeeManager employeeManager;

    @Before
    public void setUp() {
        subjectManager = new SubjectManagerImpl("xml/Subjects.xml");
        Document document = subjectManager.getData("xml/Subjects.xml");
        NodeList nodeList = document.getElementsByTagName("subjects");
        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);
            document.removeChild(node);
        }
        Element element = document.createElement("subjects");
        document.appendChild(element);

        subjectManager.saveChangesToXML(document, "xml/Subjects.xml");

        employeeManager = new EmployeeManagerImpl("xml/Employees.xml");
        document = employeeManager.getData("xml/Employees.xml");
        nodeList = document.getElementsByTagName("employees");
        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);
            document.removeChild(node);
        }
        element = document.createElement("employees");
        document.appendChild(element);

        employeeManager.saveChangesToXML(document, "xml/Employees.xml");


        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        document = workLabelManager.getData("xml/WorkLabels.xml");
        nodeList = document.getElementsByTagName("worklabels");
        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);
            document.removeChild(node);
        }
        element = document.createElement("worklabels");
        document.appendChild(element);
        workLabelManager.saveChangesToXML(document, "xml/WorkLabels.xml");

        groupManager = new GroupManagerImpl("xml/Groups.xml");
        document = groupManager.getData("xml/Groups.xml");
        nodeList = document.getElementsByTagName("groups");
        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);
            document.removeChild(node);
        }
        element = document.createElement("groups");
        document.appendChild(element);

        groupManager.saveChangesToXML(document, "xml/Groups.xml");
        groupManager = new GroupManagerImpl("xml/Groups.xml");
        groupManager.create(groupOne());
        subjectManager = new SubjectManagerImpl("xml/Subjects.xml");
        subjectManager.create(subjectOne());
        employeeManager = new EmployeeManagerImpl("xml/Employees.xml");
        employeeManager.create(employeeOne());
    }


    private Group groupOne() {
        return new Group(
                Degree.MGR,
                "ONE",
                FormOfStudy.K,
                Semester.LS,
                1,
                50,
                Language.CZ);
    }

    private Subject subjectOne() {
        return new Subject(
                "SONE",
                "Subject One",
                "One Zero",
                0,
                0,
                15,
                1,
                Completion.KL,
                10,
                Language.CZ,
                groupManager.getAllGroup());
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

    private WorkLabel workLabelOne() {
        return new WorkLabel(
                "One Exam",
                Language.CZ,
                0.5,
                10,
                null,
                Completion.ZK,
                0,
                0);
    }

    private WorkLabel workLabelTwo() {
        return new WorkLabel(
                "OneOne Seminar",
                Language.EN,
                10.0,
                20,
                LessonType.SEMINAR,
                null,
                12,
                3);
    }
    private WorkLabel workLabelThree() {
        return new WorkLabel(
                "OneOneOne Lecture",
                Language.EN,
                10.0,
                0,
                LessonType.LECTURE,
                null,
                12,
                3);
    }


    @Test
    public void testGenerateWorkLabels() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        assertThat(workLabelManager.getAllWorkLabels().size()).isZero();
        workLabelManager.generateWorkLabels();
        assertThat(workLabelManager.getAllWorkLabels().size()).isNotZero();
    }

    @Test
    public void testGenerateWorkLabelsAfterUpgradeGroupUpper() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        assertThat(workLabelManager.getAllWorkLabels().size()).isZero();
        workLabelManager.generateWorkLabels();
        int numberOfWorkLabelsBefore = workLabelManager.getAllWorkLabels().size();
        groupManager = new GroupManagerImpl("xml/Groups.xml");
        groupManager.setQuantity(groupManager.getAllGroup().get(0), 100);
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        assertThat(workLabelManager.getAllWorkLabels().size()).isGreaterThan(numberOfWorkLabelsBefore);
    }

    @Test
    public void testTestGenerateWorkLabelsAfterUpgradeSubjectLower() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        assertThat(workLabelManager.getAllWorkLabels().size()).isZero();
        workLabelManager.generateWorkLabels();
        int numberOfWorkLabelsBefore = workLabelManager.getAllWorkLabels().size();
        subjectManager = new SubjectManagerImpl("xml/Subjects.xml");
        subjectManager.setSubjectCapacity(subjectManager.getAllSubject().get(0), 5);
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        assertThat(workLabelManager.getAllWorkLabels().size()).isGreaterThan(numberOfWorkLabelsBefore);
    }

    @Test
    public void testGenerateWorkLabelsAfterUpgradeGroupLower() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        assertThat(workLabelManager.getAllWorkLabels().size()).isZero();
        workLabelManager.generateWorkLabels();
        int numberOfWorkLabelsBefore = workLabelManager.getAllWorkLabels().size();
        groupManager = new GroupManagerImpl("xml/Groups.xml");
        groupManager.setQuantity(groupManager.getAllGroup().get(0), 10);
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        assertThat(workLabelManager.getAllWorkLabels().size()).isEqualTo(numberOfWorkLabelsBefore);
    }

    @Test
    public void testTestGenerateWorkLabelsAfterUpgradeSubjectUpper() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        assertThat(workLabelManager.getAllWorkLabels().size()).isZero();
        workLabelManager.generateWorkLabels();
        int numberOfWorkLabelsBefore = workLabelManager.getAllWorkLabels().size();
        subjectManager = new SubjectManagerImpl("xml/Subjects.xml");
        subjectManager.setSubjectCapacity(subjectManager.getAllSubject().get(0), 20);
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        assertThat(workLabelManager.getAllWorkLabels().size()).isEqualTo(numberOfWorkLabelsBefore);
    }

    @Test
    public void testCreate() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        workLabelManager.create(workLabelOne());
        assertThat(workLabelManager.getAllWorkLabels().size()).isNotZero();
    }

    @Test(expected = ValidationException.class)
    public void testCreateSameWorkLabel() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        workLabelManager.create(workLabelOne());
        workLabelManager.create(workLabelOne());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullWorkLabel() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        workLabelManager.create(null);
    }

    @Test
    public void testAddEmployeeToWorkLabel() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        workLabelManager.create(workLabelOne());
        List<WorkLabel> workLabelList = workLabelManager.getAllWorkLabels();
        assertThat(workLabelList.get(0).getEmployeeId()).isNull();
        workLabelManager.addEmployeeToWorkLabel(employeeManager.getAllEmployees().get(0), workLabelList.get(0));
        assertThat(workLabelManager.getAllWorkLabels().get(0).getEmployeeId()).
                isEqualTo(employeeManager.getAllEmployees().get(0).getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullEmployeeToWorkLabel() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        workLabelManager.create(workLabelOne());
        workLabelManager.addEmployeeToWorkLabel(null, workLabelManager.getAllWorkLabels().get(0));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEmployeeToNullWorkLabel() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        workLabelManager.create(workLabelOne());
        workLabelManager.addEmployeeToWorkLabel(employeeManager.getAllEmployees().get(0), null);
    }

    @Test
    public void testRemoveEmployeeFromWorkLabel() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        workLabelManager.create(workLabelOne());
        List<WorkLabel> workLabelList = workLabelManager.getAllWorkLabels();
        assertThat(workLabelList.get(0).getEmployeeId()).isNull();
        workLabelManager.addEmployeeToWorkLabel(employeeManager.getAllEmployees().get(0), workLabelList.get(0));
        workLabelList = workLabelManager.getAllWorkLabels();
        workLabelManager.removeEmployeeFromWorkLabel(workLabelList.get(0));
        assertThat(workLabelManager.getAllWorkLabels().get(0).getEmployeeId()).
                isNull();
    }


    @Test(expected = IllegalArgumentException.class)
    public void testRemoveEmployeeToNullWorkLabel() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        workLabelManager.create(workLabelOne());
        workLabelManager.removeEmployeeFromWorkLabel(null);
    }

    @Test
    public void testRemove() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        workLabelManager.create(workLabelOne());
        List<WorkLabel> workLabelList = workLabelManager.getAllWorkLabels();
        workLabelManager.remove(workLabelList.get(0));
        assertThat(workLabelManager.getAllWorkLabels().size()).isZero();
    }

    @Test
    public void testRemoveWorkLabelThatNotExist() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        workLabelManager.create(workLabelOne());
        workLabelManager.create(workLabelTwo());
        List<WorkLabel> workLabelList = workLabelManager.getAllWorkLabels();
        workLabelManager.remove(workLabelList.get(0));
        workLabelManager.remove(workLabelList.get(0));
        assertThat(workLabelManager.getAllWorkLabels().size()).isEqualTo(workLabelList.size() - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullWorkLabel() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        workLabelManager.remove(null);
    }


    @Test
    public void testGetWorkLabel() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        workLabelManager.create(workLabelOne());
        workLabelManager.create(workLabelTwo());

        List<WorkLabel> workLabelList = workLabelManager.getAllWorkLabels();

        assertThat(workLabelManager.getWorkLabel(workLabelList.get(0).getId()).getName())
                .isEqualTo(workLabelOne().getName());

        assertThat(workLabelManager.getWorkLabel(workLabelList.get(1).getId()).getName())
                .isEqualTo(workLabelTwo().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNullWorkLabel() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        workLabelManager.getWorkLabel(null);
    }

    @Test
    public void testGetAllWorkLabels() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        assertThat(workLabelManager.getAllWorkLabels()).isEmpty();
        workLabelManager.create(workLabelOne());
        workLabelManager.create(workLabelTwo());

        assertThat(workLabelManager.getAllWorkLabels().size()).isEqualTo(2);
    }

    @Test
    public void testGetWorkLabelsWithoutEmployee() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        assertThat(workLabelManager.getAllWorkLabels()).isEmpty();
        workLabelManager.create(workLabelOne());
        workLabelManager.create(workLabelTwo());
        List<WorkLabel> workLabelList = workLabelManager.getAllWorkLabels();
        workLabelManager.addEmployeeToWorkLabel(employeeManager.getAllEmployees().get(0),
                workLabelManager.getAllWorkLabels().get(0));
        assertThat(workLabelManager.getWorkLabelsWithoutEmployee().get(0)).isEqualTo(workLabelList.get(1));
        assertThat(workLabelManager.getWorkLabelsWithoutEmployee().size()).isEqualTo(1);
    }

    @Test
    public void testGetWorkLabelsWithoutStudents() {
        workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
        assertThat(workLabelManager.getAllWorkLabels()).isEmpty();
        workLabelManager.create(workLabelOne());
        workLabelManager.create(workLabelTwo());
        workLabelManager.create(workLabelThree());
        List<WorkLabel> workLabelList = workLabelManager.getAllWorkLabels();
        workLabelManager.addEmployeeToWorkLabel(employeeManager.getAllEmployees().get(0),
                workLabelManager.getAllWorkLabels().get(0));
        assertThat(workLabelManager.getWorkLabelsWithoutStudents().size()).isEqualTo(1);
        assertThat(workLabelManager.getWorkLabelsWithoutStudents().get(0)).isEqualTo(workLabelList.get(2));
    }
}