/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outbound.controller;

import inbound.controller.*;
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author tuan
 */
public class JDateChooserRenderer extends JDateChooser implements TableCellRenderer {
     Date inDate;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        // TODO Auto-generated method stub

        if (value instanceof Date){
            this.setDate((Date) value);
        } else if (value instanceof Calendar){
            this.setCalendar((Calendar) value);
        }
        return this;
    }
}
