/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outbound.model;

import inbound.model.*;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import static javax.swing.SwingConstants.CENTER;
import inbound.model.Supplier;

/**
 *
 * @author tuan
 */
public class SupplierComboboxRenderer extends JLabel implements ListCellRenderer<Supplier> {
    public SupplierComboboxRenderer(){
        setHorizontalAlignment(CENTER);
    }
    @Override
    public Component getListCellRendererComponent(JList<? extends Supplier> list, Supplier value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value!=null){
        if (isSelected) {
            setBackground(Color.ORANGE);
            setForeground(Color.ORANGE);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        
        setText(value.getSupName()+ "");
        }
        return this;
    }

    
    
}
