package utility;

import database.IDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

/**
 * Class table model dung chung cho cac table trong app.
 *
 * @author Hoang
 * @param <M> Model
 */
public abstract class CustomizedTableModel<M> extends AbstractTableModel {

    protected List<M> list;
    protected M item;   //Dung cho get/setValueAt
    protected IDAO<M> daoImpl;
    protected String[] columnNames;
    protected int selectingIndex;

    public CustomizedTableModel(IDAO<M> daoImpl, String[] columnNames) {
        this.daoImpl = daoImpl;
        this.columnNames = columnNames;
        list = daoImpl.getList();
    }
    
    public M getElementAt(int index){
        System.out.println("index: "+index);
        return list.get(index);
    }

    public void addElement(M item) {
        list.add(item);
        fireTableRowsInserted(list.indexOf(item), list.indexOf(item));
    }
    
    public void removeElementAt(int index){
        
    }

    public int getSelectingIndex() {
        return selectingIndex;
    }

    public void setSelectingIndex(int selectingIndex) {
        this.selectingIndex = selectingIndex;          // Table model tinh tu 0
        daoImpl.setSelectingIndex(selectingIndex + 1); // ResultSet tinh tu 1
    }

    public boolean insert(M item) {
        boolean result = false;
        if (daoImpl.insert(item)) {
            list = daoImpl.getList();
            fireTableRowsInserted(list.indexOf(item), list.indexOf(item));
            result = true;
        }
        return result;
    }

    public boolean update(M item) {
        boolean result = false;
        if (daoImpl.update(item)) {
            list = daoImpl.getList();
            fireTableRowsUpdated(list.indexOf(item), list.indexOf(item));
            result = true;
        }
        return result;
    }

    public boolean delete(M item) {
        boolean result = false;
        if (daoImpl.delete(item)) {
            list = daoImpl.getList();
            fireTableRowsDeleted(list.indexOf(item), list.indexOf(item));
            result = true;
        }
        return result;
    }

    public void refresh() {
        try {
            daoImpl = daoImpl.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(CustomizedTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        list = daoImpl.getList();
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0; //Khong cho sua column ID
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
}
