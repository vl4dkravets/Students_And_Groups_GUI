package frame;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import logic.Group;

public class GroupDialog extends JDialog implements ActionListener {

    private static final int D_HEIGHT = 150;   // height of dialog window
    private final static int D_WIDTH = 200;   // width of dialog window
    private final JSpinner spYear;
    private final JComboBox<Group> groupList;
    private final JButton btnOk = new JButton("OK");
    private final JButton btnCancel = new JButton("Cancel");
    private boolean result = false;

    public GroupDialog(int year, List<Group> groups) {
        setTitle("Moving group");

        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);
        GridBagConstraints c = new GridBagConstraints();
        // margins for each element in the dialog window
        c.insets = new Insets(5, 5, 5, 5);

        // Setting for New Group label element
        JLabel l = new JLabel("New group:");
        c.gridwidth = GridBagConstraints.RELATIVE;
        // Doesn't take any extra space
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        gbl.setConstraints(l, c);
        getContentPane().add(l);

        groupList = new JComboBox<>(new Vector<>(groups));
        // Element takes the rest of width
        c.gridwidth = GridBagConstraints.REMAINDER;
        // Fills all the available space
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        gbl.setConstraints(groupList, c);
        getContentPane().add(groupList);

        l = new JLabel("New year:");
        // Means there will be some space left
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        gbl.setConstraints(l, c);
        getContentPane().add(l);

        // Add extra extra year, considering a new group that will
        // be added in the next year from currently specified one
        spYear = new JSpinner(new SpinnerNumberModel(year + 1, 1900, 2100, 1));
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        gbl.setConstraints(spYear, c);
        getContentPane().add(spYear);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        btnOk.setName("OK");
        btnOk.addActionListener(this);
        gbl.setConstraints(btnOk, c);
        getContentPane().add(btnOk);

        btnCancel.setName("Cancel");
        btnCancel.addActionListener(this);
        gbl.setConstraints(btnCancel, c);
        getContentPane().add(btnCancel);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        // Position dialog in the center of the window
        setBounds(((int) d.getWidth() - GroupDialog.D_WIDTH) / 2, ((int) d.getHeight() - GroupDialog.D_HEIGHT) / 2,
                GroupDialog.D_WIDTH, GroupDialog.D_HEIGHT);
    }

    // Returns the year specified in the spinner
    public int getYear() {
        return ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
    }

    // Returns highlighted group
    public Group getGroup() {
        if (groupList.getModel().getSize() > 0) {
            return (Group) groupList.getSelectedItem();
        }
        return null;
    }

    // Determines which button was pressed
    // true = save effect
    public boolean getResult() {
        return result;
    }

    // Event handler
    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();
        if (src.getName().equals("OK")) {
            result = true;
        }
        if (src.getName().equals("Cancel")) {
            result = false;
        }
        // Close dialog after any button was pressed
        setVisible(false);
    }
}