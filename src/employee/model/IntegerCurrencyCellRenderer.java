package employee.model;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import employee.model.EmployeeSwingUtils.FormatType;

/**
 *
 * @author Hoang
 */
public class IntegerCurrencyCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setText(EmployeeSwingUtils.formatString((int) value, FormatType.CURRENCYINTERGER));
        return label;
    }
}
