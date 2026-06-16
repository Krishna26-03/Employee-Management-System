package Main;

import Model.Employee;
import Service.DepartmentService;
import Service.EmployeeService;
import Service.LeaveService;
import Util.DbConnection;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner          sc      = new Scanner(System.in);
    private static final EmployeeService  empSvc  = new EmployeeService();
    private static final DepartmentService deptSvc = new DepartmentService();
    private static final LeaveService     leaveSvc = new LeaveService();

    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("      Employee Management System      ");
        System.out.println("======================================");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1  -> employeeMenu();
                case 2  -> departmentMenu();
                case 3  -> leaveMenu();
                case 4  -> reportsMenu();
                case 0  -> running = false;
                default -> System.out.println("Invalid option. Try again.");
            }
        }

        DbConnection.closeConnection();
        System.out.println("Goodbye!");
    }

    // ─────────────────────────────────────────────────────────────
    //  EMPLOYEE MENU
    // ─────────────────────────────────────────────────────────────
    private static void employeeMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Employee Management ---");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Search Employee by Name");
            System.out.println("4. Update Employee Salary");
            System.out.println("5. Assign Department");
            System.out.println("6. Deactivate Employee");
            System.out.println("7. Filter by Salary Range");
            System.out.println("0. Back");
            int ch = readInt("Choice: ");
            switch (ch) {
                case 1 -> addEmployee();
                case 2 -> listAllEmployees();
                case 3 -> searchEmployee();
                case 4 -> updateSalary();
                case 5 -> assignDept();
                case 6 -> deactivateEmployee();
                case 7 -> filterBySalary();
                case 0 -> back = true;
                default -> System.out.println("Invalid.");
            }
        }
    }

    private static void addEmployee() {
        System.out.println("\n-- Add New Employee --");
        System.out.print("First Name   : "); String fn = sc.nextLine().trim();
        System.out.print("Last Name    : "); String ln = sc.nextLine().trim();
        System.out.print("Email        : "); String em = sc.nextLine().trim();
        System.out.print("Phone        : "); String ph = sc.nextLine().trim();
        int deptId  = readInt("Department ID  : ");
        int desigId = readInt("Designation ID : ");
        double sal  = readDouble("Salary         : ");
        System.out.print("Hire Date (yyyy-mm-dd): ");
        LocalDate hireDate = LocalDate.parse(sc.nextLine().trim());
        int mgr = readInt("Manager ID (0 if none): ");

        Employee emp = new Employee(fn, ln, em, ph, deptId, desigId, sal, hireDate, mgr == 0 ? null : mgr);
        System.out.println(empSvc.registerEmployee(emp) ? "Employee added." : "Failed.");
    }

    private static void listAllEmployees() {
        List<Employee> list = empSvc.getAllEmployees();
        System.out.println("\n======= ALL EMPLOYEES =======");
        list.forEach(e -> System.out.printf("ID: %3d | %-20s | %-15s | %-20s | INR %,.2f%n",
                e.getEmployeeId(), e.getFullName(), e.getDepartmentName(),
                e.getDesignationName(), e.getSalary()));
    }

    private static void searchEmployee() {
        System.out.print("Enter name keyword: ");
        String kw = sc.nextLine().trim();
        empSvc.searchByName(kw).forEach(e ->
                System.out.printf("ID: %d | %s | %s%n",
                        e.getEmployeeId(), e.getFullName(), e.getEmail()));
    }

    private static void updateSalary() {
        int id        = readInt("Employee ID  : ");
        double salary = readDouble("New Salary   : ");
        System.out.println(empSvc.updateSalary(id, salary) ? "Salary updated." : "Failed.");
    }

    private static void assignDept() {
        int empId  = readInt("Employee ID   : ");
        int deptId = readInt("Department ID : ");
        System.out.println(empSvc.assignDepartment(empId, deptId) ? "Department assigned." : "Failed.");
    }

    private static void deactivateEmployee() {
        int id = readInt("Employee ID to deactivate: ");
        System.out.println(empSvc.removeEmployee(id) ? "Employee deactivated." : "Failed.");
    }

    private static void filterBySalary() {
        double min = readDouble("Min Salary: ");
        double max = readDouble("Max Salary: ");
        empSvc.filterBySalary(min, max).forEach(e ->
                System.out.printf("%-20s | INR %,.2f%n", e.getFullName(), e.getSalary()));
    }

    // ─────────────────────────────────────────────────────────────
    //  DEPARTMENT MENU
    // ─────────────────────────────────────────────────────────────
    private static void departmentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Department Management ---");
            System.out.println("1. Add Department");
            System.out.println("2. View All Departments");
            System.out.println("0. Back");
            int ch = readInt("Choice: ");
            switch (ch) {
                case 1 -> { System.out.print("Name: ");     String n = sc.nextLine();
                    System.out.print("Location: "); String l = sc.nextLine();
                    System.out.println(deptSvc.addDepartment(n, l) ? "Added." : "Failed."); }
                case 2 -> deptSvc.printAllDepartments();
                case 0 -> back = true;
            }
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  LEAVE MENU
    // ─────────────────────────────────────────────────────────────
    private static void leaveMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Leave Management ---");
            System.out.println("1. Apply for Leave");
            System.out.println("2. View My Leave History");
            System.out.println("3. View Pending Leaves");
            System.out.println("4. Approve Leave");
            System.out.println("5. Reject Leave");
            System.out.println("0. Back");
            int ch = readInt("Choice: ");
            switch (ch) {
                case 1 -> applyLeave();
                case 2 -> { int id = readInt("Employee ID: "); leaveSvc.printLeaveHistory(id); }
                case 3 -> leaveSvc.getPendingLeaves().forEach(l ->
                        System.out.printf("LeaveID: %d | %-20s | %-8s | %s → %s | Days: %d%n",
                                l.getLeaveId(), l.getEmployeeName(), l.getLeaveType(),
                                l.getStartDate(), l.getEndDate(), l.getTotalDays()));
                case 4 -> { int lid = readInt("Leave ID to approve: ");
                    System.out.println(leaveSvc.approveLeave(lid) ? "Approved." : "Failed."); }
                case 5 -> { int lid = readInt("Leave ID to reject: ");
                    System.out.println(leaveSvc.rejectLeave(lid)  ? "Rejected." : "Failed."); }
                case 0 -> back = true;
            }
        }
    }

    private static void applyLeave() {
        int empId = readInt("Employee ID : ");
        System.out.println("Leave Type (SICK / CASUAL / EARNED): ");
        String type = sc.nextLine().trim().toUpperCase();
        System.out.print("Start Date (yyyy-mm-dd): ");
        LocalDate start = LocalDate.parse(sc.nextLine().trim());
        System.out.print("End Date   (yyyy-mm-dd): ");
        LocalDate end = LocalDate.parse(sc.nextLine().trim());
        System.out.print("Remarks: ");
        String remarks = sc.nextLine().trim();
        System.out.println(leaveSvc.applyForLeave(empId, type, start, end, remarks) ? "Leave applied." : "Failed.");
    }

    // ─────────────────────────────────────────────────────────────
    //  REPORTS MENU
    // ─────────────────────────────────────────────────────────────
    private static void reportsMenu() {
        System.out.println("\n--- Reports ---");
        System.out.println("1. Salary Summary by Department");
        System.out.println("2. Department-wise Employee List");
        System.out.println("3. Employees Sorted by Salary");
        System.out.println("4. Employee Lookup by ID (HashMap demo)");
        int ch = readInt("Choice: ");
        switch (ch) {
            case 1 -> empSvc.printSalarySummary();
            case 2 -> {
                Map<String, List<Employee>> report = empSvc.getDepartmentWiseReport();
                report.forEach((dept, emps) -> {
                    System.out.println("\n► " + dept + " (" + emps.size() + " employees)");
                    emps.forEach(e -> System.out.println("   - " + e.getFullName() + " | " + e.getDesignationName()));
                });
            }
            case 3 -> empSvc.getEmployeesSortedBySalary().forEach(e ->
                    System.out.printf("%-20s | INR %,.2f%n", e.getFullName(), e.getSalary()));
            case 4 -> {
                int id = readInt("Employee ID: ");
                Map<Integer, Employee> map = empSvc.getEmployeeLookupMap();
                Employee emp = map.get(id);
                System.out.println(emp != null ? emp : "Employee not found.");
            }
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────────────────────
    private static void printMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("  1. Employee Management        ");
        System.out.println("  2. Department Management      ");
        System.out.println("  3. Leave Management           ");
        System.out.println("  4. Reports                    ");
        System.out.println("  0. Exit                       ");
        System.out.println("===============================");
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        try { int v = Integer.parseInt(sc.nextLine().trim()); return v; }
        catch (NumberFormatException e) { return -1; }
    }

    private static double readDouble(String prompt) {
        System.out.print(prompt);
        try { return Double.parseDouble(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return 0; }
    }
}
