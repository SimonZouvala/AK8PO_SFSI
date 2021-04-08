package fai.utb.db;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.Group;
import fai.utb.db.entity.Subject;
import fai.utb.db.entity.WorkLabel;
import fai.utb.db.entity.entityEnum.*;
import fai.utb.db.manager.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Šimon Zouvala
 */
public class Main {

    public static void main(String[] args) {
        // write your code here
        System.out.println("Hello world");

        System.out.println();

        EmployeeManager employeeManager = new EmployeeManagerImpl();
        WorkLabelManager workLabelManager = new WorkLabelManagerImpl();
        GroupManager groupManager = new GroupManagerImpl();
        SubjectManager subjectManager = new SubjectManagerImpl();

        Group g1 = new Group(
                UUID.randomUUID(),
                Degree.MGR,
                "SWI",
                FormOfStudy.K,
                Semester.LS,
                1,
                50,
                Language.CZ);

        Group g2 = new Group(
                UUID.randomUUID(),
                Degree.MGR,
                "SWI",
                FormOfStudy.P,
                Semester.LS,
                1,
                43,
                Language.CZ);

        groupManager.create(g1);

        groupManager.create(g2);

        List<Group> groupList = new ArrayList<>(Arrays.asList(g1, g2));

        Subject s1 = new Subject(
                UUID.randomUUID(),
                "Pokročilé programování",
                "AK8PO",
                "Vařacha Pavel",
                0,
                0,
                15,
                23,
                1,
                Completion.KL,
                Language.CZ,
                groupList);

        subjectManager.create(s1);

        Employee e1 = new Employee(
                "Pavel",
                "Vařacha",
                "+420 576 035 186",
                "varacha@utb.cz",
                1.0,
                true);

        employeeManager.create(e1);

        System.out.println(employeeManager.getAllEmployees());
//        System.out.println(employeeManager.getEmployee());

        System.out.println(groupManager.getAllGroup());
//        groupManager.create(new Group(3L, Degree.BC, "kyb", FormOfStudy.P, Semester.ZS,2, 20, Language.CZ));

//        System.out.println(groupManager.getAllGroup());
//        groupManager.remove(new Group(3L, Degree.BC, "kyb", FormOfStudy.P, Semester.ZS,2, 20, Language.CZ));
        System.out.println(subjectManager.getAllSubject());

        System.out.println(workLabelManager.getAllWorkLabels());
        workLabelManager.generateWorkLabels();
//        workLabelManager.addEmployeeToWorkLabel(employeeManager.getEmployee(1L),workLabelManager.getWorkLabel(1L));
        System.out.println(workLabelManager.getAllWorkLabels());
        for (WorkLabel workLabel : workLabelManager.getAllWorkLabels()) {
            workLabelManager.addEmployeeToWorkLabel(employeeManager.getAllEmployees().get(0), workLabel);
        }

        employeeManager.setWorkPoints(e1);

        for (WorkLabel workLabel : workLabelManager.getAllWorkLabels()) {
            workLabelManager.removeWorkLabels(workLabel);
        }

        subjectManager.remove(s1);
        groupManager.remove(g1);
        groupManager.remove(g2);
        employeeManager.remove(e1);

//        subjectManager.setSubjectCapacity(subjectManager.getSubject(1L),33);
//        groupManager.setQuantity(groupManager.getGroup(1L),111);
    }
}
