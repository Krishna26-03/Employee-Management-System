package Service;

import dao.EmployeeDAO;
import Model.Employee;

import java.util.*;

public class EmployeeService {
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    // Register new employee
    public boolean registerEmployee(Employee emp) {
        if (emp.getEmail() == null || emp.getEmail().isEmpty()) {
            System.out.println("Email is required.");
            return false;
        }
        return employeeDAO.addEmployee(emp);
    }

    //Get by ID
    public Employee getEmployee(int id) {
        return employeeDAO.getEmployeeById(id);
    }

    //All employees
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }

    //Update
    public boolean updateEmployee(Employee emp) {
        return employeeDAO.updateEmployee(emp);
    }

    //Soft delete
    public boolean removeEmployee(int id) {
        return employeeDAO.deactivateEmployee(id);
    }

    //Assign department
    public boolean assignDepartment(int employeeId, int departmentId) {
        Employee emp = employeeDAO.getEmployeeById(employeeId);
        if (emp == null) { System.out.println("Employee not found."); return false; }
        emp.setDepartmentId(departmentId);
        return employeeDAO.updateEmployee(emp);
    }

    //Update salary
    public boolean updateSalary(int employeeId, double newSalary) {
        if (newSalary <= 0) { System.out.println("Invalid salary."); return false; }
        Employee emp = employeeDAO.getEmployeeById(employeeId);
        if (emp == null) return false;
        emp.setSalary(newSalary);
        return employeeDAO.updateEmployee(emp);
    }

    //Search by name
    public List<Employee> searchByName(String keyword) {
        return employeeDAO.searchEmployeesByName(keyword);
    }

    //Filter by salary range
    public List<Employee> filterBySalary(double min, double max) {
        return employeeDAO.filterBySalaryRange(min, max);
    }

    //Department-wise report using ArrayList + HashMap
    public Map<String, List<Employee>> getDepartmentWiseReport() {
        List<Employee> all = employeeDAO.getAllEmployees();
        Map<String, List<Employee>> report = new HashMap<>();

        for (Employee emp : all) {
            String dept = emp.getDepartmentName() != null ? emp.getDepartmentName() : "Unassigned";
            report.computeIfAbsent(dept, k -> new ArrayList<>()).add(emp);
        }
        return report;
    }

    //Salary summary per department
    public void printSalarySummary() {
        Map<String, List<Employee>> deptMap = getDepartmentWiseReport();
        System.out.println("\n======= SALARY SUMMARY BY DEPARTMENT =======");
        for (Map.Entry<String, List<Employee>> entry : deptMap.entrySet()) {
            List<Employee> emps = entry.getValue();
            double total   = emps.stream().mapToDouble(Employee::getSalary).sum();
            double average = emps.stream().mapToDouble(Employee::getSalary).average().orElse(0);
            System.out.printf("%-20s | Headcount: %2d | Total: INR %10.2f | Avg: INR %8.2f%n",
                    entry.getKey(), emps.size(), total, average);
        }
        System.out.println("=============================================\n");
    }

    //HashMap lookup map (employeeId → Employee)
    public Map<Integer, Employee> getEmployeeLookupMap() {
        return employeeDAO.getEmployeeLookupMap();
    }

    //Sort employees by salary (descending)
    public List<Employee> getEmployeesSortedBySalary() {
        List<Employee> list = new ArrayList<>(employeeDAO.getAllEmployees());
        list.sort((a, b) -> Double.compare(b.getSalary(), a.getSalary()));
        return list;
    }
}
