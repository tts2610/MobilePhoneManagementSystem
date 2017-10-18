package product.dao;

/*
 * IntegerEditor is used by TableFTFEditDemo.java.
 */
import utility.*;
import customer.controller.CustomerLevelComboBoxRenderer;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.ComboBoxModel;
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
        cb.setRenderer(new BranchNameComboBoxRender());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
         JComboBox cb2 = (JComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
        ComboBoxModel cbModel = cb2.getModel();

        // Neu model la branch name
        if (cbModel instanceof BranchNameComboBoxModel) {
            cb2.setSelectedItem(((BranchNameComboBoxModel) cbModel).getBranchFromValue((String) value));
        }
        return cb2;
    }
    
}
