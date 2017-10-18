/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package branch.dao;

import branch.dao.BranchDAOImpl;
import branch.model.Branch;
import branch.model.BranchTableModel;
import database.DBProvider;
import database.IDAO;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;

/**
 *
 * @author tuan
 */
public class CheckBoxCellEditor  extends DefaultCellEditor implements ItemListener{
    protected static JCheckBox checkBox = new JCheckBox();
    private BranchTableModel branchTableModel;
    private Branch selectedBranch;
    private CachedRowSet dbCrs;
    private final CachedRowSet tableCrs;
    public CheckBoxCellEditor(BranchTableModel btm,Branch branch) {
        super(checkBox);
        tableCrs = new database.DBProvider().getCRS(Branch.Query_Show);
        this.branchTableModel = btm;
        this.selectedBranch = branch;
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        System.err.println("vo day ne nha");
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
         if (value == null)   
            return checkBox;   
        checkBox.addItemListener(this);   
        if (((Boolean) value).booleanValue())   
            checkBox.setSelected(true);   
        else 
            checkBox.setSelected(false);   
   
        return checkBox;   
    }

    @Override
    public Object getCellEditorValue() {
        if(checkBox.isSelected())   
            return Boolean.TRUE;  
        else   
            return Boolean.FALSE;   
    }
    
    @Override
    public void itemStateChanged(ItemEvent e) {
        System.err.println("eeeee");
       dbCrs = new database.DBProvider().getCRS(Branch.Query_Update);
        try {
            dbCrs.setString(1,selectedBranch.getBraName());
            dbCrs.setBoolean(2, selectedBranch.getBraStatus());
            dbCrs.setInt(3, selectedBranch.getBraID());
            dbCrs.execute();
            tableCrs.execute();
            
            } catch (SQLException ex) {
            Logger.getLogger(BranchDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JOptionPane.showMessageDialog(null, "Update successfully!");
}

    @Override
    public void addCellEditorListener(CellEditorListener l) {
         System.err.println("eeeee");
       dbCrs = new database.DBProvider().getCRS(Branch.Query_Update);
        try {
            dbCrs.setString(1,selectedBranch.getBraName());
            dbCrs.setBoolean(2, selectedBranch.getBraStatus());
            dbCrs.setInt(3, selectedBranch.getBraID());
            dbCrs.execute();
            tableCrs.execute();
            
            } catch (SQLException ex) {
            Logger.getLogger(BranchDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JOptionPane.showMessageDialog(null, "Update successfully!");
    }
    
}
