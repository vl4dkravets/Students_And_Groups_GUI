package frame;

import java.sql.SQLException;
import java.util.Vector;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import logic.Group;
import logic.ManagementSystem;
import logic.Student;

public class StudentsFrame extends JFrame implements ActionListener, ListSelectionListener, ChangeListener {

    // Action commands for buttons
    private static final String MOVE_GR = "moveGroup";
    private static final String CLEAR_GR = "clearGroup";
    private static final String INSERT_ST = "insertStudent";
    private static final String UPDATE_ST = "updateStudent";
    private static final String DELETE_ST = "deleteStudent";
    private static final String ALL_STUDENTS = "allStudent";

    private final ManagementSystem ms;
    private final JList<Group> grpList;
    private final JTable stdList;
    private final JSpinner spYear;

    public StudentsFrame() throws Exception {
        getContentPane().setLayout(new BorderLayout());

        // Menu bar on top
        JMenuBar menuBar = new JMenuBar();
        // Menu item
        JMenu menu = new JMenu("Report");
        // Element in menu item
        JMenuItem menuItem = new JMenuItem("Student list");
        menuItem.setName(ALL_STUDENTS);

        menuItem.addActionListener(this);

        menu.add(menuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Top panel which holds date spinner
        JPanel top = new JPanel();
        top.setLayout(new FlowLayout(FlowLayout.LEFT));

        top.add(new JLabel("Year in college:"));

        // Spinner = numbers specify the behavior
        SpinnerModel sm = new SpinnerNumberModel(2006, 1900, 2100, 1);
        spYear = new JSpinner(sm);
        spYear.addChangeListener(this);
        top.add(spYear);

        // Bottom panel for group list and student table
        JPanel bot = new JPanel();
        bot.setLayout(new BorderLayout());

        GroupPanel left = new GroupPanel();
        left.setLayout(new BorderLayout());
        left.setBorder(new BevelBorder(BevelBorder.LOWERED));

        // Allow to get DB connection and perform DB operations
        ms = ManagementSystem.getInstance();

        // Initializing a list of student we have in our DB
        Vector<Group> gr = new Vector<>(ms.getGroups());

        left.add(new JLabel("Groups:"), BorderLayout.NORTH);
        // Visual list of groups
        grpList = new JList<>(gr);
        grpList.addListSelectionListener(this);
        // First group will be automatically highlighted
        grpList.setSelectedIndex(0);
        left.add(new JScrollPane(grpList), BorderLayout.CENTER);

        // Buttons to manage groups
        JButton btnMvGr = new JButton("Move");
        btnMvGr.setName(MOVE_GR);
        JButton btnClGr = new JButton("Clear");
        btnClGr.setName(CLEAR_GR);

        btnMvGr.addActionListener(this);
        btnClGr.addActionListener(this);

        // Sub panel to hold buttons for groups
        JPanel pnlBtnGr = new JPanel();
        pnlBtnGr.setLayout(new GridLayout(1, 2));
        pnlBtnGr.add(btnMvGr);
        pnlBtnGr.add(btnClGr);
        left.add(pnlBtnGr, BorderLayout.SOUTH);

        // Right panel to hold table with students
        JPanel right = new JPanel();
        right.setLayout(new BorderLayout());
        right.setBorder(new BevelBorder(BevelBorder.LOWERED));


        right.add(new JLabel("Students:"), BorderLayout.NORTH);

        // Creating a table
        stdList = new JTable(1, 4);
        right.add(new JScrollPane(stdList), BorderLayout.CENTER);
        // Создаем кнопки для студентов
        JButton btnAddSt = new JButton("Add student");
        btnAddSt.setName(INSERT_ST);
        btnAddSt.addActionListener(this);
        JButton btnUpdSt = new JButton("Edit student");
        btnUpdSt.setName(UPDATE_ST);
        btnUpdSt.addActionListener(this);
        JButton btnDelSt = new JButton("Delete student");
        btnDelSt.setName(DELETE_ST);
        btnDelSt.addActionListener(this);

        // Panel to hold buttons for student's table
        JPanel pnlBtnSt = new JPanel();
        pnlBtnSt.setLayout(new GridLayout(1, 3));
        pnlBtnSt.add(btnAddSt);
        pnlBtnSt.add(btnUpdSt);
        pnlBtnSt.add(btnDelSt);
        right.add(pnlBtnSt, BorderLayout.SOUTH);

        bot.add(left, BorderLayout.WEST);
        bot.add(right, BorderLayout.CENTER);

        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(bot, BorderLayout.CENTER);

        setBounds(100, 100, 700, 500);
    }

    // Event handler
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Component) {
            Component c = (Component) e.getSource();
            if (c.getName().equals(MOVE_GR)) {
                moveGroup();
            }
            if (c.getName().equals(CLEAR_GR)) {
                clearGroup();
            }
            if (c.getName().equals(ALL_STUDENTS)) {
                showAllStudents();
            }
            if (c.getName().equals(INSERT_ST)) {
                insertStudent();
            }
            if (c.getName().equals(UPDATE_ST)) {
                updateStudent();
            }
            if (c.getName().equals(DELETE_ST)) {
                deleteStudent();
            }
        }
    }

    // Detects which groups in the list is highlighted
    // As the result, each time it reloads the list of students
    // based on a corresponding group
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            reloadStudents();
        }
    }

    // Reacts to a change in spinner when a new year was selected
    public void stateChanged(ChangeEvent e) {
        reloadStudents();
    }

    public void reloadStudents() {
        // Anonymous class which runs refresh list operation on
        // a separate thread to avoid pauses and glitches
        // when performing everything on a single (EDT) thread
        Thread t = new Thread(() -> {

            if (stdList != null) {
                // Get a highlighted group
                Group g = grpList.getSelectedValue();
                // Gets a number(year) from a spinner
                int y = ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
                try {
                    // Based on input date, retrieve list of available students
                    Collection<Student> s = ms.getStudentsFromGroup(g, y);
                    // Create a new model for visual table based on newly retrieved list
                    // of students
                    stdList.setModel(new StudentTableModel(new Vector<>(s)));
                } catch (SQLException e) {
                    // JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        // starting a thread
        t.start();
    }

    private void moveGroup() {
        Thread t = new Thread(() -> {

            // If a group wasn't selected => leave method
            if (grpList.getSelectedValue() == null) {
                return;
            }
            try {
                // Get a highlighted group
                Group g = grpList.getSelectedValue();
                // Gets a number(year) from a spinner
                int y = ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
                // Creating a new dialog window
                GroupDialog gd = new GroupDialog(y, ms.getGroups());
                // Setting it to modal - the only active window on a screen
                gd.setModal(true);
                // Displaying it
                gd.setVisible(true);
                // If OK was pressed - we save input data
                if (gd.getResult()) {
                    ms.moveStudentsToGroup(g, y, gd.getGroup(), gd.getYear());
                    reloadStudents();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
            }
        });
        t.start();
    }

    private void clearGroup() {
        Thread t = new Thread(() -> {

            if (grpList.getSelectedValue() != null) {
                if (JOptionPane.showConfirmDialog(StudentsFrame.this,
                        "Do you wish to delete students from the selected group", "Deleting students",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    Group g = grpList.getSelectedValue();
                    int y = ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
                    try {
                        ms.removeStudentsFromGroup(g, y);
                        reloadStudents();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                    }
                }
            }
        });
        t.start();
    }

    private void insertStudent() {
        Thread t = new Thread(() -> {

            try {
                // Dialog for entering student info
                // Takes flag true, telling dialog it'll be used to create a new student
                // Also, it'll need the instance of main frame to refresh the list of students
                StudentDialog sd = new StudentDialog(ms.getGroups(), true, StudentsFrame.this);
                sd.setModal(true);
                sd.setVisible(true);
                // if SAVE was pressed then
                if (sd.getResult()) {
                    // retrieve Student from dialog form
                    Student s = sd.getStudent();
                    ms.insertStudent(s);
                    reloadStudents();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
            }
        });
        t.start();
    }

    private void updateStudent() {
        Thread t = new Thread(() -> {

            if (stdList != null) {
                StudentTableModel stm = (StudentTableModel) stdList.getModel();
                if (stdList.getSelectedRow() >= 0) {
                    // Вот где нам пригодился метод getStudent(int rowIndex)
                    Student s = stm.getStudent(stdList.getSelectedRow());
                    try {
                        // This time, flag  is false, telling the dialog that it won't create a new student
                        // but instead will need to edit an existing one from DB
                        StudentDialog sd = new StudentDialog(ms.getGroups(), false, StudentsFrame.this);
                        // here, we set selected student manually by passing its instance to the Dialog
                        // where it'll be used to automatically initialize all the fields
                        sd.setStudent(s);
                        sd.setModal(true);
                        sd.setVisible(true);
                        // After Student was initialized, you'd then proceed with editing the data in the text fields
                        // Then, if SAVE was pressed
                        if (sd.getResult()) {
                            // you create a student based on input from dialog text fields
                            Student us = sd.getStudent();
                            ms.updateStudent(us);
                            reloadStudents();
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                    }
                } // Если студент не выделен - сообщаем пользователю, что это необходимо
                else {
                    JOptionPane.showMessageDialog(StudentsFrame.this,
                            "You need to select a student you wish to update");
                }
            }
        });
        t.start();
    }

    private void deleteStudent() {
        Thread t = new Thread(() -> {

            if (stdList != null) {
                StudentTableModel stm = (StudentTableModel) stdList.getModel();
                if (stdList.getSelectedRow() >= 0) {
                    if (JOptionPane.showConfirmDialog(StudentsFrame.this,
                            "Do you want to delete a student?", "Delete student",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        // Вот где нам пригодился метод getStudent(int rowIndex)
                        Student s = stm.getStudent(stdList.getSelectedRow());
                        try {
                            ms.deleteStudent(s);
                            reloadStudents();
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                        }
                    }
                } // If student wasn't selected => display error
                else {
                    JOptionPane.showMessageDialog(StudentsFrame.this, "You need to select a student fom the table ");
                }
            }
        });
        t.start();
    }

    // handles showing all the students when menu bar option Report was chosen
    private void showAllStudents() {
        JOptionPane.showMessageDialog(this, "showAllStudents");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            try {
                // If there was a fail creating main frame
                // & specifically DB connection => exit
                StudentsFrame sf = new StudentsFrame();
                sf.setDefaultCloseOperation(EXIT_ON_CLOSE);
                sf.setVisible(true);
                sf.reloadStudents();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });
    }
}

// Inner class - defines my own version of panel
class GroupPanel extends JPanel {

    public Dimension getPreferredSize() {
        return new Dimension(250, 0);
    }
}