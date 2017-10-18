package employee.controller;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
/**
 *
 * @author BonBon
 */
public class UserNameComboBoxCellEditor extends DefaultCellEditor {

    private JComboBox cb;

    public UserNameComboBoxCellEditor(UserNameComboBoxModel userNameComboBoxModel) {
        super(new JComboBox(userNameComboBoxModel));
        cb = (JComboBox) getComponent();
        cb.setRenderer(new UserNameComboBoxRenderer());
    }

    // Override to invoke setValue on the formatted text field.
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JComboBox cb2 = (JComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
        cb2.setSelectedItem(((UserNameComboBoxModel) cb2.getModel()).getUserNameFromValue((String) value));
        return cb2;
    }
}
