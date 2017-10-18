package service.controller;


import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import service.model.ServiceDetails;
/**
 *
 * @author BonBon
 */
public class ServiceDetailsComboBoxCellEditor extends DefaultCellEditor {

    private JComboBox cb;

    public ServiceDetailsComboBoxCellEditor(ServiceDetailsComboBoxModel serviceDetailsComboBoxModel) {
        super(new JComboBox(serviceDetailsComboBoxModel));
        cb = (JComboBox) getComponent();
        cb.setRenderer(new ServiceDetailsComboBoxRenderer());
    }

    // Override to invoke setValue on the formatted text field.
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JComboBox cb2 = (JComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
        ServiceDetails op = ((ServiceDetailsComboBoxModel) cb2.getModel()).getProductFromName((String) value);
        cb2.setSelectedItem(op);
        return cb2;
    }
}
