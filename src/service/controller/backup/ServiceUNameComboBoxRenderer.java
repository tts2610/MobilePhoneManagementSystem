/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.controller.backup;

import service.controller.*;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import static javax.swing.SwingConstants.CENTER;
import user.model.User;

/**
 *
 * @author BonBon
 */
public class ServiceUNameComboBoxRenderer extends JLabel implements ListCellRenderer<User> {

    public ServiceUNameComboBoxRenderer() {
        setHorizontalAlignment(CENTER);
    }


    @Override
    public Component getListCellRendererComponent(JList<? extends User> list, User value, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(Color.RED);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setText(value.getUserName()+ "");
        return this;

    }
}
