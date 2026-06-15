package dao;

import Model.Employee;
import Util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDAO {

    //HashMap cache: EmployeeId -> Employee for fast lookup
    private Map<Integer, Employee> employeeCache = new HashMap<>();

    //CREATE
    public boolean addEmployee(Employee emp){
        String sql = "INSERT INTO employees (first_name, last_name, email, phone, " + "department_id, designation_id, salary, hire_date, manager_id" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?";
        try(Connection conn = DbConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            ps.setString(1, emp.getFirstName());
            ps.setString(2, emp.getLastName());
            ps.setString(3, emp.getEmail());
            ps.setString(4, emp.getPhone());
            ps.setInt(5, emp.getDepartmentId());
            ps.setInt(6, emp.getDesignationId());
            ps.setDouble(7, emp.getSalary());
            ps.setDate(8, Date.valueOf(emp.getHireDate()));
            if (emp.getManagerId() != null){
                ps.setInt(9, emp.getManagerId());
            }else{
                ps.setNull(9, Types.INTEGER);
            }

            int rows = ps.executeUpdate();
            if (rows > 0){
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    emp.setEmployeeId(keys.getInt(1));
                }
                employeeCache.put(emp.getEmployeeId(), emp);
                return true;
            }
        }
        catch (SQLException e){
            System.err.println("Error adding employee: " + e.getMessage());
        }
        return false;
    }

    //READ get by ID (uses cache)
    public Employee getEmployeeById(int id){
        if (employeeCache.containsKey(id)){
            return employeeCache.get(id);
        }
        String sql = "SELECT e.*, d.department_name, des.designation_name " +
                "LEFT JOINS departments d ON e.department_id = d.department_id " +
                "LEFT JOINS designations des ON e.designation_id = des.designation_id " +
                "WHERE e.employee_id = ? AND e.is_active = TRUE";
        try(Connection conn = DbConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Employee emp = mapRow(rs);
                employeeCache.put(id, emp);
                return emp;
            }
        } catch (SQLException e){
            System.err.println("Error fetching employee: " + e.getMessage());
        }
        return null;
    }

    //READ - all active employees -> ArrayList
    public List<Employee> getAllEmployees(){
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT e.*, d.department_name, des.designation_name " +
                "FROM employees e " +
                "LEFT JOIN departments d ON e.department_id = d.department_id " +
                "LEFT JOIN designations des ON e.designation_id = des.designation_id " +
                "WHERE e.is_active = TRUE ORDER BY e.employee_id";
        try( Connection conn = DbConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Employee emp = mapRow(rs);
                list.add(emp);
                employeeCache.put(emp.getEmployeeId(), emp);
            }
        } catch (SQLException e){
            System.err.println("Error fetching employees: " + e.getMessage());
        }
        return list;
    }

    //READ -> by department
    public List<Employee> getEmployeesByDepartment(int departmentId) {
        List<Employee> list = new ArrayList<>();
        String sql = "Select e.*, d.department_name, des.designation_name " +
                "FROM employees e " +
                "LEFT JOIN departments d ON e.department_id = d.department_id " +
                "LEFT JOIN designations des ON e.department_id = des.designation_id " +
                "WHERE e.department_id = ? AND e.is_active = TRUE";
        try(Connection conn = DbConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
                System.err.println("Error fetching by dept: " + e.getMessage());
        }
        return list;
    }

    //READ - search by name
    public List<Employee> searchEmployeesByName(String keyword) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT e.*, d.department_name, des.designation_name " +
                "FROM employees e " +
                "LEFT JOIN departments d   ON e.department_id  = d.department_id " +
                "LEFT JOIN designations des ON e.designation_id = des.designation_id " +
                "WHERE (e.first_name LIKE ? OR e.last_name LIKE ?) AND e.is_active = TRUE";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("Error searching employees: " + e.getMessage());
        }
        return list;
    }

    //READ - filter by salary range
    public List<Employee> filterBySalaryRange(double min, double max) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT e.*, d.department_name, des.designation_name " +
                "FROM employees e " +
                "LEFT JOIN departments d   ON e.department_id  = d.department_id " +
                "LEFT JOIN designations des ON e.designation_id = des.designation_id " +
                "WHERE e.salary BETWEEN ? AND ? AND e.is_active = TRUE ORDER BY e.salary DESC";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, min);
            ps.setDouble(2, max);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("Error filtering by salary: " + e.getMessage());
        }
        return list;
    }

    //UPDATE
    public boolean updateEmployee(Employee emp) {
        String sql = "UPDATE employees SET first_name=?, last_name=?, email=?, phone=?, " +
                "department_id=?, designation_id=?, salary=?, manager_id=? " +
                "WHERE employee_id=?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emp.getFirstName());
            ps.setString(2, emp.getLastName());
            ps.setString(3, emp.getEmail());
            ps.setString(4, emp.getPhone());
            ps.setInt   (5, emp.getDepartmentId());
            ps.setInt   (6, emp.getDesignationId());
            ps.setDouble(7, emp.getSalary());
            if (emp.getManagerId() != null) ps.setInt(8, emp.getManagerId());
            else ps.setNull(8, Types.INTEGER);
            ps.setInt(9, emp.getEmployeeId());

            boolean ok = ps.executeUpdate() > 0;
            if (ok) employeeCache.put(emp.getEmployeeId(), emp);
            return ok;
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
        }
        return false;
    }

    //DELETE
    public boolean deactivateEmployee(int id) {
        String sql = "UPDATE employees SET is_active = FALSE WHERE employee_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) employeeCache.remove(id);
            return ok;
        } catch (SQLException e) {
            System.err.println("Error deactivating employee: " + e.getMessage());
        }
        return false;
    }

    //HashMap - load all into cache for fast lookup
    public Map<Integer, Employee> getEmployeeLookupMap() {
        if (employeeCache.isEmpty()) getAllEmployees();
        return new HashMap<>(employeeCache);
    }

    //Private helper - map ResultSet row -> Employee object
    private Employee mapRow(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setEmployeeId(rs.getInt("employee_id"));
        emp.setFirstName(rs.getString("first_name"));
        emp.setLastName(rs.getString("last_name"));
        emp.setEmail(rs.getString("email"));
        emp.setPhone(rs.getString("phone"));
        emp.setDepartmentId(rs.getInt("department_id"));
        emp.setDesignationId(rs.getInt("designation_id"));
        emp.setSalary(rs.getDouble("salary"));
        emp.setHireDate(rs.getDate("hire_date").toLocalDate());
        emp.setActive(rs.getBoolean("is_active"));
        int managerId = rs.getInt("manager_id");
        emp.setManagerId(rs.wasNull() ? null : managerId);
        emp.setDepartmentName (rs.getString("department_name"));
        emp.setDesignationName(rs.getString("designation_name"));
        return emp;
    }
}
