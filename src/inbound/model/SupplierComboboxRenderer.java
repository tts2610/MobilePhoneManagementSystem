/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inbound.model;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import static javax.swing.SwingConstants.CENTER;
import inbound.model.Supplier;
import javax.swing.JTextField;

/**
 *
 * @author tuan
 */
public class SupplierComboboxRenderer extends JLabel implements ListCellRenderer<Supplier> {
    JTextField jtf = new JTextField();
    public SupplierComboboxRenderer(){
        setHorizontalAlignment(CENTER);
    }
    @Override
    public Component getListCellRendererComponent(JList<? extends Supplier> list, Supplier value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value!=null){
        if (isSelected) {
            jtf.setBackground(Color.ORANGE);
            jtf.setForeground(Color.BLACK);
        } else {
            jtf.setBackground(new java.awt.Color(51, 51, 51));
            jtf.setForeground(list.getForeground());
        }
        
        jtf.setText(value.getSupName()+ "");
        jtf.setBorder(null);
        jtf.setHorizontalAlignment(CENTER);
        }
        return jtf;
    }

    
    
}
