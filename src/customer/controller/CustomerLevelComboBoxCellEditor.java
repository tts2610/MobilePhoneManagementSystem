package customer.controller;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;

public class CustomerLevelComboBoxCellEditor extends DefaultCellEditor {

    private JComboBox cb;

    public CustomerLevelComboBoxCellEditor(CustomerLevelComboBoxModel customerLevelComboBoxModel) {
        super(new JComboBox(customerLevelComboBoxModel));
        cb = (JComboBox) getComponent();
        cb.setRenderer(new CustomerLevelComboBoxRenderer());
    }

    // Override to invoke setValue on the formatted text field.
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JComboBox cb2 = (JComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
        cb2.setSelectedItem(((CustomerLevelComboBoxModel) cb2.getModel()).getCustomerLevelFromLevelName((String) value));
        return cb2;
    }
}
