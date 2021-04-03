package fai.utb.db;

import fai.utb.db.entity.Group;
import fai.utb.db.entity.entityEnum.Degree;
import fai.utb.db.entity.entityEnum.FormOfStudy;
import fai.utb.db.entity.entityEnum.Language;
import fai.utb.db.entity.entityEnum.Semester;
import fai.utb.db.manager.*;
import fai.utb.db.reader.DataReader;
import fai.utb.db.reader.DataReaderXML;
import org.w3c.dom.Document;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println("Hello world");

        System.out.println();

        EmployeeManager employeeManager = new EmployeeManagerImpl();

        System.out.println(employeeManager.getAllEmployees().get(0));
        System.out.println(employeeManager.getEmployee(1L));




        GroupManager groupManager = new GroupManagerImpl();

        System.out.println(groupManager.getAllGroup());
        groupManager.create(new Group(3L, Degree.BC, "kyb", FormOfStudy.P, Semester.ZS,2, 20, Language.CZ));
        System.out.println(groupManager.getGroup(3L));
        groupManager.setQuantity(groupManager.getGroup(3L),10);
        System.out.println(groupManager.getGroup(3L));
        System.out.println(groupManager.getAllGroup());
        groupManager.remove(new Group(3L, Degree.BC, "kyb", FormOfStudy.P, Semester.ZS,2, 20, Language.CZ));



        SubjectManager subjectManager = new SubjectManagerImpl();

        System.out.println(subjectManager.getAllSubject());


    }
}
