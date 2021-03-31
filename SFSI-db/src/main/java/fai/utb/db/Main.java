package fai.utb.db;

import fai.utb.db.entity.Group;
import fai.utb.db.entity.entityEnum.Degree;
import fai.utb.db.entity.entityEnum.FormOfStudy;
import fai.utb.db.entity.entityEnum.Language;
import fai.utb.db.entity.entityEnum.Semester;
import fai.utb.db.manager.EmployeeManager;
import fai.utb.db.manager.EmployeeManagerImpl;
import fai.utb.db.manager.GroupManager;
import fai.utb.db.manager.GroupManagerImpl;
import fai.utb.db.reader.DataReader;
import fai.utb.db.reader.DataReaderXML;
import org.w3c.dom.Document;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println("Hello world");

        Document documentEmployees = new DataReaderXML("Employees.xml").getData();

        EmployeeManager employeeManager = new EmployeeManagerImpl(documentEmployees);

        System.out.println(employeeManager.getAllEmployees().get(0));
        System.out.println(employeeManager.getEmployee(1L));


        Document documentGroups = new DataReaderXML("Groups.xml").getData();

        GroupManager groupManager = new GroupManagerImpl(documentGroups);

        System.out.println(groupManager.getAllGroup());
        groupManager.create(new Group(3L, Degree.BC, "kyb", FormOfStudy.P, Semester.ZS,2, 20, Language.CZ));
        System.out.println(groupManager.getAllGroup());
        groupManager.remove(new Group(3L, Degree.BC, "kyb", FormOfStudy.P, Semester.ZS,2, 20, Language.CZ));

    }
}
