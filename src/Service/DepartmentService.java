package Service;

import dao.DepartmentDAO;
import Model.Department;

import java.util.List;

public class DepartmentService {
    private final DepartmentDAO departmentDAO = new DepartmentDAO();

    public boolean addDepartment(String name, String location) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Department name cannot be empty.");
            return false;
        }
        return departmentDAO.addDepartment(new Department(0, name.trim(), location));
    }

    public List<Department> getAllDepartments() {
        return departmentDAO.getAllDepartments();
    }

    public Department getDepartment(int id) {
        return departmentDAO.getDepartmentById(id);
    }

    public boolean updateDepartment(Department dept) {
        return departmentDAO.updateDepartment(dept);
    }

    public boolean removeDepartment(int id) {
        return departmentDAO.deleteDepartment(id);
    }

    public void printAllDepartments() {
        List<Department> list = getAllDepartments();
        System.out.println("\n======= DEPARTMENTS =======");
        list.forEach(d -> System.out.printf("ID: %d | %-20s | %s%n",
                d.getDepartmentId(), d.getDepartmentName(), d.getLocation()));
        System.out.println("===========================\n");
    }
}
