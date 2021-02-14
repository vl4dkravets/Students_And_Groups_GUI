package frame;

import java.text.DateFormat;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

import logic.Student;

public class StudentTableModel extends AbstractTableModel {
    // Storage for list of students
    private final Vector<Student> students;

    // Table model gets the list of students
    public StudentTableModel(Vector<Student> students) {
        this.students = students;
    }

    // # of rows = # of students
    public int getRowCount() {
        if (students != null) {
            return students.size();
        }
        return 0;
    }

    public int getColumnCount() {
        return 4;
    }

    public String getColumnName(int column) {
        String[] colNames = {"ID", "First name", "Last name", "DoB"};
        return colNames[column];
    }

    // Returns data from certain row & column
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (students != null) {
            Student st = students.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return st.getStudentId();
                case 1:
                    return st.getFirstName();
                case 2:
                    return st.getLastName();
                case 3:
                    return DateFormat.getDateInstance(DateFormat.SHORT).format(
                            st.getDateOfBirth());
            }
        }
        return null;
    }

    // Returns a student based on a table
    public Student getStudent(int rowIndex) {
        if (students != null) {
            if (rowIndex < students.size() && rowIndex >= 0) {
                return students.get(rowIndex);
            }
        }
        return null;
    }
}