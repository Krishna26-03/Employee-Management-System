package Model;

public class Designation {

    private int designationId;
    private String designationName;
    private double baseSalary;
    private int roleLevel;

    public Designation(){}

    public Designation(int designationId, String designationName, double baseSalary, int roleLevel) {
        this.designationId = designationId;
        this.designationName = designationName;
        this.baseSalary = baseSalary;
        this.roleLevel = roleLevel;
    }

    public int getDesignationId() {
        return designationId;
    }

    public void setDesignationId(int designationId) {
        this.designationId = designationId;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public int getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(int roleLevel) {
        this.roleLevel = roleLevel;
    }

    @Override
    public String toString() {
        return String.format("Designation[id=%d, name=%s, baseSalary=%.2f, level=%d", designationId, designationName,baseSalary, roleLevel);
    }
}
