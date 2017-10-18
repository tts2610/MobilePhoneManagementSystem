package inbound.controller;

/*
 * IntegerEditor is used by TableFTFEditDemo.java.
 */
import customer.controller.CustomerLevelComboBoxRenderer;

import inbound.model.SupplierComboboxModel;
import inbound.model.SupplierComboboxRenderer;
import inbound.model.UserComboboxModel;
import inbound.model.UserNameComboboxRenderer;
import java.awt.Component;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;

/**
 * Implements a cell editor that uses a formatted text field to edit Integer
 * values.
 */
public class ComboBoxCellEditor extends DefaultCellEditor {

    private JComboBox cb;

    public ComboBoxCellEditor(ComboBoxModel comboBoxModel) {
        super(new JComboBox(comboBoxModel));
        cb = (JComboBox) getComponent();
        if(comboBoxModel instanceof SupplierComboboxModel){
            cb.setRenderer(new SupplierComboboxRenderer());
        }
        if(comboBoxModel instanceof UserComboboxModel){
            cb.setRenderer(new UserNameComboboxRenderer());
        }
    }

    // Override to invoke setValue on the formatted text field.
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JComboBox cb2 = (JComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
        ComboBoxModel cbModel = cb2.getModel();
        
        //Neu model la supplier name
        if (cbModel instanceof SupplierComboboxModel) {
            cb2.setSelectedItem(((SupplierComboboxModel) cbModel).getSupplierFromValue((String) value));
        }
        return cb2;
    }
}
