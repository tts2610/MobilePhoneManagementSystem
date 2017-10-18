package employee.model;

/*
 * IntegerEditor is used by TableFTFEditDemo.java.
 */
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.util.Calendar;
import java.util.Date;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * Implements a cell editor that uses a JDateChooser to edit Date values.
 */
public class DateCellWorkingDateEditor extends DefaultCellEditor {

    private JDateChooser dc;

    public DateCellWorkingDateEditor() {
        super(new JTextField());
        dc = new JDateChooser();
        dc.setDateFormatString("MMM dd, yyyy");
        Calendar c = Calendar.getInstance();
        dc.getJCalendar().setMaxSelectableDate(c.getTime());
        
        c.set(2010, 0, 1);
        dc.getJCalendar().setMinSelectableDate(c.getTime());
        dc.getDateEditor().setEnabled(false);
    }

// Override to ensure that the value remains an Integer.
    @Override
    public Object getCellEditorValue() {
        return dc.getDate();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        dc.setDate((Date) value);
        return dc;
    }
}
