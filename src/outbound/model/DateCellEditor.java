package outbound.model;

/*
 * IntegerEditor is used by TableFTFEditDemo.java.
 */
import inbound.model.*;
import utility.*;
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.util.Date;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * Implements a cell editor that uses a JDateChooser to edit Date values.
 */
public class DateCellEditor extends DefaultCellEditor {

    private JDateChooser dc;

    public DateCellEditor() {
        super(new JTextField());
        dc = new JDateChooser();
        dc.getDateEditor().setEnabled(false);
        
        //chi cho chon nhung ngay trong qua khuuuuuuu
        dc.getJCalendar().setMaxSelectableDate(new Date());
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
