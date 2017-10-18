package order.controller;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import order.model.OrderBranch;

/**
 *
 * @author Hoang
 */
public class OrderBranchListCellRenderer implements ListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        OrderBranch ob = (OrderBranch) value;
        JCheckBox checkbox = new JCheckBox(ob.getBraName());
        checkbox.setBackground(isSelected
                ? list.getSelectionBackground() : list.getBackground());
        checkbox.setForeground(isSelected
                ? list.getSelectionForeground() : list.getForeground());
        checkbox.setFocusPainted(false);
        checkbox.setBorderPainted(true);
        checkbox.setBorder(null);
        checkbox.setSelected(isSelected);
        checkbox.setPreferredSize(new Dimension(150,30));
        return checkbox;
    }

}
