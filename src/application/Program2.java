package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {

		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		Scanner scan = new Scanner(System.in);

		System.out.println("--- TEST 1: department findById ---");
		Department department = departmentDao.findById(2);
		System.out.println(department);


		List<Department> list = new ArrayList<>();
		
		System.out.println("\n--- TEST 2: department findAll ---");
		list = departmentDao.findAll();
		for (Department dep : list) {
			System.out.println(dep);
		}

		System.out.println("\n--- TEST 3: department insert ---");
		Department newDepartment = new Department(null, "Games");
		departmentDao.insert(newDepartment);
		System.out.println("Inserted! New id = " + newDepartment.getId());
		
		
		System.out.println("\n--- TEST 4: department update ---");
		department = departmentDao.findById(1);
		department.setName("Music");
		departmentDao.update(department);
		System.out.println("Update succeeded");
		
		
		System.out.println("\n--- TEST 5: department delete ---");
		System.out.println("Type in an id for deletion: ");
		int id = scan.nextInt();
		departmentDao.deleteById(id);
		System.out.println("Deletion completed");
		
		scan.close();
		
	}

}
