package dao;

import Model.Department;
import Util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    public boolean addDepartment(Department dept) {
        String sql = "INSERT INTO departments (department_name, location) VALUES (?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, dept.getDepartmentName());
            ps.setString(2, dept.getLocation());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) dept.setDepartmentId(keys.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding department: " + e.getMessage());
        }
        return false;
    }

    public List<Department> getAllDepartments() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM departments ORDER BY department_id";
        try (Connection conn = DbConnection.getConnection();
             Statement st   = conn.createStatement();
             ResultSet rs   = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Department(
                        rs.getInt("department_id"),
                        rs.getString("department_name"),
                        rs.getString("location")));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching departments: " + e.getMessage());
        }
        return list;
    }

    public Department getDepartmentById(int id) {
        String sql = "SELECT * FROM departments WHERE department_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new Department(
                    rs.getInt("department_id"),
                    rs.getString("department_name"),
                    rs.getString("location"));
        } catch (SQLException e) {
            System.err.println("Error fetching department: " + e.getMessage());
        }
        return null;
    }

    public boolean updateDepartment(Department dept) {
        String sql = "UPDATE departments SET department_name=?, location=? WHERE department_id=?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dept.getDepartmentName());
            ps.setString(2, dept.getLocation());
            ps.setInt   (3, dept.getDepartmentId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating department: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteDepartment(int id) {
        String sql = "DELETE FROM departments WHERE department_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting department: " + e.getMessage());
        }
        return false;
    }
}
