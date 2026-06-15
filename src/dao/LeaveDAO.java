package dao;

import Model.LeaveRequest;
import Util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaveDAO {
    public boolean applyLeave(LeaveRequest leave) {
        String sql = "INSERT INTO leave_requests (employee_id, leave_type, start_date, end_date, total_days, status, remarks) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt   (1, leave.getEmployeeId());
            ps.setString(2, leave.getLeaveType());
            ps.setDate  (3, Date.valueOf(leave.getStartDate()));
            ps.setDate  (4, Date.valueOf(leave.getEndDate()));
            ps.setInt   (5, leave.getTotalDays());
            ps.setString(6, leave.getStatus());
            ps.setString(7, leave.getRemarks());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) leave.setLeaveId(keys.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error applying leave: " + e.getMessage());
        }
        return false;
    }

    public boolean updateLeaveStatus(int leaveId, String status) {
        String sql = "UPDATE leave_requests SET status = ? WHERE leave_id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt   (2, leaveId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating leave status: " + e.getMessage());
        }
        return false;
    }

    public List<LeaveRequest> getLeavesByEmployee(int employeeId) {
        List<LeaveRequest> list = new ArrayList<>();
        String sql = "SELECT l.*, CONCAT(e.first_name,' ',e.last_name) AS employee_name " +
                "FROM leave_requests l JOIN employees e ON l.employee_id = e.employee_id " +
                "WHERE l.employee_id = ? ORDER BY l.applied_on DESC";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("Error fetching leaves: " + e.getMessage());
        }
        return list;
    }

    public List<LeaveRequest> getPendingLeaves() {
        List<LeaveRequest> list = new ArrayList<>();
        String sql = "SELECT l.*, CONCAT(e.first_name,' ',e.last_name) AS employee_name " +
                "FROM leave_requests l JOIN employees e ON l.employee_id = e.employee_id " +
                "WHERE l.status = 'PENDING' ORDER BY l.applied_on";
        try (Connection conn = DbConnection.getConnection();
             Statement  st   = conn.createStatement();
             ResultSet  rs   = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("Error fetching pending leaves: " + e.getMessage());
        }
        return list;
    }

    private LeaveRequest mapRow(ResultSet rs) throws SQLException {
        LeaveRequest l = new LeaveRequest();
        l.setLeaveId    (rs.getInt   ("leave_id"));
        l.setEmployeeId (rs.getInt   ("employee_id"));
        l.setLeaveType  (rs.getString("leave_type"));
        l.setStartDate  (rs.getDate  ("start_date").toLocalDate());
        l.setEndDate    (rs.getDate  ("end_date").toLocalDate());
        l.setTotalDays  (rs.getInt   ("total_days"));
        l.setStatus     (rs.getString("status"));
        l.setRemarks    (rs.getString("remarks"));
        l.setEmployeeName(rs.getString("employee_name"));
        return l;
    }
}
