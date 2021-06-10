package fai.utb.db.manager;

import fai.utb.db.entity.Group;
import fai.utb.db.entity.entityEnum.Degree;
import fai.utb.db.entity.entityEnum.FormOfStudy;
import fai.utb.db.entity.entityEnum.Language;
import fai.utb.db.entity.entityEnum.Semester;
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
public class GroupManagerTest {

    private GroupManager groupManager;

    @Before
    public void setUp() {
        groupManager = new GroupManagerImpl("xml/Groups.xml");
        Document document = groupManager.getData("xml/Groups.xml");
        NodeList nodeList = document.getElementsByTagName("groups");
        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);
            document.removeChild(node);
        }
        Element element = document.createElement("groups");
        document.appendChild(element);

        groupManager.saveChangesToXML(document, "xml/Groups.xml");
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

    private Group groupTwo() {
        return new Group(
                Degree.MGR,
                "TWO",
                FormOfStudy.P,
                Semester.LS,
                2,
                10,
                Language.CZ);
    }

    @Test
    public void testCreate() {
        groupManager = new GroupManagerImpl("xml/Groups.xml");
        groupManager.create(groupOne());

        assertThat(groupManager.getAllGroup().size()).isNotZero();
    }

    @Test(expected = ValidationException.class)
    public void testCreateSameGroup() {
        groupManager = new GroupManagerImpl("xml/Groups.xml");
        groupManager.create(groupOne());
        groupManager.create(groupOne());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullGroup() {
        groupManager = new GroupManagerImpl("xml/Groups.xml");
        groupManager.create(null);
    }

    @Test
    public void testRemove() {
        groupManager = new GroupManagerImpl("xml/Groups.xml");
        groupManager.create(groupOne());
        List<Group> groupList = groupManager.getAllGroup();
        groupManager.remove(groupList.get(0));
        assertThat(groupManager.getAllGroup().size()).isZero();
    }

    @Test
    public void testRemoveGroupThatNotExist() {
        groupManager = new GroupManagerImpl("xml/Groups.xml");
        groupManager.create(groupOne());
        groupManager.create(groupTwo());
        List<Group> groupList = groupManager.getAllGroup();
        groupManager.remove(groupList.get(0));
        groupManager.remove(groupList.get(0));
        assertThat(groupManager.getAllGroup().size()).isEqualTo(groupList.size() - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullGroup() {
        groupManager = new GroupManagerImpl("xml/Groups.xml");
        groupManager.remove(null);
    }


    @Test
    public void testSetQuantity() {
        groupManager = new GroupManagerImpl("xml/Groups.xml");
        groupManager.create(groupOne());
        List<Group> groupList = groupManager.getAllGroup();
        assertThat(groupManager.getGroup(groupList.get(0).getId()).getQuantity()).isEqualTo(50);

        groupManager.setQuantity(groupList.get(0), 20);
        groupManager = new GroupManagerImpl("xml/Groups.xml");
        assertThat(groupManager.getGroup(groupList.get(0).getId()).getQuantity()).isEqualTo(20);
    }

    @Test(expected = ValidationException.class)
    public void testSetQuantityWithNegativeNumber() {
        groupManager = new GroupManagerImpl("xml/Groups.xml");
        groupManager.create(groupOne());
        List<Group> groupList = groupManager.getAllGroup();
        groupManager.setQuantity(groupList.get(0), -20);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetQuantityWithNullGroup() {
        groupManager = new GroupManagerImpl("xml/Groups.xml");
        groupManager.create(groupOne());
        groupManager.setQuantity(null, 20);
    }

    @Test
    public void testGetAllGroup() {
        groupManager = new GroupManagerImpl("xml/Groups.xml");
        assertThat(groupManager.getAllGroup()).isEmpty();
        groupManager.create(groupOne());
        groupManager.create(groupTwo());

        assertThat(groupManager.getAllGroup().size()).isEqualTo(2);
    }

    @Test
    public void testGetGroup() {
        groupManager = new GroupManagerImpl("xml/Groups.xml");

        groupManager.create(groupOne());
        groupManager.create(groupTwo());

        List<Group> groupList = groupManager.getAllGroup();

        assertThat(groupManager.getGroup(groupList.get(0).getId()).getFieldOfStudy())
                .isEqualTo(groupOne().getFieldOfStudy());

        assertThat(groupManager.getGroup(groupList.get(1).getId()).getFieldOfStudy())
                .isEqualTo(groupTwo().getFieldOfStudy());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNullGroup() {
        groupManager = new GroupManagerImpl("xml/Groups.xml");
        groupManager.getGroup(null);
    }
}