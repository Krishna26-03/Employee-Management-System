# 🏢 Employee Management System

A console-based **Employee Management System** built with **Java**, **JDBC**, and **MySQL** that handles employee records, department management, salary calculations, and leave tracking.

---

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| Java | Core application logic |
| JDBC | Database connectivity |
| MySQL | Data storage |
| Collections Framework | HashMap (lookup), ArrayList (reports) |

---

## ✨ Features

- ✅ Employee registration & management
- ✅ Department assignment
- ✅ Salary structure management
- ✅ Employee search & filtering
- ✅ Department-wise reports
- ✅ Leave tracking (Apply / Approve / Reject)
- ✅ Role-based hierarchy (Manager, Developer, Designer)
- ✅ Salary summary reports

---

## 📁 Project Structure

```
EmployeeManagementSystem/
│
├── database/
│   ├── schema.sql          ← Run first — creates all tables
│   ├── seed_data.sql       ← Run second — inserts sample data
│   └── queries.sql         ← Reference SQL queries
│
└── src/com/ems/
    │
    ├── model/              ← Data classes (what things are)
    │   ├── Employee.java         ← Base employee entity
    │   ├── EmployeeRoles.java    ← Manager / Developer / Designer
    │   ├── Department.java       ← Department entity
    │   ├── Designation.java      ← Designation/job title entity
    │   └── LeaveRequest.java     ← Leave request entity
    │
    ├── dao/                ← Database layer (JDBC queries)
    │   ├── EmployeeDAO.java      ← CRUD for employees + HashMap cache
    │   ├── DepartmentDAO.java    ← CRUD for departments
    │   └── LeaveDAO.java         ← CRUD for leave requests
    │
    ├── service/            ← Business logic layer
    │   ├── EmployeeService.java  ← Employee business rules + reports
    │   ├── DepartmentService.java← Department business rules
    │   └── LeaveService.java     ← Leave approval logic
    │
    ├── util/               ← Helper classes
    │   ├── DBConnection.java     ← Singleton DB connection
    │   └── Constants.java        ← App-wide constants
    │
    └── main/
        └── Main.java             ← Entry point + console menu
```

---

## 🗄️ Database Schema

```
departments          designations
─────────────        ─────────────
department_id  PK    designation_id  PK
department_name      designation_name
location             base_salary
                     role_level

employees                        leave_requests
─────────────────────────────    ──────────────────
employee_id     PK               leave_id      PK
first_name                       employee_id   FK → employees
last_name                        leave_type    (SICK/CASUAL/EARNED)
email                            start_date
phone                            end_date
department_id   FK → departments total_days
designation_id  FK → designations status       (PENDING/APPROVED/REJECTED)
salary                           applied_on
hire_date                        remarks
is_active
manager_id      FK → employees (self-reference)
```

---

## ⚙️ Setup Instructions

### Step 1 — Database Setup
Open **MySQL Workbench** or terminal and run:
```sql
source database/schema.sql;
source database/seed_data.sql;
```

### Step 2 — Configure DB Credentials
Open `src/com/ems/util/DBConnection.java` and update:
```java
private static final String URL      = "jdbc:mysql://localhost:3306/employee_management";
private static final String USER     = "root";
private static final String PASSWORD = "your_password";  // ← change this
```

### Step 3 — Add MySQL JDBC Driver
Download `mysql-connector-j-8.x.x.jar` and add to classpath:

**IntelliJ IDEA:**
```
File → Project Structure → Libraries → + → JAR → Select the .jar file
```

**VS Code:**
Add to `lib/` folder and reference in `launch.json`

### Step 4 — Run the App
Run `com.ems.main.Main` as the main class.

---

## 🖥️ Console Menu

```
╔══════════ MAIN MENU ══════════╗
║  1. Employee Management        ║
║  2. Department Management      ║
║  3. Leave Management           ║
║  4. Reports                    ║
║  0. Exit                       ║
╚═══════════════════════════════╝
```

### Employee Management
- Add new employee
- View all employees
- Search by name
- Update salary
- Assign department
- Deactivate employee
- Filter by salary range

### Leave Management
- Apply for leave
- View leave history
- Approve / Reject leave
- View pending leaves

### Reports
- Salary summary by department
- Department-wise employee list
- Employees sorted by salary
- Employee lookup by ID

---

## 🏗️ Key Design Concepts

| Concept | Where Used |
|---------|-----------|
| **Inheritance** | `Manager`, `Developer`, `Designer` extend `Employee`; each overrides `calculateBonus()` |
| **HashMap** | `EmployeeDAO` — O(1) employee lookup cache by ID |
| **ArrayList** | All list results — search, department reports, leave history |
| **DAO Pattern** | Separates DB/SQL logic from business logic |
| **Service Layer** | Business rules live in `*Service` classes |
| **Singleton** | `DBConnection` — single shared DB connection |
| **Soft Delete** | Employees marked `is_active=FALSE`, not permanently deleted |
| **Encapsulation** | All fields `private`, accessed via getters/setters |

---

## 👥 Role-Based Hierarchy

```
Employee (Base Class)
├── Manager    → 20% bonus + team size allowance
├── Developer  → 15% bonus + tech stack bonus
└── Designer   → 12% bonus + tools expertise bonus
```

---

## 📊 Collections Used

```java
// HashMap — fast O(1) employee lookup by ID
Map<Integer, Employee> employeeCache = new HashMap<>();

// ArrayList — department-wise employee lists
List<Employee> deptEmployees = new ArrayList<>();

// HashMap — group employees by department for reports
Map<String, List<Employee>> deptReport = new HashMap<>();
```

---

## 👨‍💻 Author

**Krishna Patel**  
B.Tech CSE | Java Developer  
GitHub: https://github.com/Krishna26-03


## 📄 License

This project is for educational purposes.

---
