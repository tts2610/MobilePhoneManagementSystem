package utility;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Toolkit;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.JTable;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.DocumentFilter;
import javax.swing.text.NumberFormatter;

/**
 * Implements a cell editor that uses a formatted text field to edit Integer
 * values.
 */
public class IntegerCellEditor extends DefaultCellEditor {

    private JFormattedTextField ftf;
    private NumberFormat integerFormat;
    private int minimum, maximum;

    public IntegerCellEditor(int min, int max) {
        super(new JFormattedTextField());
        ftf = (JFormattedTextField) getComponent();
        minimum = min;
        maximum = max;

        //Set up the editor for the integer cells.
        integerFormat = NumberFormat.getIntegerInstance();
        NumberFormatter intFormatter = new NumberFormatter(integerFormat);
        intFormatter.setFormat(integerFormat);
        intFormatter.setMinimum(minimum);
        intFormatter.setMaximum(maximum);

        ftf.setFormatterFactory(new DefaultFormatterFactory(intFormatter));
        ftf.setValue(minimum);
        ftf.setHorizontalAlignment(JTextField.LEFT);
        ftf.setFocusLostBehavior(JFormattedTextField.PERSIST);

    }
    
    //Override to ensure that the value remains an Integer.
    @Override
    public Object getCellEditorValue() {
        JFormattedTextField ftf2 = (JFormattedTextField) getComponent();
        Object o = ftf2.getValue();
        if (o instanceof Integer) {
            return o;
        } else if (o instanceof Number) {
            return ((Number) o).intValue();
        } else {
            try {
                return integerFormat.parseObject(o.toString());
            } catch (ParseException exc) {
                System.err.println("getCellEditorValue: can't parse o: " + o);
                return null;
            }
        }
    }

    // Override to invoke setValue on the formatted text field.
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JFormattedTextField ftf2 = (JFormattedTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
        ftf2.setValue(value);
        return ftf2;
    }

    //Override to check whether the edit is valid,
    //setting the value if it is and complaining if
    //it isn't.  If it's OK for the editor to go
    //away, we need to invoke the superclass's version 
    //of this method so that everything gets cleaned up.
    @Override
    public boolean stopCellEditing() {
//        System.out.println("stopcelledit");
        JFormattedTextField ftf2 = (JFormattedTextField) getComponent();
        if (ftf2.isEditValid()) {
//            System.out.println("stopcelledit:isValid");
//               userSaysRevert();
            try {
                ftf2.commitEdit();
            } catch (java.text.ParseException exc) {
            }

        } else { //text is invalid
//            System.out.println("stopcelledit:notValid");

            if (!userSaysRevert()) { //user wants to edit
                return false; //don't let the editor go away
            }
        }
        return super.stopCellEditing();
    }

    /**
     * Lets the user know that the text they entered is bad. Returns true if the
     * user elects to revert to the last good value. Otherwise, returns false,
     * indicating that the user wants to continue editing.
     *
     * @return boolean
     */
    protected boolean userSaysRevert() {
        Toolkit.getDefaultToolkit().beep();
//        System.out.println("userSaysRevert1: " + ftf.getValue());

        if (SwingUtils.showInputValidationDialog(
                "The value must be an integer between "
                + minimum + " and "
                + maximum + ".\n"
                + "You can either reinput or "
                + "revert to the last valid value.") == JOptionPane.NO_OPTION) { //Revert!
//            System.out.println("userSaysRevert2: " + ftf.getValue());
            ftf.setValue(ftf.getValue());
            return true;
        }
        ftf.selectAll();
        return false;
    }
    
    private static class MyDocumentFilter extends DocumentFilter {

        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            System.out.println("insert");
            if (string.matches("\\d+")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("\\d+")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}
