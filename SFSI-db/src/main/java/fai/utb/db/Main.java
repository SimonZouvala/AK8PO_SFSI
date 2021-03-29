package fai.utb.db;

import fai.utb.db.manager.EmployeeManager;
import fai.utb.db.manager.EmployeeManagerImpl;
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


    }
}
