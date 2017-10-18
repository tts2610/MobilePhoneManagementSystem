package user.controller;


import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;


/**
 *
 * @author BonBon
 */
public class UserEmployeeComboBoxCellEditor extends DefaultCellEditor {

    private JComboBox cb;

    public UserEmployeeComboBoxCellEditor(UserEmployeeComboBoxModel userEmployeeComboBoxModel) {
        super(new JComboBox(userEmployeeComboBoxModel));
        cb = (JComboBox) getComponent();
        cb.setRenderer(new UserEmployeeComboBoxRenderer());
    }

    // Override to invoke setValue on the formatted text field.
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JComboBox cb2 = (JComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
        cb2.setSelectedItem(((UserEmployeeComboBoxModel) cb2.getModel()).getUserEmployeeNameFromValue((String) value));
        return cb2;
    }
}
