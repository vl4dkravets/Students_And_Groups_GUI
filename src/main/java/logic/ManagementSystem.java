package logic;

import config.Config;
import dao.ConnectionBuilder;
import dao.ConnectionBuilderFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.joda.time.LocalDate;

public class ManagementSystem {

    private static Connection con;
    private static ManagementSystem instance;
    private final ConnectionBuilder builder;

    private static final String selectGroups = "SELECT group_id, group_name, professor_name, speciality_field FROM groups";
    private static final String selectStudents ="SELECT student_id, first_name, last_name, " +
                                                 "date_of_birth, sex, group_id, year_in_college FROM students ORDER BY last_name, first_name";
    private static final String selectStudentsFromGroup ="SELECT student_id, first_name, last_name, date_of_birth, sex, group_id, year_in_college FROM students " +
                                                            "WHERE group_id=? AND year_in_college=? ORDER BY last_name, first_name";
    private static final String moveStudentsToGroup = "UPDATE students SET group_id=?, year_in_college=? WHERE group_id=? AND year_in_college=?";
    private static final String deleteStudentsFromGroup = "DELETE FROM students WHERE group_id=? AND year_in_college=?";
    private static final String insertStudent = "INSERT INTO students (first_name, last_name, date_of_birth, sex, group_id, year_in_college) " +
                                                "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String updateStudent = "UPDATE students SET first_name=?, last_name=?, date_of_birth=?,  sex=?, group_id=?, year_in_college=?" +
                                                "WHERE student_id=?";
    private static final String deleteStudent = "DELETE FROM students WHERE student_id=?";


    private ManagementSystem() throws Exception {
        try {
            // connect to config file / loads it
            Config.initGlobalConfig();
            builder = ConnectionBuilderFactory.getConnectionBuilder();
            con = getConnection();
        }
        catch (SQLException e) {
            throw new Exception(e);
        }
    }

    // Returns an instance of a new connection
    // Simulates ConnectionPool
    // Safer option when considering multithreading environment
    private Connection getConnection() throws SQLException {
        return builder.getConnection();
    }

    // Singleton
    public static ManagementSystem getInstance() throws Exception {
        if (instance == null) {
            instance = new ManagementSystem();
        }
        return instance;
    }

    public List<Group> getGroups() throws SQLException {
        List<Group> groups = new ArrayList<>();

        try(Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectGroups))
        {
            while (rs.next()) {
                Group gr = new Group();
                gr.setGroupId(rs.getInt(1));
                gr.setGroupName(rs.getString(2));
                gr.setProfessorName(rs.getString(3));
                gr.setSpecialityField(rs.getString(4));

                groups.add(gr);
            }
        }

        return groups;
    }

    public Collection<Student> getAllStudents() throws SQLException {
        Collection<Student> students = new ArrayList<>();

        try(Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectStudents)) {

            while (rs.next()) {
                Student st = new Student(rs);
                students.add(st);
            }
        }
        return students;
    }

    public Collection<Student> getStudentsFromGroup(Group group, int year) throws SQLException {
        Collection<Student> students = new ArrayList<>();

        ResultSet rs = null;
        try (PreparedStatement stmt = con.prepareStatement(selectStudentsFromGroup)){

            stmt.setInt(1, group.getGroupId());
            stmt.setInt(2, year);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Student st = new Student(rs);

                students.add(st);
            }
        }
        catch(NullPointerException e) {
            // exception is thrown if group doesn't have an ID / wasn't created yet
        }
        finally {
            if (rs != null) {
                rs.close();
            }
        }

        return students;
    }

    public void moveStudentsToGroup(Group oldGroup, int oldYear, Group newGroup, int newYear) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(moveStudentsToGroup)) {
            stmt.setInt(1, newGroup.getGroupId());
            stmt.setInt(2, newYear);
            stmt.setInt(3, oldGroup.getGroupId());
            stmt.setInt(4, oldYear);
            stmt.execute();
        }
    }

    public void removeStudentsFromGroup(Group group, int year) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(deleteStudentsFromGroup)) {
            stmt.setInt(1, group.getGroupId());
            stmt.setInt(2, year);
            stmt.execute();
        }
    }

    public void insertStudent(Student student) throws SQLException {
        try(PreparedStatement stmt = con.prepareStatement(insertStudent)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setDate(3, new Date(student.getDateOfBirth().getTime()));
            stmt.setString(4, new String(new char[]{student.getSex()}));
            stmt.setInt(5, student.getGroupId());
            stmt.setInt(6, student.getYearInCollege());
            stmt.execute();
        }
    }

    public void updateStudent(Student student) throws SQLException {
        try(PreparedStatement stmt = con.prepareStatement(updateStudent)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setDate(3, new Date(student.getDateOfBirth().getTime()));
            stmt.setString(4, new String(new char[]{student.getSex()}));
            stmt.setInt(5, student.getGroupId());
            stmt.setInt(6, student.getYearInCollege());
            stmt.setInt(7, student.getStudentId());
            stmt.execute();
        }
    }

    public void deleteStudent(Student student) throws SQLException {
        try(PreparedStatement stmt = con.prepareStatement(deleteStudent)) {
            stmt.setInt(1, student.getStudentId());
            stmt.execute();
        }
    }
}