package utility;

/*
 * IntegerEditor is used by TableFTFEditDemo.java.
 */
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 * Implements a cell editor that uses a spinner to edit Integer values.
 */
public class SpinnerCellEditor extends DefaultCellEditor {

    private JSpinner spinner;

    public SpinnerCellEditor(int min, int max) {
        super(new JTextField());
        spinner = new JSpinner(new SpinnerNumberModel(min, min, max, 1));

        // Validate input
        JSpinner.NumberEditor ne = (JSpinner.NumberEditor) spinner.getEditor();
        SwingUtils.validateIntegerInput(ne.getTextField());
    }

// Override to ensure that the value remains an Integer.
    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        spinner.setValue(value);
        return spinner;
    }
}
