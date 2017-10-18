/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inbound.controller;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author tuan
 */
public class MyTableModel_InDetails extends AbstractTableModel {
    private String[] columnNames;
    private Object[][] data;
    public MyTableModel_InDetails(String[] columnNames,Object[][]data) {
        this.columnNames = columnNames;
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }
    
    public String getColumnName(int col) {
            return columnNames[col];
        }
    public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }
    public void setValueAt(Object value, int row, int col) {
                System.out.println("Setting value at " + row + "," + col
                                   + " to " + value
                                   + " (an instance of "
                                   + value.getClass() + ")");
            

            data[row][col] = value;
            fireTableCellUpdated(row, col);
                System.out.println("New value of data:");
                printDebugData();
            
        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    
}
