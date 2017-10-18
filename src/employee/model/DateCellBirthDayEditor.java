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
public class DateCellBirthDayEditor extends DefaultCellEditor {

    private JDateChooser dc;

    public DateCellBirthDayEditor() {
        super(new JTextField());
        dc = new JDateChooser();
        dc.setDateFormatString("MMM dd, yyyy");
        Calendar c = Calendar.getInstance();
        c.set(1945, 11, 31);
        dc.getJCalendar().setMinSelectableDate(c.getTime());
        
        Date today=new Date();
        Calendar c1=Calendar.getInstance();
        c1.setTime(today);
        int year=c1.get(Calendar.YEAR);
        int month=c1.get(Calendar.MONTH);
        int day=c1.get(Calendar.DATE);
        c1.set(year-18,month,day-1);
//        System.out.println(c1.getTime());
        dc.getJCalendar().setMaxSelectableDate(c1.getTime());
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
