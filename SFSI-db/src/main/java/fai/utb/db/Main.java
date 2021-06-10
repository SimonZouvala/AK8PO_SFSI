package fai.utb.db;

import fai.utb.db.entity.Employee;
import fai.utb.db.entity.Group;
import fai.utb.db.entity.Subject;
import fai.utb.db.entity.WorkLabel;
import fai.utb.db.entity.entityEnum.*;
import fai.utb.db.manager.*;

import java.util.ArrayList;
import java.util.List;


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

                Degree.MGR,
                "SWI",
                FormOfStudy.K,
                Semester.LS,
                1,
                50,
                Language.CZ);

        Group g2 = new Group(
                Degree.MGR,
                "SWI",
                FormOfStudy.P,
                Semester.LS,
                1,
                43,
                Language.CZ);

        groupManager.create(g1);

        groupManager.create(g2);

        List<Group> groupList = new ArrayList<>();
        groupList.add(g1);
        groupList.add(g2);

        Subject s1 = new Subject(
                "NECO",
                "Test předmět",
                "Vařacha Pavel",
                0,
                0,
                15,
                1,
                Completion.KL,
                23,
                Language.CZ,
                groupManager.getAllGroup());

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
            workLabelManager.addEmployeeToWorkLabel(employeeManager.getAllEmployees().get(employeeManager.getAllEmployees().size()-1), workLabel);
        }
        System.out.println(workLabelManager.getAllWorkLabels());
        System.out.println(employeeManager.getAllEmployees());
//        employeeManager.setWorkPoints(e1);
//
//        for (WorkLabel workLabel : workLabelManager.getAllWorkLabels()) {
//            workLabelManager.remove(workLabel);
//        }

//        subjectManager.remove(subjectManager.getAllSubject().get(subjectManager.getAllSubject().size()-1));
//        groupManager.remove(groupManager.getAllGroup().get(groupManager.getAllGroup().size()-1));
//        groupManager.remove(groupManager.getAllGroup().get(groupManager.getAllGroup().size()-1));
//        employeeManager.remove(employeeManager.getAllEmployees().get(employeeManager.getAllEmployees().size()-1));

        subjectManager.setSubjectCapacity(subjectManager.getAllSubject().get(subjectManager.getAllSubject().size()-1),33);
//        groupManager.setQuantity(groupManager.getGroup(1L),111);
    }
}