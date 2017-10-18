/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import database.IDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

/**
 *
 * @author Hoang
 * @param <M>
 */
public class CustomizedComboBoxModel<M> extends AbstractListModel<M> implements MutableComboBoxModel<M> {

    protected List<M> list;
    protected M selectedItem;
    protected IDAO<M> daoImpl;

    /**
     * ComboBox model cho tat ca combobox trong app
     *
     * @param daoImpl
     */
    public CustomizedComboBoxModel(IDAO<M> daoImpl) {
        this.daoImpl = daoImpl;
        list = daoImpl.getList();
    }
    
    public void refresh(){
        try {
            daoImpl = daoImpl.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(CustomizedComboBoxModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        list = daoImpl.getList();
        fireContentsChanged(this, 0, list.size()-1);
    }

    // implements javax.swing.ComboBoxModel
    /**
     * Set the value of the selected item. The selected item may be null.
     *
     * @param item The combo box value or null for no selection.
     */
    @Override
    public void setSelectedItem(Object item) {
        if ((selectedItem != null && !selectedItem.equals(item))
                || selectedItem == null && item != null) {
            selectedItem = (M) item;
            fireContentsChanged(this, -1, -1);
        }
    }

    // implements javax.swing.ComboBoxModel
    @Override
    public M getSelectedItem() {
        return selectedItem;
    }

    // implements javax.swing.ListModel
    @Override
    public int getSize() {
        return list.size();
    }

    // implements javax.swing.ListModel
    @Override
    public M getElementAt(int index) {
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        } else {
            return null;
        }
    }

    /**
     * Returns the index-position of the specified object in the list.
     *
     * @param item
     * @return an int representing the index position, where 0 is the first
     * position
     */
    public int getIndexOf(M item) {
        return list.indexOf(item);
    }

    // implements javax.swing.MutableComboBoxModel
    @Override
    public void addElement(M item) {
        list.add(item);
        fireIntervalAdded(this, list.size() - 1, list.size() - 1);
        if (list.size() == 1 && selectedItem == null && item != null) {
            setSelectedItem(item);
        }
    }

    // implements javax.swing.MutableComboBoxModel
    @Override
    public void insertElementAt(M item, int index) {
        list.add(index, item);
        fireIntervalAdded(this, index, index);
    }

    // implements javax.swing.MutableComboBoxModel
    @Override
    public void removeElementAt(int index) {
        if (getElementAt(index) == selectedItem) {
            if (index == 0) {
                setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
            } else {
                setSelectedItem(getElementAt(index - 1));
            }
        }

        list.remove(index);

        fireIntervalRemoved(this, index, index);
    }

    // implements javax.swing.MutableComboBoxModel
    @Override
    public void removeElement(Object item) {
        int index = list.indexOf(item);
        if (index != -1) {
            removeElementAt(index);
        }
    }

    /**
     * Empties the list.
     */
    public void removeAllElements() {
        if (list.size() > 0) {
            int firstIndex = 0;
            int lastIndex = list.size() - 1;
            list.clear();
            selectedItem = null;
            fireIntervalRemoved(this, firstIndex, lastIndex);
        } else {
            selectedItem = null;
        }
    }
}
