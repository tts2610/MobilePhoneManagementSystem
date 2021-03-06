/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.controller;


import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import service.model.ServiceStatus;

/**
 *
 * @author BonBon
 */
public class ServiceStatusComboBoxRenderer extends JLabel implements ListCellRenderer<ServiceStatus> {

    public ServiceStatusComboBoxRenderer() {
        setHorizontalAlignment(LEFT);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ServiceStatus> list, ServiceStatus value, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(Color.RED);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        if (value != null) {
            setText(value.getSttName() + "");
        }
        return this;
    }
}
