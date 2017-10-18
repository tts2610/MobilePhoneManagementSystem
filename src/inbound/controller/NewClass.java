/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inbound.controller;

import inbound.model.Supplier;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 *
 * @author tuan
 */
public class NewClass  extends JLabel implements ListCellRenderer<Supplier> {

    @Override
    public Component getListCellRendererComponent(JList<? extends Supplier> list, Supplier value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
           setText(value.getSupName());
        }
        return this;
    }
}
