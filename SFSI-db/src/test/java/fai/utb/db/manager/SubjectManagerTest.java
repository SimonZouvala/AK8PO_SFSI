package fai.utb.db.manager;

import fai.utb.db.entity.Group;
import fai.utb.db.entity.Subject;
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
public class SubjectManagerTest {

    private GroupManager groupManager;
    private SubjectManager subjectManager;

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

        WorkLabelManager workLabelManager = new WorkLabelManagerImpl("xml/WorkLabels.xml");
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
                23,
                Language.CZ,
                groupManager.getAllGroup());
    }

    private Subject subjectTwo() {
        return new Subject(
                "STWO",
                "Subject TWO",
                "One ONE",
                2,
                0,
                5,
                12,
                Completion.ZK,
                10,
                Language.EN,
                groupManager.getAllGroup());
    }

    @Test
    public void testCreate() {
        subjectManager = new SubjectManagerImpl("xml/Subjects.xml");
        subjectManager.create(subjectOne());

        assertThat(subjectManager.getAllSubject().size()).isNotZero();
    }

    @Test(expected = ValidationException.class)
    public void testCreateSameSubject() {
        subjectManager = new SubjectManagerImpl("xml/Subjects.xml");
        subjectManager.create(subjectOne());
        subjectManager.create(subjectOne());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullSubject() {
        subjectManager = new SubjectManagerImpl("xml/Subjects.xml");
        subjectManager.create(null);
    }


    @Test
    public void testSetSubjectCapacity() {
        subjectManager = new SubjectManagerImpl("xml/Subjects.xml");
        subjectManager.create(subjectOne());
        List<Subject> subjectList = subjectManager.getAllSubject();
        assertThat(subjectManager.getSubject(subjectList.get(0).getId()).getClassroomCapacity()).isEqualTo(23);

        subjectManager.setSubjectCapacity(subjectList.get(0), 20);
        subjectManager = new SubjectManagerImpl("xml/Subjects.xml");
        assertThat(subjectManager.getSubject(subjectList.get(0).getId()).getClassroomCapacity()).isEqualTo(20);
    }

    @Test
    public void testRemove() {
        subjectManager = new SubjectManagerImpl("xml/Subjects.xml");
        subjectManager.create(subjectOne());
        List<Subject> subjectList = subjectManager.getAllSubject();
        subjectManager.remove(subjectList.get(0));
        assertThat(subjectManager.getAllSubject().size()).isZero();
    }

    @Test
    public void testRemoveSubjectThatNotExist() {
        subjectManager = new SubjectManagerImpl("xml/Subjects.xml");
        subjectManager.create(subjectOne());
        subjectManager.create(subjectTwo());
        List<Subject> subjectList = subjectManager.getAllSubject();
        subjectManager.remove(subjectList.get(0));
        subjectManager.remove(subjectList.get(0));
        assertThat(subjectManager.getAllSubject().size()).isEqualTo(subjectList.size() - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullSubject() {
        subjectManager = new SubjectManagerImpl("xml/Subjects.xml");
        subjectManager.remove(null);
    }

    @Test
    public void testGetAllSubject() {
        subjectManager = new SubjectManagerImpl("xml/Subjects.xml");
        assertThat(subjectManager.getAllSubject()).isEmpty();
        subjectManager.create(subjectOne());
        subjectManager.create(subjectTwo());

        assertThat(subjectManager.getAllSubject().size()).isEqualTo(2);
    }

    @Test
    public void testGetSubject() {
        subjectManager = new SubjectManagerImpl("xml/Subjects.xml");

        subjectManager.create(subjectOne());
        subjectManager.create(subjectTwo());

        List<Subject> subjectList = subjectManager.getAllSubject();

        assertThat(subjectManager.getSubject(subjectList.get(0).getId()).getName())
                .isEqualTo(subjectOne().getName());

        assertThat(subjectManager.getSubject(subjectList.get(1).getId()).getName())
                .isEqualTo(subjectTwo().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNullSubject() {
        subjectManager = new SubjectManagerImpl("xml/Subjects.xml");
        subjectManager.getSubject(null);
    }

}