/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.dao;


import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

/**
 *
 * @author tuan
 */
public class BranchNameComboBoxRender extends JLabel implements ListCellRenderer<Branch> {
    JTextField jtf = new JTextField();
    public BranchNameComboBoxRender() {
        setHorizontalAlignment(CENTER);
    }
    

    @Override
    public Component getListCellRendererComponent(JList<? extends Branch> list, Branch value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value!=null){
        if (isSelected) {
           jtf.setBackground(Color.ORANGE);
            jtf.setForeground(Color.BLACK);
            
        } else {
            jtf.setBackground(new java.awt.Color(51, 51, 51));
            jtf.setForeground(list.getForeground());
        }
        
        
        jtf.setText(value.getBraName() + "");
        jtf.setBorder(null);
        jtf.setHorizontalAlignment(CENTER);
        }
        return jtf;
    }
    
    
}
