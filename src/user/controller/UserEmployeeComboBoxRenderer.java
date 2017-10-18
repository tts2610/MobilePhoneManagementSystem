/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user.controller;


import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import user.model.UserEmployee;


/**
 *
 * @author BonBon
 */
public class UserEmployeeComboBoxRenderer extends JLabel implements ListCellRenderer<UserEmployee> {

    public UserEmployeeComboBoxRenderer() {
        setHorizontalAlignment(LEFT);
    }


    @Override
    public Component getListCellRendererComponent(JList<? extends UserEmployee> list, UserEmployee value, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(Color.RED);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        if (value != null) {
            setText(String.format("ID: %-10s   %s",value.getEmpID(),value.getEmpName()));
        }
        return this;

    }
}
