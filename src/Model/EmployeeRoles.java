package Model;

import java.time.LocalDate;

class Manager extends Employee {
    private int teamSize;

    public Manager(String firstName, String lastName, String email, String phone, int departmentId, int designationId, double salary, LocalDate hireDate, int teamSize) {
        super(firstName, lastName, email, phone, departmentId, designationId, salary, hireDate, null);
        this.teamSize = teamSize;
    }

    @Override
    public double calculateBonus() {
        double base = getSalary() * 0.20;
        double teamAllowance = teamSize * 500;
        return base + teamAllowance;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", teamSize=%d, bonus=%.2f", teamSize, calculateBonus());
    }
}

class Developer extends Employee{

    private String techStack;

    public Developer(String firstName, String lastName, String email, String phone, int departmentId, int designationId, double salary, LocalDate hireDate, Integer managerId, String techStack) {
        super(firstName, lastName, email, phone, departmentId, designationId, salary, hireDate, managerId);
        this.techStack = techStack;
    }

    @Override
    public double calculateBonus() {
        double base = getSalary() * 0.15;
        double techBonus = (techStack != null && techStack.toLowerCase().contains("full")) ? 3000.0 : 1000.0;
        return base + techBonus;
    }

    public String getTechStack() {
        return techStack;
    }

    public void setTechStack(String techStack) {
        this.techStack = techStack;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", techStack=%s, bonus=%.2f", techStack, calculateBonus());
    }
}

class Designer extends Employee{

    private String toolsExpertise;

    public Designer(String firstName, String lastName, String email, String phone, int departmentId, int designationId, double salary, LocalDate hireDate, Integer managerId, String toolsExpertise) {
        super(firstName, lastName, email, phone, departmentId, designationId, salary, hireDate, managerId);
        this.toolsExpertise = toolsExpertise;
    }

    @Override
    public double calculateBonus() {
        double base = getSalary() * 0.12;
        double toolCount = toolsExpertise != null ? toolsExpertise.split(",").length : 0;
        return base + (toolCount * 500);
    }

    public String getToolsExpertise() {
        return toolsExpertise;
    }

    public void setToolsExpertise(String toolsExpertise) {
        this.toolsExpertise = toolsExpertise;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", tools=%s, bonus=%.2f", toolsExpertise, calculateBonus());
    }
}