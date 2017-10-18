package utility;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Hoang
 */
public class CheckBoxListCellRenderer implements ListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JCheckBox checkbox = (JCheckBox) value;
        checkbox.setBackground(isSelected
                ? list.getSelectionBackground() : list.getBackground());
        checkbox.setForeground(isSelected
                ? list.getSelectionForeground() : list.getForeground());
        checkbox.setFocusPainted(false);
        checkbox.setBorderPainted(true);
        checkbox.setBorder(null);
        return checkbox;
    }

}
