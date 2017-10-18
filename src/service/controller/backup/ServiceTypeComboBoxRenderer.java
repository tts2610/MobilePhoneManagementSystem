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
import service.model.ServiceType;

/**
 *
 * @author BonBon
 */
public class ServiceTypeComboBoxRenderer extends JLabel implements ListCellRenderer<ServiceType> {

    public ServiceTypeComboBoxRenderer() {
        setHorizontalAlignment(LEFT);
    }


    @Override
    public Component getListCellRendererComponent(JList<? extends ServiceType> list, ServiceType value, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(Color.RED);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        if (value != null) {
            setText(value.getSerTypeName()+ "");
        }
        return this;

    }
}
