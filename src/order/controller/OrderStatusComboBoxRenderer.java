/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package order.controller;

import customer.model.CustomerLevel;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import static javax.swing.SwingConstants.CENTER;
import order.model.OrderStatus;

/**
 *
 * @author Hoang
 */
public class OrderStatusComboBoxRenderer extends JLabel implements ListCellRenderer<OrderStatus> {

    public OrderStatusComboBoxRenderer() {
        setHorizontalAlignment(LEFT);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends OrderStatus> list, OrderStatus value, int index, boolean isSelected, boolean cellHasFocus) {
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
