package Service;

import dao.LeaveDAO;
import Model.LeaveRequest;
import Util.Constants;

import java.time.LocalDate;
import java.util.List;

public class LeaveService {
    private final LeaveDAO leaveDAO = new LeaveDAO();

    public boolean applyForLeave(int employeeId, String leaveType, LocalDate start, LocalDate end, String remarks) {
        if (end.isBefore(start)) {
            System.out.println("End date cannot be before start date.");
            return false;
        }
        LeaveRequest leave = new LeaveRequest(employeeId, leaveType, start, end, remarks);
        return leaveDAO.applyLeave(leave);
    }

    public boolean approveLeave(int leaveId) {
        return leaveDAO.updateLeaveStatus(leaveId, Constants.STATUS_APPROVED);
    }

    public boolean rejectLeave(int leaveId) {
        return leaveDAO.updateLeaveStatus(leaveId, Constants.STATUS_REJECTED);
    }

    public List<LeaveRequest> getLeaveHistory(int employeeId) {
        return leaveDAO.getLeavesByEmployee(employeeId);
    }

    public List<LeaveRequest> getPendingLeaves() {
        return leaveDAO.getPendingLeaves();
    }

    public void printLeaveHistory(int employeeId) {
        List<LeaveRequest> leaves = getLeaveHistory(employeeId);
        System.out.println("\n======= LEAVE HISTORY (Emp ID: " + employeeId + ") =======");
        if (leaves.isEmpty()) { System.out.println("No leave records found."); return; }
        leaves.forEach(l -> System.out.printf(
                "LeaveID: %d | Type: %-8s | %s → %s | Days: %d | Status: %s%n",
                l.getLeaveId(), l.getLeaveType(), l.getStartDate(), l.getEndDate(),
                l.getTotalDays(), l.getStatus()));
        System.out.println("======================================================\n");
    }
}
