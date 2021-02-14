package logic;

import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Student {

    private int studentId;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private char sex;
    private int groupId;
    private int yearInCollege;

    public Student() {
    }

    public Student(ResultSet rs) throws SQLException {
        setStudentId(rs.getInt(1));
        setFirstName(rs.getString(2));
        setLastName(rs.getString(3));
        setDateOfBirth(rs.getDate(4));
        setSex(rs.getString(5).charAt(0));
        setGroupId(rs.getInt(6));
        setYearInCollege(rs.getInt(7));



    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getYearInCollege() {
        return yearInCollege;
    }

    public void setYearInCollege(int educationYear) {
        this.yearInCollege = educationYear;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String surName) {
        this.lastName = surName;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String toString() {
        return lastName + " " + firstName + ", "
                + DateFormat.getDateInstance(DateFormat.SHORT).format(dateOfBirth)
                + ", GroupID=" + groupId + " Sex: " +getSex() + " Year:" + yearInCollege;
    }

}