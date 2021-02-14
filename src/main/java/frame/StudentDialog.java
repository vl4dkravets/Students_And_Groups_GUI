package frame;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;

import logic.Group;
import logic.ManagementSystem;
import logic.Student;

public class StudentDialog extends JDialog implements ActionListener {

    private static final int D_HEIGHT = 200;   // height of a window
    private final static int D_WIDTH = 450;   // width of a window
    private final static int L_X = 10;      // left margin
    private final static int L_W = 100;     // width of a label
    private final static int C_W = 150;     // width of a text field

    private final StudentsFrame mainFrame;

    // state of a button
    private boolean result = false;


    private int studentId = 0;
    private final JTextField firstName = new JTextField();
    private final JTextField lastName = new JTextField();
    private final JSpinner dateOfBirth = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
    private final ButtonGroup sex = new ButtonGroup();
    private final JSpinner year = new JSpinner(new SpinnerNumberModel(2006, 1900, 2100, 1));
    private final JComboBox<Group> groupList;

    // second param determines whether we add a new student, or update an existing one
    public StudentDialog(List<Group> groups, boolean newStudent, StudentsFrame mainFrame) {
        // Need access to main window to refresh its list
        this.mainFrame = mainFrame;

        setTitle("Editing student");
        getContentPane().setLayout(new FlowLayout());

        groupList = new JComboBox<>(new Vector<>(groups));

        JRadioButton m = new JRadioButton("Male");
        JRadioButton w = new JRadioButton("Female");

        m.setActionCommand("M");
        w.setActionCommand("F");

        sex.add(m);
        sex.add(w);

        // Means, we'll set layout manually
        getContentPane().setLayout(null);

        // lastName component
        JLabel l = new JLabel("First name:", JLabel.RIGHT);
        l.setBounds(L_X, 10, L_W, 20);
        getContentPane().add(l);
        firstName.setBounds(L_X + L_W + 10, 10, C_W, 20);
        getContentPane().add(firstName);
        // First name
        l = new JLabel("Last name:", JLabel.RIGHT);
        l.setBounds(L_X, 30, L_W, 20);
        getContentPane().add(l);
        lastName.setBounds(L_X + L_W + 10, 30, C_W, 20);
        getContentPane().add(lastName);

        // Sex
        l = new JLabel("Sex:", JLabel.RIGHT);
        l.setBounds(L_X, 70, L_W, 20);
        getContentPane().add(l);
        m.setBounds(L_X + L_W + 10, 70, C_W / 2, 20);
        getContentPane().add(m);
        w.setBounds(L_X + L_W + 10 + C_W / 2, 70, C_W / 2, 20);
        // Female is choice by default
        w.setSelected(true);
        getContentPane().add(w);

        // Date of birth
        l = new JLabel("Date of birth:", JLabel.RIGHT);
        l.setBounds(L_X, 90, L_W, 20);
        getContentPane().add(l);
        dateOfBirth.setBounds(L_X + L_W + 10, 90, C_W, 20);
        getContentPane().add(dateOfBirth);

        // Group
        l = new JLabel("Group:", JLabel.RIGHT);
        l.setBounds(L_X, 115, L_W, 25);
        getContentPane().add(l);
        groupList.setBounds(L_X + L_W + 10, 115, C_W, 25);
        getContentPane().add(groupList);

        // Year in college
        l = new JLabel("Year in college:", JLabel.RIGHT);
        l.setBounds(L_X, 145, L_W, 20);
        getContentPane().add(l);
        year.setBounds(L_X + L_W + 10, 145, C_W, 20);
        getContentPane().add(year);

        JButton btnOk = new JButton("OK");
        btnOk.setName("OK");
        btnOk.addActionListener(this);
        btnOk.setBounds(L_X + L_W + C_W + 10 + 50, 10, 100, 25);
        getContentPane().add(btnOk);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setName("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setBounds(L_X + L_W + C_W + 10 + 50, 40, 100, 25);
        getContentPane().add(btnCancel);

        if (newStudent) {
            JButton btnNew = new JButton("Save");
            btnNew.setName("Save");
            btnNew.addActionListener(this);
            btnNew.setBounds(L_X + L_W + C_W + 10 + 50, 70, 100, 25);
            getContentPane().add(btnNew);
        }

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        setBounds(((int) d.getWidth() - StudentDialog.D_WIDTH) / 2, ((int) d.getHeight() - StudentDialog.D_HEIGHT) / 2,
                StudentDialog.D_WIDTH, StudentDialog.D_HEIGHT);
    }

    // Fills field automatically
    public void setStudent(Student st) {
        studentId = st.getStudentId();
        firstName.setText(st.getFirstName());
        lastName.setText(st.getLastName());
        dateOfBirth.getModel().setValue(st.getDateOfBirth());
        for (Enumeration e = sex.getElements(); e.hasMoreElements();) {
            AbstractButton ab = (AbstractButton) e.nextElement();
            ab.setSelected(ab.getActionCommand().equals("" + st.getSex()));
        }
        year.getModel().setValue(st.getYearInCollege());
        for (int i = 0; i < groupList.getModel().getSize(); i++) {
            Group g = groupList.getModel().getElementAt(i);
            if (g.getGroupId() == st.getGroupId()) {
                groupList.setSelectedIndex(i);
                break;
            }
        }
    }

    // Builds/returns new students based on input data
    public Student getStudent() {
        Student st = new Student();
        st.setStudentId(studentId);
        st.setFirstName(firstName.getText());
        st.setLastName(lastName.getText());
        Date d = ((SpinnerDateModel) dateOfBirth.getModel()).getDate();
        st.setDateOfBirth(d);
        for (Enumeration e = sex.getElements(); e.hasMoreElements();) {
            AbstractButton ab = (AbstractButton) e.nextElement();
            if (ab.isSelected()) {
                st.setSex(ab.getActionCommand().charAt(0));
            }
        }
        int y = ((SpinnerNumberModel) year.getModel()).getNumber().intValue();
        st.setYearInCollege(y);
        st.setGroupId(((Group) groupList.getSelectedItem()).getGroupId());
        return st;
    }

    // Returns state of SAVE button
    public boolean getResult() {
        return result;
    }

    // Button press handler
    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();

        // If pressed Save, that mean a new student will be added
        // however dialog won't be closed so a new student can be added
        if (src.getName().equals("Save")) {
            result = true;
            try {
                ManagementSystem.getInstance().insertStudent(getStudent());
                mainFrame.reloadStudents();
                firstName.setText("");
                lastName.setText("");
            } catch (Exception sql_e) {
                JOptionPane.showMessageDialog(this, sql_e.getMessage());
            }
            return;
        }
        if (src.getName().equals("OK")) {
            result = true;
        }
        if (src.getName().equals("Cancel")) {
            result = false;
        }
        setVisible(false);
    }
}