package utility;

/*
 * IntegerEditor is used by TableFTFEditDemo.java.
 */
import java.awt.Component;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.InputVerifier;
import javax.swing.JTable;
import utility.SwingUtils;

/**
 * Implements a cell editor that uses a formatted text field to edit Integer
 * values.
 */
public class StringCellEditor extends DefaultCellEditor {

    private JTextField tf;
    private int minLength;

    public StringCellEditor(int minLength, int maxLength, String regex) {
        super(new JTextField());
        tf = (JTextField) getComponent();
        this.minLength = minLength;

        SwingUtils.validateStringInput(tf, minLength, maxLength, regex);
    }

    //Override to check whether the edit is valid,
    //setting the value if it is and complaining if
    //it isn't.  If it's OK for the editor to go
    //away, we need to invoke the superclass's version 
    //of this method so that everything gets cleaned up.
    @Override
    public boolean stopCellEditing() {
        boolean result = false;
        int textLength = tf.getText().trim().length();
        if (minLength > 0) {
            if (textLength > 0) {
                if (textLength >= minLength) {
                    result = true;
                } else {
                    SwingUtils.showErrorDialog("Must have at least " + minLength + " character(s) !");
                }
            } else {
                SwingUtils.showErrorDialog("Cannot be empty !");
            }
        } else {
            result = true;
        }
        return result ? super.stopCellEditing() : false;
    }
}
